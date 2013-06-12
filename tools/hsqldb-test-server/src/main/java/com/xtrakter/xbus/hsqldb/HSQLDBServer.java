package org.opennms.ng.hsqldb;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

import org.hsqldb.Database;
import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerConstants;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class HSQLDBPubSubServer is a testing driver as a stand alone
 * implementation for running HSQLDB. Its use primarily is for testing without
 * needing to use a real external database.
 */
public class HSQLDBServer {

    private static Logger LOG = LoggerFactory.getLogger(HSQLDBServer.class);

    public static String LOG_FILE_NAME = "hsql.log";
    public static String LOG_ERROR_FILE_NAME = "hsql_errors.log";
    public static String DEFAULT_TABLE_TYPE = "memory";

    /**
     * The Bundle bpcontext.
     */
    private BundleContext bpcontext;

    /**
     * The database name.
     */
    private String dbName;

    /**
     * The database location.
     */
    private String dbLocation;

    /**
     * The tableType.
     */
    private String tableType = DEFAULT_TABLE_TYPE;

    /**
     * The user name.
     */
    private String userName;

    /**
     * The password.
     */
    private String password;

    /**
     * The server object.
     */
    private Server server;

    /**
     * The server port.
     */
    private int port;

    /**
     * The server state
     */
    private int serverState;

    /**
     * A flag indicating if the server should be started *
     */
    private boolean startServer = true;

    /**
     * A facility for threads to schedule tasks for to check the server state
     */
    private Timer serverStateTimer;

    /**
     * The maximum number of start retries
     */
    private int maxStartRetry = 5;

    /**
     * The maximum number of start retries
     */
    private int startTriesCount = 0;

    /**
     * Path to the log files *
     */
    private String logFilePath = null;

    public void destroy() throws Exception {

        shutdown();
    }

    public void shutdown() {
        LOG.info(getClass().getName() + ": shutting down hsql db server...");
        if (startServer) {
            try {
                // wait 5 seconds
                Thread.sleep(5000);
                // check server is not running just in case ...
                server.checkRunning(false);
                LOG.info(getClass().getName()
                        + ": hsql db server has been shut down.");
            } catch (Exception e) {
                try {

                    LOG.info(getClass().getName()
                            + ": first attempt to shut down hsql db server.");
                    // first attempt to shutdown
                    org.hsqldb.DatabaseManager
                            .closeDatabases(Database.CLOSEMODE_NORMAL);
                } catch (Exception e0) {
                    // ignore
                } finally {
                    try {
                        // wait 5 seconds
                        Thread.sleep(5000);
                        // check server is not running just in case ...
                        server.checkRunning(false);
                        LOG.info(getClass().getName()
                                + ": hsql db server has been shut down.");
                    } catch (Exception e2) {
                        LOG.info(getClass().getName()
                                + ": second attempt to shut down hsql db server.");

                        Properties info = new Properties();
                        info.setProperty("user", userName);
                        info.setProperty("password", password);

                        String sql = "SHUTDOWN";
                        Statement stmt;
                        try {
                            final Connection conn = DriverManager
                                    .getConnection(
                                            "jdbc:hsqldb:hsql://localhost"
                                                    + (port != 9001 ? ":"
                                                    + port : "") + "/"
                                                    + dbName, info);
                            stmt = conn.createStatement();
                            stmt.executeUpdate(sql);
                            stmt.close();
                        } catch (SQLException sqle) {
                            // sqle.printStackTrace();
                        } finally {
                            try {
                                // wait 5 seconds
                                Thread.sleep(5000);
                                // check server is not running just in case ...
                                server.checkRunning(false);
                                LOG.info(getClass().getName()
                                        + ": hsql db server has been shut down.");
                            } catch (Exception e3) {
                                LOG.info(getClass().getName()
                                        + ": third attempt to shut down hsql db server.");
                                server.stop();
                                server.shutdown();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Spring overload to fire as a startup after all Spring properties have been
     * set
     */
    public void init() throws Exception {

        if (startServer) {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");

            server = new Server();
            server.setSilent(true);

            if (logFilePath != null && !logFilePath.isEmpty()) {

                try {
                    File logFileDir = new File(logFilePath);
                    if (!logFileDir.exists()) {
                        logFileDir.mkdirs();
                    }
                    FileWriter fw;
                    fw = new FileWriter(logFilePath + File.separator + LOG_FILE_NAME);
                    PrintWriter pw = new PrintWriter(fw);
                    server.setLogWriter(pw);
                    LOG.info(getClass().getName() + ": HSQL logging: " + logFilePath + File.separator + LOG_FILE_NAME);
                    FileWriter fw2 = new FileWriter(logFilePath + File.separator + LOG_ERROR_FILE_NAME);
                    PrintWriter pw2 = new PrintWriter(fw2);
                    server.setErrWriter(pw2);
                    LOG.info(getClass().getName() + ": HSQL logging errors: " + logFilePath + File.separator + LOG_ERROR_FILE_NAME);
                } catch (IOException e) {
                    LOG.info(getClass().getName() + ": Provided invalid logFile location: " + logFilePath, e);
                }
            } else {
                LOG.info(getClass().getName() + ": HSQL will not log since 'logFile' was not setup.");
                server.setLogWriter(null);
                server.setErrWriter(null);
            }

            server.setDatabaseName(0, dbName);
            //server.setDatabasePath(0, dbLocation);
            server.setPort(port);

            HsqlProperties hsqlproperties = new HsqlProperties();
            hsqlproperties.setProperty("server.database.0", dbLocation + ";hsqldb.default_table_type=" + tableType);
            server.setProperties(hsqlproperties);

            // server.setNoSystemExit(true);
            // server.setSilent(false);
            LOG.info(getClass().getName() + ": afterPropertiesSet() started db server.");
            serverState = server.start();
            startTriesCount++;

            if (serverStateTimer == null) {
                ServerStateCheckTask task = new ServerStateCheckTask();
                serverStateTimer = new Timer(true);
                serverStateTimer.schedule(task, 1000, 2000);

                Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        serverStateTimer.cancel();
                    }
                });
            }

            waitPort(server.getPort());

            //			final Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost"
            //					+ (port != 9001 ? ":" + port : "") + "/" + dbName, userName, password);

            Properties info = new Properties();
            info.setProperty("user", userName);
            info.setProperty("password", password);

            final Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost" + (port != 9001 ? ":" + port : "") + "/" + dbName,
                    info);

            setupDatabase(conn);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    LOG.info(getClass().getName() + ": ShutdownHook : shutting down hsql db server.");
                    shutdown();
                }
            });
        } else {
            LOG.warn(
                    getClass().getName() + ": HSQL DB Server not started. HSQL DB 'useProvidedHsqlDBServer' flag is: " + startServer);
        }
    }

    /**
     * Sets up the HSQLDB database daemon and loads the schema if it is empty on
     * first execution.
     *
     * @throws Exception the exception
     */
    private void setupDatabase(Connection conn) throws Exception {

        Statement stmt = null;

        try {

            stmt = conn.createStatement();

            // See if we have a schema
            try {
                // This will fail if we have no schema
                stmt.execute("select * from version");
                LOG.info(getClass().getName() + ": setupDatabase() schema already present in db.");
            } catch (SQLException e) {

                LOG.info(
                        getClass().getName() + ": setupDatabase() creating new schema in db." + "from " + bpcontext.getBundle().getResource("/db.sql"));
                // No schema, so lets create it
                URL resource = bpcontext.getBundle().getResource("/db.sql");
                if (resource != null) {
                    BufferedInputStream bis = new BufferedInputStream(bpcontext.getBundle().getResource("/db.sql").openStream());
                    byte[] buf = new byte[1024];
                    StringBuffer sb = new StringBuffer();
                    while (true) {
                        int len = bis.read(buf);
                        if (len <= 0) {
                            break;
                        }
                        sb.append(new String(buf, 0, len));
                    }
                    bis.close();

                    String sql = new String(sb.toString());
                    stmt = conn.createStatement();
                    stmt.execute(sql);
                } else {
                    LOG.warn(": no schema found to load (db.sql)... ignoring schema creation");
                }
            }
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                }
            }
        }
    }

    // ///////////// Inner class to check the state of the db //
    // server./////////////
    private class ServerStateCheckTask extends TimerTask {

        // Retrieves current state of this server in numerically coded form.
        // Typically, this will be one of:
        //
        // SERVER_STATE_ONLINE (1)
        // SERVER_STATE_OPENING (4)
        // SERVER_STATE_CLOSING (8)
        // SERVER_STATE_SHUTDOWN (16)

        public void run() {

            serverState = server.getState();
            if (serverState == ServerConstants.SERVER_STATE_ONLINE) {
                if (serverStateTimer != null) {
                    LOG.info(getClass().getName() + ": hsql db server is in online state now.");
                    // stop checking since it's online
                    serverStateTimer.purge();
                    serverStateTimer.cancel();
                }
            } else {
                if (serverState == ServerConstants.SERVER_STATE_SHUTDOWN || serverState == ServerConstants.SERVER_STATE_CLOSING) {
                    LOG.error(getClass().getName() + ": hsql db server start failed !!!!!.");
                    LOG.error(getClass().getName() + ": hsql db last server error: ", server.getServerError());
                    try {
                        // check server is not running just in case ...
                        server.checkRunning(true);
                    } catch (Exception e) {
                        if (startTriesCount <= maxStartRetry) {
                            startTriesCount++;
                            LOG.warn(getClass().getName() + ": trying to start hsql db server " + startTriesCount + " time.");
                            // try to start the server again
                            server.start();
                        } else {
                            LOG.error(
                                    getClass().getName() + ": Fatal error. Stopped trying to start db server after " + startTriesCount + " trying times");
                            serverStateTimer.cancel();
                        }
                    }
                }
            }
        }
    }

    /**
     * Waits for port to be listening
     *
     * @param port
     * @throws Exception
     */
    void waitPort(int port) throws Exception {

        for (int i = 0; i < 60; i++) {
            Socket s = null;
            try {
                s = new Socket((String) null, port);
                return;
            } catch (IOException e) {
                // wait
            } finally {
                if (s != null) {
                    try {
                        s.close();
                    } catch (IOException e) {
                    }
                }
            }
            Thread.sleep(2000);
        }
        throw new TimeoutException("HSQL Database server start time out.");
    }

    /**
     * Gets the db name.
     *
     * @return the db name
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * Sets the db name.
     *
     * @param dbName the new db name
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * Gets the db location.
     *
     * @return the db location
     */
    public String getDbLocation() {
        return dbLocation;
    }

    /**
     * Sets the db location.
     *
     * @param dbLocation the new db location
     */
    public void setDbLocation(String dbLocation) {
        this.dbLocation = dbLocation;
    }

    /**
     * Gets the user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name.
     *
     * @param userName the new user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the port.
     *
     * @param port the new port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Gets the tableType.
     *
     * @return the tableType
     */
    public String getTableType() {
        return tableType;
    }

    /**
     * Sets the tableType.
     *
     * @param tableType the new tableType
     */
    public void setTableType(String tableType) {
        this.tableType = tableType;
    }


    public int getServerState() {
        return serverState;
    }

    public void setMaxStartRetry(int maxStartRetry) {
        this.maxStartRetry = maxStartRetry;
    }

    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public boolean isStartServer() {
        return startServer;
    }

    public void setStartServer(boolean startServer) {
        this.startServer = startServer;
    }

    public void setBpcontext(BundleContext bpcontext) {
        this.bpcontext = bpcontext;
    }
}