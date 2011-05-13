package com.talend.tac.cases.executionTask;

import java.awt.Event;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.talend.tac.base.Base;
import com.talend.tac.cases.Login;

public class TestAddTaskBaseBranchProject  extends Login {
	public boolean deleteTask(String taskLabel) throws InterruptedException{
		boolean deleteOk = true;
		selenium.refresh();
		this.waitForElementPresent("//span[text()='" + taskLabel + "']",
				WAIT_TIME);
		selenium.mouseDown("//span[text()='" + taskLabel + "']");
		Thread.sleep(3000);
		selenium.chooseOkOnNextConfirmation();
		selenium.click("idSubModuleDeleteButton");
		selenium.getConfirmation();
		Thread.sleep(3000);
		if(selenium.isElementPresent("//span[text()='" + taskLabel + "']")){
			deleteOk = false;
		}
		return deleteOk;
	}
	public int getTotalExecutionTimes(){
		int total = 0;
		selenium.click("//span//span[text()='Logs']");
		selenium.setSpeed(MAX_SPEED);
		selenium.click("//img[@src='gxt/themes/access/images/grid/refresh.gif']");
		String strContains = selenium.getText("//div[@class='my-paging-display x-component ']");
		String to = (strContains.split("of")[1].trim());
		selenium.setSpeed(MIN_SPEED);
		System.out.println(to);
		return total=Integer.parseInt(to);
		
	}
	public void inputCheckBoxUncheck(String xpathinputBox){
		if(selenium.getValue(xpathinputBox).equals("on")){
			selenium.click(xpathinputBox);
		}
	}
	public String getLogsValue() {
		String logs = null;
		selenium.click("//span//span[text()='Logs']");
		selenium.setSpeed(MID_SPEED);
		selenium.click("//div[@class=' x-grid3-hd-inner x-grid3-hd-startDate x-component sort-desc ']//a[@class='x-grid3-hd-btn']");
		selenium.setSpeed(MIN_SPEED);
		selenium.click("//a[@class=' x-menu-item x-component' and text()='Sort Descending']");
		this.waitForElementPresent(
				"//div[@class='x-grid3-cell-inner x-grid3-col-startDate']",
				WAIT_TIME);
//		String time = selenium
//				.getText("//div[@class='x-grid3-cell-inner x-grid3-col-startDate']");
		selenium.mouseDown("//div[@class='x-grid3-cell-inner x-grid3-col-startDate']");
		selenium.setSpeed(MID_SPEED);
		logs = selenium
				.getValue("//table[@class=' x-btn x-form-file-btn x-component x-btn-text-icon']/ancestor::div[@class='x-form-item ']//textarea");
		selenium.setSpeed(MIN_SPEED);
		System.out.println(logs);
		return logs;
	}
	public boolean runtask(String tasklabel) throws InterruptedException {
		selenium.refresh();
		this.waitForElementPresent("//span[text()='" + tasklabel + "']",
				WAIT_TIME);
		selenium.mouseDown("//span[text()='" + tasklabel + "']");
		Thread.sleep(3000);
		selenium.click("//button[@id='idJobConductorTaskRunButton()'  and @class='x-btn-text ' and text()='Run']");
//		Date start = new Date();
		boolean success = (waitForCondition("//label[text()='Ok']", Base.WAIT_TIME));
		// close the pop window
		selenium.click("//div[@class=' x-nodrag x-tool-close x-tool x-component']");
		// System.out.println(checkContextValue(start));
        return success;
	}
	
	public void runTask(String tasklabel,int times) throws InterruptedException{
		for (int i = 0; i < times; i++) {
			selenium.refresh();
			this.waitForElementPresent("//span[text()='" + tasklabel + "']",
					WAIT_TIME);
			selenium.mouseDown("//span[text()='" + tasklabel + "']");
			Thread.sleep(3000);
			selenium.click("//button[@id='idJobConductorTaskRunButton()'  and @class='x-btn-text ' and text()='Run']");
			// Date start = new Date();
			Assert.assertTrue(
					waitForCondition("//label[text()='Ok']", Base.WAIT_TIME),
					"task run failed!");
			// close the pop window
			selenium.click("//div[@class=' x-nodrag x-tool-close x-tool x-component']");
		}
		}
	public void addSimpleTrigger(String tasklabel,String timeInterval){
		selenium.refresh();
		this.waitForElementPresent("//span[text()='"+tasklabel+"']", WAIT_TIME);
		selenium.mouseDown("//span[text()='"+tasklabel+"']");
		selenium.click("//button[text()='Add trigger...']");//add a trigger
		selenium.click("//a[text()='Add simple trigger']");//add a SimpleTrigger
        Assert.assertTrue(selenium.isElementPresent("//span[text()='"+rb.getString("trigger.action.addSimpleTrigger")+"']"));
        
        this.typeString("//span[text()='Add simple trigger']/parent::legend/parent::fieldset" +
        		"//input[@name='label']", "deactiveTaskSimpletrigger");//label
		
        this.typeString("//span[text()='Add simple trigger']/parent::legend/parent::fieldset" +
				"//input[@name='description']", "testdescription");//description
        this.typeString("//span[text()='Add simple trigger']/parent::legend/parent::fieldset" +
        		"//input[@name='repeatInterval']", timeInterval);//Time interval (s)
	
        selenium.setSpeed(MID_SPEED);
        //click save button to save trigger
	    selenium.click("//span[text()='Add simple trigger']/parent::legend/parent::fieldset/parent::form/" +
		"parent::div/parent::div/parent::div/parent::div/parent::div//button[@id='idFormSaveButton']");
	    Assert.assertTrue(selenium.isElementPresent("//span[text()='deactiveTaskSimpletrigger']"));
		selenium.setSpeed(MIN_SPEED);
	
	}
	public void addTask(String label, String projectName, String branchName,
			 String jobName, String version, String context, String serverName, String statisticName) {
		branchName="branches/"+branchName;
		this.clickWaitForElementPresent("!!!menu.executionTasks.element!!!");
        selenium.setSpeed(MID_SPEED);
	    Assert.assertTrue(selenium.isElementPresent("//div[text()='"+rb.getString("menu.jobConductor")+"']"));
	    selenium.setSpeed(MIN_SPEED);
	    selenium.click("idSubModuleAddButton");
		this.typeString("//input[@name='label']", label);//plan name /Label
		this.typeString("//span[text()='Execution task']/parent::legend/" +
				"parent::fieldset/div/div[2]//input", "branchJobTask");//plan name /Label
		selenium.click("//label[text()='Project:']/parent::div/div/div/div");//select a project 
		this.waitForElementPresent("//div[text()='"+projectName+"' and @role='listitem']", WAIT_TIME);
		selenium.mouseDownAt("//div[text()='"+projectName+"' and @role='listitem']",""+Event.ENTER); 
		selenium.click("//label[text()='Branch:']/parent::div/div/div/div");//select a project 
		this.waitForElementPresent("//div[text()='"+branchName+"' and @role='listitem']", WAIT_TIME);
		selenium.mouseDownAt("//div[text()='"+branchName+"' and @role='listitem']",""+Event.ENTER); 
		selenium.click("//label[text()='Job:']/parent::div/div/div/div");//job
		this.waitForElementPresent("//div[text()='"+jobName+"' and @role='listitem']", WAIT_TIME);
		selenium.mouseDownAt("//div[text()='"+jobName+"' and @role='listitem']",""+Event.ENTER);
		selenium.click("//label[text()='Version:']/parent::div/div/div/div");//version
		this.waitForElementPresent("//div[text()='"+version+"' and @role='listitem']", WAIT_TIME);
		selenium.mouseDownAt("//div[text()='"+version+"' and @role='listitem']",""+Event.ENTER);
		selenium.click("//label[text()='Context:']/parent::div/div/div/div");//context
		this.waitForElementPresent("//div[text()='"+context+"' and @role='listitem']", WAIT_TIME);
		selenium.mouseDownAt("//div[text()='"+context+"' and @role='listitem']",""+Event.ENTER);
		selenium.click("//label[text()='Execution server:']/parent::div/div/div/div");//server
		this.waitForElementPresent("//div[text()='"+serverName+"' and @role='listitem']", WAIT_TIME);
		selenium.mouseDownAt("//div[text()='"+serverName+"' and @role='listitem']",""+Event.ENTER);
		selenium.click("//label[text()='Statistic:']/parent::div/div/div/div");//statistic
		this.waitForElementPresent("//div[text()='"+statisticName+"' and @role='listitem']", WAIT_TIME);
		selenium.mouseDownAt("//div[text()='"+statisticName+"' and @role='listitem']",""+Event.ENTER);
				
	}

	
	@Test(dependsOnGroups={"AddTask"})
	@Parameters({"AddcommonProjectname","ProjectBranch","jobNameBranchJob","version0.1",
		"context","ServerForUseAvailable","statisticEnabled"})
	public void testDeactiveTaskWithSimpleTrigger(String projectName, String branchName,
			 String jobName, String version, String context, String serverName, String statisticName) throws InterruptedException{
		 String label = "taskwithsimpletrigger";
		 int executiontime = 30000;
		 if(!selenium.isElementPresent("//span[text()='"+label+"']")){
			 addTask(label, projectName,branchName,jobName,version,context,serverName,statisticName);
		 }
		 if(!selenium.isElementPresent("//span[text()='"+label+"']")) {
			
			selenium.click("idFormSaveButton");
	        selenium.setSpeed(MID_SPEED);
			Assert.assertTrue(selenium.isElementPresent("//span[text()='"+label+"']"));
			selenium.setSpeed(MIN_SPEED);
			
		}
		 
		 runTask(label,1);
        //add a simple trigger for the task
        addSimpleTrigger(label,"5");
        //wait for 160 seconds so that the task run several times
        Thread.sleep(executiontime);
        //see task logs ,count the execution times
        selenium.click("//span//span[text()='Logs']");
		selenium.setSpeed(MID_SPEED);
//		selenium.click("");
		Assert.assertTrue(selenium.isElementPresent("//div[@class='x-grid3-cell-inner x-grid3-col-startDate']"), "task run failed!");
		Assert.assertTrue(getTotalExecutionTimes() > 0, "task run failed!");
		selenium.setSpeed(MIN_SPEED);
		selenium.refresh();
		this.waitForElementPresent("//span[text()='"+label+"']", WAIT_TIME);
		selenium.setSpeed(MID_SPEED);
		selenium.mouseDown("//span[text()='"+label+"']");
		this.waitForElementPresent("//span[text()='Execution task']/ancestor::fieldset//label[text()='Active:']/ancestor::div[@class='x-form-item ']//input", WAIT_TIME);
		inputCheckBoxUncheck("//span[text()='Execution task']/ancestor::fieldset//label[text()='Active:']/ancestor::div[@class='x-form-item ']//input");
//		selenium.setSpeed(MID_SPEED);
		selenium.click("idFormSaveButton");
		Thread.sleep(3000);
		selenium.setSpeed(MIN_SPEED);
		Thread.sleep(3000);
		selenium.refresh();
		this.waitForElementPresent("//span[text()='"+label+"']", WAIT_TIME);
		selenium.setSpeed(MID_SPEED);
		selenium.mouseDown("//span[text()='"+label+"']");
		int totalafterdeactive = getTotalExecutionTimes();
		selenium.setSpeed(MIN_SPEED);
		Thread.sleep(executiontime);
		selenium.refresh();
		this.waitForElementPresent("//span[text()='"+label+"']", WAIT_TIME);
		selenium.setSpeed(MID_SPEED);
		selenium.mouseDown("//span[text()='"+label+"']");
		Assert.assertFalse( getTotalExecutionTimes()> totalafterdeactive, "task deactive failed!");
		selenium.setSpeed(MIN_SPEED);
		deleteTask(label);
	}
	@Test(dependsOnMethods={"testDeactiveTaskWithSimpleTrigger"})
	@Parameters({"TaskBaseBranch","AddcommonProjectname","ProjectBranch","jobNameBranchJob","version0.1",
		"context","ServerForUseAvailable","statisticEnabled"})
	public void testAddTaskBaseBranch(String label, String projectName, String branchName,
			 String jobName, String version, String context, String serverName, String statisticName){
		addTask(label,projectName, branchName, jobName, version, context, serverName, statisticName);
		if(!selenium.isElementPresent("//span[text()='"+label+"']")) {
			
			selenium.click("idFormSaveButton");
	        selenium.setSpeed(MID_SPEED);
			Assert.assertTrue(selenium.isElementPresent("//span[text()='"+label+"']"));
			selenium.setSpeed(MIN_SPEED);
			
		}
	}
	@Test(dependsOnMethods={"testAddTaskBaseBranch"})
	@Parameters({"TaskBaseBranch"})
	public void testRunTaskBaseBranch(String tasklabel) throws InterruptedException{
//		this.clickWaitForElementPresent("!!!menu.executionTasks.element!!!");
//		Assert.assertTrue(selenium.isElementPresent("//div[text()='"
//				+ rb.getString("menu.jobConductor") + "']"));
//		// select a exist task
//		selenium.mouseDown("//span[text()='"+tasklabel+"']");
		boolean ok= runtask(tasklabel);
		if(ok){
		Assert.assertTrue(getLogsValue().contains("hello branch!!!"),
				"default context test failed");
		}
		else{
			Assert.assertTrue(getLogsValue().contains("Exception"),
			"exception");
			Assert.fail("task branch run failed !");
		}
		
	}
	
	@Test(dependsOnMethods={"testRunTaskBaseBranch"})
	@Parameters({"labelStatisticViewTask"})
	public void testTaskStatisticViewDisable(String tasklabel) throws InterruptedException{
		this.clickWaitForElementPresent("!!!menu.executionTasks.element!!!");
		Assert.assertTrue(selenium.isElementPresent("//div[text()='"
				+ rb.getString("menu.jobConductor") + "']"));
		// select a exist task
		selenium.mouseDown("//span[text()='"+tasklabel+"']");
		this.clickWaitForElementPresent("//span[text()='Edition']");
		this.selectDropDownList("//input[@id='idJobConductorTaskStatisticsListBox()']", 2);
		selenium.setSpeed(MID_SPEED);
		selenium.click("idFormSaveButton");
		selenium.setSpeed(MIN_SPEED);
		selenium.refresh();
		this.waitForElementPresent("//span[text()='" + tasklabel + "']",
				WAIT_TIME);
		selenium.mouseDown("//span[text()='" + tasklabel + "']");
		Thread.sleep(3000);
		selenium.click("//button[@id='idJobConductorTaskRunButton()'  and @class='x-btn-text ' and text()='Run']");
		if(waitForCondition("//span[@class='x-window-header-text' and text()='Real time statistics']", 15)){
			Assert.fail("test statistic view disable failed!");
		}
		//undo disable select
		this.selectDropDownList("//input[@id='idJobConductorTaskStatisticsListBox()']", 1);
		selenium.setSpeed(MID_SPEED);
		selenium.click("idFormSaveButton");
		selenium.setSpeed(MIN_SPEED);
	}
	@Test(dependsOnMethods={"testTaskStatisticViewDisable"})
	@Parameters({"labelStatisticViewTask"})
	public void testTaskStatisticViewEnable(String tasklabel) throws InterruptedException{
		this.clickWaitForElementPresent("!!!menu.executionTasks.element!!!");
		Assert.assertTrue(selenium.isElementPresent("//div[text()='"
				+ rb.getString("menu.jobConductor") + "']"));
		// select a exist task
		selenium.mouseDown("//span[text()='"+tasklabel+"']");
		this.clickWaitForElementPresent("//span[text()='Edition']");
		this.selectDropDownList("//input[@id='idJobConductorTaskStatisticsListBox()']", 1);
		selenium.setSpeed(MID_SPEED);
		selenium.click("idFormSaveButton");
		selenium.setSpeed(MIN_SPEED);
		selenium.refresh();
		this.waitForElementPresent("//span[text()='" + tasklabel + "']",
				WAIT_TIME);
		selenium.mouseDown("//span[text()='" + tasklabel + "']");
		Thread.sleep(3000);
		selenium.click("//button[@id='idJobConductorTaskRunButton()'  and @class='x-btn-text ' and text()='Run']");
		Assert.assertTrue((waitForCondition("//span[@class='x-window-header-text' and text()='Real time statistics']", Base.WAIT_TIME)),
			"test statistic view disable failed!");
		
	}
	
	
	@Test(dependsOnMethods={"testTaskStatisticViewEnable"})
	@Parameters({"AddcommonProjectname","ProjectBranch","jobNameBranchJob","version0.1",
		"context","ServerForUseAvailable","statisticEnabled"})
	public void testDeleteTaskExecutionLogs(String projectName,
			String branchName, String jobName, String version, String context,
			String serverName, String statisticName) throws InterruptedException {
		String label = "taskDeleteLogs";
		addTask(label, projectName, branchName, jobName, version, context,
				serverName, statisticName);
		if (!selenium.isElementPresent("//span[text()='" + label + "']")) {
			selenium.click("idFormSaveButton");
			selenium.setSpeed(MID_SPEED);
			Assert.assertTrue(selenium.isElementPresent("//span[text()='"
					+ label + "']"));
			selenium.setSpeed(MIN_SPEED);
		}
		runTask(label,5);
		selenium.click("//span//span[text()='Logs']");
		Thread.sleep(3000);
		Assert.assertTrue((selenium.getXpathCount("//div[@class='x-grid3-cell-inner x-grid3-col-startDate']")).intValue()==5,"task run generate logs failed !");
		selenium.chooseOkOnNextConfirmation();
		selenium.click("idJobConductorJobLogClearLogButton()");
		selenium.getConfirmation();
		Thread.sleep(3000);
		Assert.assertTrue((selenium.getXpathCount("//div[@class='x-grid3-cell-inner x-grid3-col-startDate']")).intValue()==0,"task run generate logs failed !");
		if(deleteTask(label)){
			addTask(label, projectName, branchName, jobName, version, context,
					serverName, statisticName);
			if (!selenium.isElementPresent("//span[text()='" + label + "']")) {
				selenium.click("idFormSaveButton");
				selenium.setSpeed(MID_SPEED);
				Assert.assertTrue(selenium.isElementPresent("//span[text()='"
						+ label + "']"));
				selenium.setSpeed(MIN_SPEED);
			}
			selenium.setSpeed(MID_SPEED);
			selenium.mouseDown("//span[text()='"
						+ label + "']");
			selenium.click("//span//span[text()='Logs']");
			Thread.sleep(3000);
			Assert.assertTrue((selenium.getXpathCount("//div[@class='x-grid3-cell-inner x-grid3-col-startDate']")).intValue()==0,"delete  logs through delete task failed !");
			selenium.setSpeed(MIN_SPEED);
		}
	}
	
	public boolean waitForCondition(String locator,int seconds) throws InterruptedException{
		boolean conditionPresent = true;
		for (int second = 0;; second++) {
			if (second >= seconds){
				conditionPresent = false;
				break;
			}
			
				if (selenium.isElementPresent(locator)){
					break;
				}
				else{
				Thread.sleep(1000);
			    } 
		}
		
		return conditionPresent;
	}

}