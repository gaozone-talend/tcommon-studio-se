package org.talend.repository.viewer.actions.example;

import org.eclipse.jface.action.IMenuManager;
import org.talend.repository.view.di.viewer.action.DIRepositoryNodeActionProvider;

public class ActionProviderThatRemovesCreateFolderContextualMenu extends DIRepositoryNodeActionProvider {

    public ActionProviderThatRemovesCreateFolderContextualMenu() {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.view.di.viewer.action.DIRepositoryNodeActionProvider#fillContextMenu(org.eclipse.jface.
     * action.IMenuManager)
     */
    @Override
    public void fillContextMenu(IMenuManager manager) {
        super.fillContextMenu(manager);
        manager.remove("org.talend.repository.actions.createfolder");
    }

}
