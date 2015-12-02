package com.taptech.graylog;

import org.bson.Document;
import org.graylog2.gelfclient.GelfMessage;
import org.graylog2.gelfclient.GelfMessageBuilder;
import org.graylog2.gelfclient.GelfMessageLevel;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.ConnectionStrategy;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Default;
import org.mule.api.ConnectionException;
import org.mule.config.i18n.Message;
import org.mule.config.i18n.MessageFactory;

@Connector(name = "graylog", friendlyName = "GrayLog")
public class GrayLogConnector {

    @SuppressWarnings("deprecation")
    @ConnectionStrategy
    ConnectorConnectionStrategy connectionStrategy;

    public ConnectorConnectionStrategy getConnectionStrategy() {
        return connectionStrategy;
    }

    public void setConnectionStrategy(ConnectorConnectionStrategy connectionStrategy) {
        this.connectionStrategy = connectionStrategy;
    }

    boolean blocking = true;

    @Processor(friendlyName = "Send Log to GrayLog")
    public Object sendLog(@Default("#[payload]") Object payload) throws InterruptedException {
        GelfMessageBuilder builder = new GelfMessageBuilder("", "example.com")
                .level(GelfMessageLevel.INFO)
                .additionalField("_foo", "bar");
        String resolvedStr = resolvePayLoadToJSONString(payload);
        final GelfMessage message = builder.message(resolvedStr)
                .additionalField("_count", 1)
                .build();
        getConnectionStrategy().getGelfTransport().send(message);
        return payload;
    }

    public String resolvePayLoadToJSONString(Object payload) {
        Document document = new Document("mulePayload", payload);
        String resolved = document.toJson();
        return resolved;
    }

    @Processor(friendlyName = "Send Log Async to GrayLog")
    public Object sendLogAsync(@Default("#[payload]") Object payload) {
        GelfMessageBuilder builder = new GelfMessageBuilder("", "example.com")
                .level(GelfMessageLevel.INFO)
                .additionalField("_foo", "bar");
        String resolvedStr = resolvePayLoadToJSONString(payload);
        final GelfMessage message = builder.message(resolvedStr)
                .additionalField("_count", 1)
                .build();
        boolean enqueued = getConnectionStrategy().getGelfTransport().trySend(message);
        return payload;
    }


}