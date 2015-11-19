package com.taptech.graylog.graylog.automation.testrunners;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import com.taptech.graylog.graylog.automation.testcases.GreetTestCases;
import com.taptech.graylog.graylog.GrayLogConnector;
import org.mule.tools.devkit.ctf.junit.RegressionTests;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;

@RunWith(Categories.class)
@IncludeCategory(RegressionTests.class)

@SuiteClasses({
	GreetTestCases.class
	
})

public class RegressionTestSuite {
	
	@BeforeClass
	public static void initialiseSuite(){
		
		ConnectorTestContext.initialize(GrayLogConnector.class);

	}
	
	@AfterClass
    public static void shutdownSuite() {

        ConnectorTestContext.shutDown();

    }
	
}