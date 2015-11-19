package com.taptech.graylog.graylog.automation;

import org.junit.Before;
import com.taptech.graylog.graylog.GrayLogConnector;
import org.mule.tools.devkit.ctf.mockup.ConnectorDispatcher;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;

public abstract class AbstractTestCase {
	
	private GrayLogConnector connector;
	private ConnectorDispatcher<GrayLogConnector> dispatcher;
	
	
	protected GrayLogConnector getConnector() {
		return connector;
	}


	protected ConnectorDispatcher<GrayLogConnector> getDispatcher() {
		return dispatcher;
	}

	@Before
	public void init() throws Exception {
		
		//Initialization for single-test run
        ConnectorTestContext.initialize(GrayLogConnector.class, false);
		
		//Context instance
		ConnectorTestContext<GrayLogConnector> context = ConnectorTestContext.getInstance(GrayLogConnector.class);
		
		//Connector dispatcher
		dispatcher = context.getConnectorDispatcher();
		
		connector = dispatcher.createMockup();
		
	}

}
