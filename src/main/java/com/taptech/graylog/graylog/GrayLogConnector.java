package com.taptech.graylog.graylog;

import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

import com.taptech.graylog.graylog.config.ConnectorConfig;

@Connector(name="graylog", friendlyName="GrayLog")
public class GrayLogConnector {

    @Config
    ConnectorConfig config;

    /**
     * Custom processor
     *
     * {@sample.xml ../../../doc/graylog-connector.xml.sample graylog:greet}
     *
     * @param friend Name to be used to generate a greeting message.
     * @return A greeting message
     */
    @Processor
    public String greet(String friend) {
        /*
         * MESSAGE PROCESSOR CODE GOES HERE
         */
        return config.getGreeting() + " " + friend + ". " + config.getReply();
    }


    public ConnectorConfig getConfig() {
        return config;
    }

    public void setConfig(ConnectorConfig config) {
        this.config = config;
    }

}