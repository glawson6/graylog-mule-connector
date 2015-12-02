package com.taptech.graylog;

import com.google.common.cache.*;
import org.graylog2.gelfclient.GelfConfiguration;
import org.graylog2.gelfclient.GelfTransports;
import org.graylog2.gelfclient.transport.GelfTransport;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.*;
import org.mule.api.annotations.components.ConnectionManagement;
import org.mule.api.annotations.display.Password;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.annotations.param.Default;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by greglawson on 11/18/15.
 */
@ConnectionManagement(configElementName = "graylog", friendlyName = "Connection Managament type strategy")
public class ConnectorConnectionStrategy {
    private final static Logger logger = LoggerFactory.getLogger(ConnectorConnectionStrategy.class);

    private String user;
    private String pass;
    private Boolean connected = false;
    private GelfConfiguration gelfConfiguration;
    private GelfTransport gelfTransport;

    private static LoadingCache<String, String> sessionIDCache;
    @Connect
    @TestConnectivity
    public void connect(@ConnectionKey final String username, @Password final String password) throws ConnectionException {
        logger.info("Calling connect with username {} ", username);
        logger.debug("Calling connect with password {}", password);
        logger.info("ConnectorConnectionStrategy => {}", this.toString());
        this.user = username;
        this.pass = password;
        connectTransport();
        String sessionID = null;
        /*
        connected = connectClients();
        try {
            if (null == sessionIDCache){
                sessionIDCache = CacheBuilder.newBuilder()
                        .maximumSize(1000)
                        .expireAfterWrite(invalidateEntryTime, TimeUnit.MINUTES)
                        .removalListener(new RemovalListener<Object, Object>() {
                            public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
                                logger.info("Removing {}", removalNotification.toString());
                            }
                        })
                        .build(
                                new CacheLoader<String, String>() {
                                    public String load(String key) throws Exception {
                                        return getSessionID(username,password);
                                    }
                                });
            }
            sessionID = sessionIDCache.get(username);
            if (null == sessionID) {
                sessionIDCache.put(username, sawSessionServiceSoapClient.logon(username, password));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error getting sessionID from Obiee",e);
            throw new ConnectionException(ConnectionExceptionCode.UNKNOWN,"FIND A CODE","Error getting sessionID from Obiee",e);
        }
        */
    }

    private void connectTransport() {
        gelfConfiguration = new GelfConfiguration(new InetSocketAddress(host, port))
                .transport(GelfTransports.TCP)
                .queueSize(512)
                .connectTimeout(5000)
                .reconnectDelay(1000)
                .tcpNoDelay(true)
                .sendBufferSize(32768);

        gelfTransport = GelfTransports.create(gelfConfiguration);
    }

    /**
     * Disconnect
     */
    @Disconnect
    public void disconnect() {
        logger.info("Calling disconnect.......");
        connected = false;
        gelfTransport = null;
    }

    /**
     * Are we connected?
     */
    @ValidateConnection
    public boolean isConnected() {
        logger.info("Calling isConnected.......");
        return null != gelfTransport;
    }

    /**
     * Are we connected?
     */
    @ConnectionIdentifier
    public String connectionId() {
        logger.info("Calling connectionId.......");
        return "001";
    }
    @Configurable
    @Default("localhost")
    private String host;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Configurable
    @Default("12201")
    private Integer port;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * This is the time in minutes to invalidate the cache of sessionIDs.
     */
    @Configurable
    @Default("10")
    private Integer invalidateEntryTime = 10;

    public Integer getInvalidateEntryTime() {
        return invalidateEntryTime;
    }

    public void setInvalidateEntryTime(Integer invalidateEntryTime) {
        this.invalidateEntryTime = invalidateEntryTime;
    }

    public String getPass() {
        return pass;
    }

    public GelfTransport getGelfTransport() {

        return gelfTransport;
    }

    public String getUser() {
        return user;
    }
}

