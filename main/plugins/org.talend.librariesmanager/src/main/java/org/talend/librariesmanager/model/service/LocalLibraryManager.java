// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.librariesmanager.model.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EMap;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.ILibraryManagerUIService;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.librariesmanager.emf.librariesindex.LibrariesIndex;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class LocalLibraryManager implements ILibraryManagerService {

    private Set<String> jarList = new HashSet<String>();

    // map uri to absolute path
    // key = null, means uri not tested yet....
    // value is null = jar not existing
    // value set = absolute path of the jar
    private Map<String, String> uriJarInstalled = new HashMap<String, String>();

    boolean listToUpdate = false;

    // private long totalSizeCanBeReduced = 0;

    @Override
    public boolean isInitialized() {
        File indexFile = new File(LibrariesIndexManager.getInstance().getIndexFilePath());
        if (indexFile.exists()) {
            LibrariesIndexManager.getInstance().loadResource();
            return LibrariesIndexManager.getInstance().getIndex().isInitialized();
        }
        return false;
    }

    @Override
    public void setInitialized() {
        LibrariesIndexManager.getInstance().loadResource();
        LibrariesIndexManager.getInstance().getIndex().setInitialized(true);
        LibrariesIndexManager.getInstance().saveResource();
    }

    /**
     * @param jarFileUri : file:/.....
     */
    @Override
    public void deploy(URI jarFileUri, IProgressMonitor... monitorWrap) {
        String installLocation = getStorageDirectory().getAbsolutePath();
        File indexFile = new File(LibrariesIndexManager.getInstance().getIndexFilePath());
        if (indexFile.exists()) {
            LibrariesIndexManager.getInstance().loadResource();
        }

        try {
            File file = new File(jarFileUri);
            String contributeID = "";
            // MOD qiongli 2013,avoid NPE.TOP dosen't contain IComponentsService.
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IComponentsService.class)) {
                IComponentsService service = (IComponentsService) GlobalServiceRegister.getDefault().getService(
                        IComponentsService.class);
                Map<String, File> componentsFolders = service.getComponentsFactory().getComponentsProvidersFolder();
                Set<String> contributeIdSet = componentsFolders.keySet();
                for (String contributor : contributeIdSet) {
                    if (file.getAbsolutePath().contains(contributor)) {
                        contributeID = contributor;
                        break;
                    }
                }
            }
            if (!"".equals(contributeID)) {
                String actualPluginPath = FileLocator.getBundleFile(Platform.getBundle(contributeID)).getPath();
                // check if the path of the imported jar is at least from the same studio, in case import a jar from a
                // studio to another one.
                if (!file.getAbsolutePath().startsWith(actualPluginPath)) {
                    contributeID = "";
                }
            }
            if (file == null || !file.exists()) {
                return;
            }
            listToUpdate = true;
            if (contributeID.equals("")) {
                if (file.isDirectory()) {
                    FilesUtils.copyFolder(new File(jarFileUri), getStorageDirectory(), false,
                            FilesUtils.getExcludeSystemFilesFilter(), FilesUtils.getAcceptJARFilesFilter(), false, monitorWrap);
                } else {
                    File target = new File(installLocation, file.getName());
                    FilesUtils.copyFile(file, target);
                }
            } else {
                if ("org.talend.designer.components.model.UserComponentsProvider".contains(contributeID)
                        || "org.talend.designer.components.exchange.ExchangeComponentsProvider".contains(contributeID)) {
                    if (file.isDirectory()) {
                        FilesUtils.copyFolder(new File(jarFileUri), getStorageDirectory(), false,
                                FilesUtils.getExcludeSystemFilesFilter(), FilesUtils.getAcceptJARFilesFilter(), false,
                                monitorWrap);
                    } else {
                        File target = new File(installLocation, file.getName());
                        FilesUtils.copyFile(file, target);
                    }
                } else {
                    LibrariesIndex index = LibrariesIndexManager.getInstance().getIndex();
                    EMap<String, String> jarsToRelativePath = index.getJarsToRelativePath();
                    List<File> jarFiles = FilesUtils.getJarFilesFromFolder(file, null);
                    boolean modified = false;
                    if (jarFiles.size() > 0) {
                        for (File jarFile : jarFiles) {
                            String name = jarFile.getName();
                            String fullPath = jarFile.getAbsolutePath();
                            // caculate the relative path
                            if (fullPath.indexOf(contributeID) != -1) {
                                fullPath = new Path(fullPath).toPortableString();
                                String relativePath = fullPath.substring(fullPath.indexOf(contributeID));
                                if (!jarsToRelativePath.keySet().contains(name)) {
                                    jarsToRelativePath.put(name, relativePath);
                                    modified = true;
                                } else {
                                    // fix for TDI-25834 , in case the index.xml already stored some locations not exist
                                    // in product , replace it with a new location
                                    boolean jarFound = false;
                                    String existedPath = jarsToRelativePath.get(name);
                                    if (existedPath != null && existedPath.startsWith("platform:/")) {
                                        jarFound = checkJarInstalledFromPlatform(existedPath);
                                    }
                                    if (!jarFound) {
                                        jarsToRelativePath.put(name, relativePath);
                                    }
                                    // System.out.println("duplicate jar " + name + " found\n in :" +
                                    // jarsToRelativePath.get(name)
                                    // + "\n and : " + relativePath);
                                    // totalSizeCanBeReduced += jarFile.length();
                                    // System.out.println("total size can be reduced from:" + totalSizeCanBeReduced /
                                    // 1024 + "kb\n");
                                }
                            }
                        }
                        if (modified) {
                            LibrariesIndexManager.getInstance().saveResource();
                        }
                    }

                    // copy dll files
                    List<File> dlls = FilesUtils.getDllFilesFromFolder(file, null);
                    for (File dllFile : dlls) {
                        FilesUtils.copyFile(dllFile, new File(installLocation, dllFile.getName()));
                    }
                }
            }
        } catch (IOException e) {
            CommonExceptionHandler.process(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.IRepositoryBundleService#deploy(java.util.Collection,
     * org.eclipse.core.runtime.IProgressMonitor[])
     */
    @Override
    public void deploy(Collection<URI> jarFileUris, IProgressMonitor... monitorWrap) {
        if (jarFileUris == null || jarFileUris.size() == 0) {
            return;
        }
        for (URI uri : jarFileUris) {
            deploy(uri, monitorWrap);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.IRepositoryBundleService#retrieve(java.lang.String, java.lang.String)
     */
    @Override
    public boolean retrieve(String jarNeeded, String pathToStore, IProgressMonitor... monitorWrap) {
        return retrieve(jarNeeded, pathToStore, true, monitorWrap);
    }

    @Override
    public boolean retrieve(String jarNeeded, String pathToStore, boolean popUp, IProgressMonitor... monitorWrap) {
        LibrariesIndexManager.getInstance().loadResource();
        String sourcePath = null, targetPath = pathToStore;
        try {
            List<File> jarFiles = FilesUtils.getJarFilesFromFolder(getStorageDirectory(), jarNeeded);
            if (jarFiles.size() > 0) {
                File jarFile = jarFiles.get(0);
                File target = new File(StringUtils.trimToEmpty(pathToStore));
                if (!target.exists()) {
                    target.mkdirs();
                }
                sourcePath = jarFile.getAbsolutePath();
                FilesUtils.copyFile(jarFile, new File(pathToStore, jarFile.getName()));
                return true;
            }
            // retrieve jar from the index.xml if not find in lib/java
            else {
                EMap<String, String> jarsToRelative = LibrariesIndexManager.getInstance().getIndex().getJarsToRelativePath();
                String relativePath = jarsToRelative.get(jarNeeded);
                String bundleLocation = "";
                String jarLocation = "";

                Map<String, File> componentsFolders = null;
                Set<String> contributeIdSet = null;
                if (GlobalServiceRegister.getDefault().isServiceRegistered(IComponentsService.class)) {
                    IComponentsService service = (IComponentsService) GlobalServiceRegister.getDefault().getService(
                            IComponentsService.class);
                    componentsFolders = service.getComponentsFactory().getComponentsProvidersFolder();
                    contributeIdSet = componentsFolders.keySet();
                }

                boolean jarFound = false;
                if (relativePath != null) {
                    if (relativePath.startsWith("platform:/")) {
                        jarFound = checkJarInstalledFromPlatform(relativePath);
                        if (jarFound) {
                            jarLocation = uriJarInstalled.get(relativePath);
                        }
                        if (!jarFound) {
                            // some libraries maybe not exist in some product ,but there is configuration in index.xml
                            // build from component
                            if (!popUp) {
                                return false;
                            } else if (!CommonsPlugin.isHeadless()) {
                                if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
                                    ILibraryManagerUIService libUiService = (ILibraryManagerUIService) GlobalServiceRegister
                                            .getDefault().getService(ILibraryManagerUIService.class);

                                    libUiService.installModules(new String[] { jarNeeded });
                                }
                                return false;
                            }
                        }
                    } else {
                        if (componentsFolders != null && contributeIdSet != null) {
                            for (String contributor : contributeIdSet) {
                                if (relativePath.contains(contributor)) {
                                    // caculate the the absolute path of the jar
                                    bundleLocation = componentsFolders.get(contributor).getAbsolutePath();
                                    int index = bundleLocation.indexOf(contributor);
                                    jarLocation = new Path(bundleLocation.substring(0, index)).append(relativePath)
                                            .toPortableString();
                                    jarFound = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                sourcePath = jarLocation;

                if (!jarFound && popUp && !CommonsPlugin.isHeadless()) {
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
                        ILibraryManagerUIService libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault()
                                .getService(ILibraryManagerUIService.class);

                        libUiService.installModules(new String[] { jarNeeded });
                    }
                    return false;
                }

                if (!jarFound) {
                    // CommonExceptionHandler.log("Jar: " + jarNeeded + " not found, not in the plugins available:"
                    // + contributeIdSet);
                    return false;
                }
                FilesUtils.copyFile(new File(sourcePath), new File(pathToStore, jarNeeded));
                return true;
            }
        } catch (MalformedURLException e) {
            CommonExceptionHandler.process(e);
        } catch (IOException e) {
            CommonExceptionHandler.process(new Exception("Can not copy: " + sourcePath + " to :" + targetPath, e));
        }
        return false;

    }

    @Override
    public boolean retrieve(Collection<String> jarsNeeded, String pathToStore, boolean showDialog,
            IProgressMonitor... monitorWrap) {
        if (jarsNeeded == null || jarsNeeded.size() == 0) {
            return false;
        }
        List<String> jarNotFound = new ArrayList<String>();

        boolean allIsOK = true;
        for (String jar : jarsNeeded) {
            if (!retrieve(jar, pathToStore, false, monitorWrap)) {
                jarNotFound.add(jar);
                allIsOK = false;
            }
        }
        if (showDialog && !jarNotFound.isEmpty() && !CommonsPlugin.isHeadless()) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
                ILibraryManagerUIService libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault().getService(
                        ILibraryManagerUIService.class);
                libUiService.installModules(jarNotFound.toArray(new String[jarNotFound.size()]));
                jarsNeeded = new ArrayList<String>(jarNotFound);
                jarNotFound.clear();
                allIsOK = true;
                for (String jar : jarsNeeded) {
                    if (!retrieve(jar, pathToStore, false, monitorWrap)) {
                        jarNotFound.add(jar);
                        allIsOK = false;
                    }
                }
            }
        }

        return allIsOK;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.IRepositoryBundleService#retrieve(java.util.Collection, java.lang.String,
     * org.eclipse.core.runtime.IProgressMonitor[])
     */
    @Override
    public boolean retrieve(Collection<String> jarsNeeded, String pathToStore, IProgressMonitor... monitorWrap) {
        return retrieve(jarsNeeded, pathToStore, true, monitorWrap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.IRepositoryBundleService#list(org.eclipse.core.runtime.IProgressMonitor[])
     */
    @Override
    public Set<String> list(IProgressMonitor... monitorWrap) {
        return list(true, monitorWrap);
    }

    @Override
    public Set<String> list(boolean withComponent, IProgressMonitor... monitorWrap) {
        Set<String> names = new HashSet<String>();
        try {
            List<File> jarFiles = FilesUtils.getJarFilesFromFolder(getStorageDirectory(), null);
            if (jarFiles.size() > 0) {
                for (File file : jarFiles) {
                    names.add(file.getName());
                }
            }
        } catch (MalformedURLException e) {
            CommonExceptionHandler.process(e);
        }

        LibrariesIndexManager.getInstance().loadResource();

        // fix for TDI-25474 ,some libraries are removed form tos,only list jars exist
        EMap<String, String> jarsToRelative = LibrariesIndexManager.getInstance().getIndex().getJarsToRelativePath();
        Map<String, File> componentsFolders = null;
        Set<String> contributeIdSet = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IComponentsService.class)) {
            IComponentsService service = (IComponentsService) GlobalServiceRegister.getDefault().getService(
                    IComponentsService.class);
            componentsFolders = service.getComponentsFactory().getComponentsProvidersFolder();
            contributeIdSet = componentsFolders.keySet();
        }

        for (String jarName : jarsToRelative.keySet()) {
            String relativePath = jarsToRelative.get(jarName);
            boolean jarFound = false;
            if (relativePath != null) {
                if (relativePath.startsWith("platform:/")) {
                    jarFound = checkJarInstalledFromPlatform(relativePath);
                } else {
                    if (componentsFolders != null && contributeIdSet != null) {
                        for (String contributor : contributeIdSet) {
                            if (relativePath.contains(contributor)) {
                                // caculate the the absolute path of the jar
                                String bundleLocation = componentsFolders.get(contributor).getAbsolutePath();
                                int index = bundleLocation.indexOf(contributor);
                                String jarLocation = new Path(bundleLocation.substring(0, index)).append(relativePath)
                                        .toPortableString();

                                File file = new File(jarLocation);
                                if (file.exists()) {
                                    jarFound = true;
                                }
                                break;
                            }
                        }
                    }
                }
            }
            if (jarFound) {
                if (!withComponent) {
                    if (ModulesNeededProvider.getModulesNeededNames().contains(jarName)) {
                        names.add(jarName);
                    }
                } else {
                    names.add(jarName);
                }
            }
        }
        return names;

    }

    @Override
    public Set<String> listAllDllFiles() {
        Set<String> names = new HashSet<String>();
        try {
            List<File> dllFiles = FilesUtils.getDllFilesFromFolder(getStorageDirectory(), null);
            if (dllFiles.size() > 0) {
                for (File file : dllFiles) {
                    names.add(file.getName());
                }
            } else {

            }
        } catch (MalformedURLException e) {
            CommonExceptionHandler.process(e);
        }

        return names;
    }

    private File getStorageDirectory() {
        String librariesPath = LibrariesManagerUtils.getLibrariesPath(ECodeLanguage.JAVA);
        File storageDir = new File(librariesPath);
        return storageDir;
    }

    @Override
    public void clearCache() {
        if (isInitialized()) {
            LibrariesIndexManager.getInstance().loadResource();
            LibrariesIndexManager.getInstance().getIndex().setInitialized(false);
            LibrariesIndexManager.getInstance().getIndex().getJarsToRelativePath().clear();
            LibrariesIndexManager.getInstance().saveResource();
        }
    }

    @Override
    public boolean contains(String jarName) {
        if (jarList.isEmpty() || listToUpdate) {
            jarList = list(new NullProgressMonitor());
            listToUpdate = false;
        }
        return jarList.contains(jarName);
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.core.ILibraryManagerService#delete(java.lang.String)
     */
    @Override
    public boolean delete(String jarName) {
        // only delete jar from lib/java, do not delete jars from original components providers.

        try {
            List<File> jarFiles = FilesUtils.getJarFilesFromFolder(getStorageDirectory(), null);
            if (jarFiles.size() > 0) {
                for (File file : jarFiles) {
                    if (file.getName().equals(jarName)) {
                        file.delete();
                        jarList.remove(jarName);
                        return true;
                    }
                }
            }
        } catch (MalformedURLException e) {
            CommonExceptionHandler.process(e);
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerService#deploy(java.util.Set, org.eclipse.core.runtime.IProgressMonitor[])
     */
    @Override
    public void deploy(Set<IComponent> componentList, IProgressMonitor... monitorWrap) {
        List<ModuleNeeded> modules = new ArrayList<ModuleNeeded>();

        for (IComponent component : componentList) {
            modules.addAll(component.getModulesNeeded());
        }
        deploy(modules, monitorWrap);
    }

    @Override
    public void deploy(List<ModuleNeeded> modules, IProgressMonitor... monitorWrap) {
        File indexFile = new File(LibrariesIndexManager.getInstance().getIndexFilePath());
        if (indexFile.exists()) {
            LibrariesIndexManager.getInstance().loadResource();
        }
        boolean modified = false;
        LibrariesIndex index = LibrariesIndexManager.getInstance().getIndex();
        EMap<String, String> jarsToRelativePath = index.getJarsToRelativePath();
        for (ModuleNeeded module : modules) {
            String moduleLocaion = module.getModuleLocaion();
            if (moduleLocaion != null && !"".equals(moduleLocaion)) {
                if (!jarsToRelativePath.keySet().contains(module.getModuleName())) {
                    jarsToRelativePath.put(module.getModuleName(), moduleLocaion);
                    modified = true;
                } else {
                    boolean jarFound = false;
                    String existePath = jarsToRelativePath.get(module.getModuleName());
                    if (existePath != null && existePath.startsWith("platform:/")) {
                        jarFound = checkJarInstalledFromPlatform(existePath);
                    }
                    // in case the index.xml already stored some locations not exist in product , replace it with a new
                    // location
                    if (!jarFound) {
                        jarsToRelativePath.put(module.getModuleName(), moduleLocaion);
                        modified = true;
                    }
                }
            }
        }
        if (modified) {
            LibrariesIndexManager.getInstance().saveResource();
        }
    }

    @Override
    public String getJarPath(String jarName) {
        String libPath = null;
        List<File> jarFiles = null;
        try {
            jarFiles = FilesUtils.getJarFilesFromFolder(getStorageDirectory(), jarName);
            if (jarFiles.size() > 0) {
                File file = jarFiles.get(0);
                libPath = file.getAbsolutePath();
            } else {
                EMap<String, String> jarsToRelative = LibrariesIndexManager.getInstance().getIndex().getJarsToRelativePath();
                String relativePath = jarsToRelative.get(jarName);
                if (relativePath != null && relativePath.startsWith("platform:/")) { //$NON-NLS-1$
                    boolean jarFound = checkJarInstalledFromPlatform(relativePath);
                    if (jarFound) {
                        libPath = uriJarInstalled.get(relativePath);
                    }
                }
            }

        } catch (MalformedURLException e) {
            CommonExceptionHandler.process(e);
        }
        return libPath;
    }

    private boolean checkJarInstalledFromPlatform(String uriPath) {
        if (uriJarInstalled.containsKey(uriPath)) {
            return uriJarInstalled.get(uriPath) != null;
        }
        boolean jarFound = false;
        String absolutePath = null;
        try {
            URI uri = new URI(uriPath);
            URL url = FileLocator.toFileURL(uri.toURL());
            File file = new File(url.getFile());
            if (file.exists()) {
                jarFound = true;
                absolutePath = file.getAbsolutePath();
            }
        } catch (Exception e) {
            // do nothing
        }
        uriJarInstalled.put(uriPath, absolutePath);
        return jarFound;
    }

}
