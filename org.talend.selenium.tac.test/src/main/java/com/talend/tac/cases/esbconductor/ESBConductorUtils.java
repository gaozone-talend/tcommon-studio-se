package com.talend.tac.cases.esbconductor;

import org.testng.Assert;

import com.talend.tac.cases.Login;

public class ESBConductorUtils extends Login {
    	
	/*go to ESBConductor page*/
	public void intoESBConductorPage() {
		
		this.clickWaitForElementPresent("!!!menu.esbconductor.element!!!");
		this.waitForElementPresent("//div[@class='header-title' and text()" +
				"='ESB Conductor']", WAIT_TIME);
		
	}
	
	/*add method for select feature*/
    public void selectDropDownListForESBConductor(String id, String itemName, String filedName) {
		
		if(selenium.isElementPresent("//span[text()='Select Feature" +
			" from Talend repository']//ancestor::div[@class='" +
			" x-window x-component']//input[@id='"+id+"']"
				+ "/following-sibling::div")) {
			
			selenium.click("//span[text()='Select Feature" +
			" from Talend repository']//ancestor::div[@class='" +
			" x-window x-component']//input[@id='"+id+"']"
					+ "/following-sibling::div");
			
		} else if (selenium.isElementPresent("//label[text()='"+filedName+"']//parent::div" +
						"//input[@id='"+id+"']"
						+ "/following-sibling::div")) {
				
				selenium.click("//label[text()='"+filedName+"']//parent::div" +
						"//input[@id='"+id+"']"
						+ "/following-sibling::div");
				
		} 
		                            
		this.waitForElementPresent("//div[text()='" + itemName + "' and @role='listitem']", WAIT_TIME);
		selenium.mouseDown("//div[text()='" + itemName + "' and @role='listitem']");
		selenium.fireEvent("//input[@id='"+id+"']", "blur");

	}
	
	
	/*add esbconductor*/
	public void addESBConductor(String label, String des, String repository,
			String group, String artifact, String version, String name, String type, 
			String context, String server) {
		
		this.intoESBConductorPage();
		
		this.clickWaitForElementPresent("idESBConductorTaskGridAddButton");
		this.waitForElementPresent("//img[@class='gwt-Image" +
				" x-component ']", WAIT_TIME);
		
		this.typeString("idESBConductorTasklabelInput", label);
		this.typeString("idESBConductorTaskdesInput", des);
		selenium.click("idESBConductorTaskSelectButton");
		this.waitForElementPresent("//span[text()='Select" +
				" Feature from Talend repository']", WAIT_TIME);
		this.selectDropDownListForESBConductor("idTaskProjectListBox", repository, "Repository:");
		this.selectDropDownListForESBConductor("idTaskBranchListBox", group, "Group:");
		this.selectDropDownListForESBConductor("idTaskApplicationListBox", artifact, "Artifact:");
		this.selectDropDownListForESBConductor("idTaskVersionListBox", version, "Version:");
		selenium.click("//span[text()='Select Feature from Talend repository']" +
				"//ancestor::div[@class=' x-window x-component']" +
				"//button[text()='OK']");//save select feature info after click OK 
		this.selectDropDownListForESBConductor("idTaskProjectListBox", name, "Name:");
		this.selectDropDownListForESBConductor("idJobConductorExecutionServerListBox", type, "Type:");
		this.selectDropDownListForESBConductor("idESBConductorTaskContextListBox", context, "Context:");
		this.selectDropDownListForESBConductor("idJobConductorExecutionServerListBox", server, "Server:");
		selenium.click("idFormSaveButton");
		
	}
	
	/*
	 * method to delete 
	 * */
	public void deleteESBConductorOK(String label) {
		
		this.intoESBConductorPage();
		this.waitForElementPresent("//div[text()='"+label+"']", WAIT_TIME);
		selenium.mouseDown("//div[text()='"+label+"']");
		this.sleep(3000);
		selenium.chooseOkOnNextConfirmation();//
		selenium.click("idESBConductorTaskGridDeleteButton");
		Assert.assertTrue(selenium.getConfirmation().matches("^Are you sure you want to remove the selected esb task [\\s\\S]$"));
		this.waitForElementDispear("//div[text()='"+label+"']", WAIT_TIME);
		
	}
	
	/*
	 * method to delete 
	 * */
	public void deleteESBConductorCancel(String label) {
		
		this.intoESBConductorPage();
		this.waitForElementPresent("//div[text()='"+label+"']", WAIT_TIME);
		selenium.mouseDown("//div[text()='"+label+"']");
		this.sleep(3000);
		selenium.chooseCancelOnNextConfirmation();//choose 'Cancel'
		selenium.click("idESBConductorTaskGridDeleteButton");
		Assert.assertTrue(selenium.getConfirmation().matches("^Are you sure you want to remove the selected esb task [\\s\\S]$"));
		this.sleep(3000);
		selenium.refresh();
		this.waitForElementPresent("//div[text()='"+label+"']", WAIT_TIME);
		
	}
	
	/*deploy start a conductor*/
	public void deployStartConductor(String label, String name, String promptInfo,
			String id, String status) {
		
		this.waitForElementPresent("//div[text()='"+label+"']", WAIT_TIME);
		selenium.mouseDown("//div[text()='"+label+"']");
		
		selenium.click(id);//button{deploy start}
		
		this.waitForTextPresent(promptInfo, WAIT_TIME);
		selenium.setSpeed(MID_SPEED);
		selenium.click("idESBConductorTaskGridRefreshButton");
		selenium.setSpeed(MIN_SPEED);
		
		this.waitForElementPresent("//div[text()='"+label+"']" +
		"//ancestor::table[@class='x-grid3-row-table']//span[text()='"+status+"']", WAIT_TIME);
		Assert.assertTrue(selenium.isElementPresent("//div[text()='"+label+"']" +
		"//ancestor::table[@class='x-grid3-row-table']//span[text()='"+status+"']"));
	}
	
	/*undeploy stop conductor*/
    public void undeployStopConductor(String label, String name, String id, String status,
    		String popupInfo, String promptInfo) {
    	
    	this.waitForElementPresent("//div[text()='"+label+"']", WAIT_TIME);
		selenium.mouseDown("//div[text()='"+label+"']");
		
		this.sleep(3000);
		selenium.chooseOkOnNextConfirmation();
		selenium.click(id);//button {undeploy stop}
		Assert.assertTrue(selenium.getConfirmation().matches("^"+popupInfo+" [\\s\\S]$"));
		
		this.waitForTextPresent(promptInfo, WAIT_TIME);
		selenium.setSpeed(MID_SPEED);
		selenium.click("idESBConductorTaskGridRefreshButton");
		selenium.setSpeed(MIN_SPEED);
		
		this.waitForElementPresent("//div[text()='"+label+"']" +
				"//ancestor::table[@class='x-grid3-row-table']//span[text()='"+status+"']", WAIT_TIME);
		Assert.assertTrue(selenium.isElementPresent("//div[text()='"+label+"']" +
				"//ancestor::table[@class='x-grid3-row-table']//span[text()='"+status+"']"));
    	
    }
	
}