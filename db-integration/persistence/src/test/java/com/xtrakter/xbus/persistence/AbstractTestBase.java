package com.xtrakter.xbus.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class AbstractTestBase {

    Logger log = LoggerFactory.getLogger(getClass());

    private static String APP_CONTEXT_DEV[] = {"persistence-test.xml"};

    private static boolean configured = false;

    protected TransactionStatus status = null;

    private ApplicationContext appContext;

    private IDatabaseConnection getConnection() throws Exception {
        DataSource ds = (DataSource) getBean("dataSource");
        IDatabaseConnection connection = new DatabaseConnection(ds.getConnection());
        DatabaseConfig config = connection.getConfig();
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
        return connection;
    }

    protected IDataSet getDataSet() throws Exception {
        return (new FlatXmlDataSetBuilder()).build(new FileInputStream(new File(getBaseDir(), "src/test/dbunit/dataset.xml")));
    }

    protected void dump() throws Exception {
        IDataSet fullDataSet = getConnection().createDataSet();
        FlatXmlDataSet.write(fullDataSet, System.out);
    }

    private void handleSetUpOperation() throws Exception {
        final IDatabaseConnection conn = getConnection();
        final IDataSet data = getDataSet();
        try {
            DatabaseOperation.CLEAN_INSERT.execute(conn, data);

            //Commit the table inserts so other connections can retrieve it
            conn.getConnection().commit();
        } finally {
            conn.close();
        }
    }

    public ApplicationContext getAppContext() {
        return appContext;
    }

    public Object getBean(String beanName) {
        return appContext.getBean(beanName);
    }

    public void createTables() throws Exception {
        log.info("Creating tables");
        Connection conn = null;
        Statement stmt = null;
        try {
            DataSource ds = (DataSource) getBean("dataSource");
            conn = ds.getConnection();

            File file = new File(getBaseDir(), "../db/target/db/hsqldb/db.sql");

            FileInputStream fis = new FileInputStream(file);
            byte[] buf = new byte[(int) file.length()];
            fis.read(buf);

            stmt = conn.createStatement();
            stmt.execute("SET IGNORECASE TRUE");

            String sql = new String(buf);
            stmt = conn.createStatement();
            stmt.execute(sql);
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
            }
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
    }

    @Before
    public void runBefore() throws Exception {

        appContext = (ApplicationContext) new ClassPathXmlApplicationContext(APP_CONTEXT_DEV);

        if (!configured) {
            createTables();
            configured = true;
        }
        // Force a transaction to prevent a LazyInitializationException
        beginTransaction();
        handleSetUpOperation();
    }

    @After
    public void runAfter() throws Exception {
        clearTransaction();
    }

    protected void beginTransaction() {
        JpaTransactionManager jtm = (JpaTransactionManager) appContext.getBean("transactionManager");
        EntityManagerFactoryUtils.getTransactionalEntityManager(jtm.getEntityManagerFactory());
        jtm.getEntityManagerFactory().createEntityManager().getTransaction().begin();
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        status = jtm.getTransaction(def);
    }

    protected void clearTransaction() {
        try {
            if (status != null && !status.isRollbackOnly()) {
                JpaTransactionManager jtm = (JpaTransactionManager) appContext.getBean("transactionManager");
                jtm.commit(status);
            }
        } finally {
            status = null;
        }
    }

    protected final File getBaseDir() {
        File dir;

        String tmp = System.getProperty("basedir");
        if (tmp != null) {
            dir = new File(tmp);
        }
        else {
            String path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();

            dir = new File(path).getParentFile().getParentFile();

            System.setProperty("basedir", dir.getPath());
        }

        return dir;
    }
}
