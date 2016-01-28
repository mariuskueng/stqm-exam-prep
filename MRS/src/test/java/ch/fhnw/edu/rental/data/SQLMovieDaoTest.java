package ch.fhnw.edu.rental.data;

import junit.framework.TestCase;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by mariuskueng on 28.01.16.
 */
public class SQLMovieDaoTest extends TestCase {

    private IDatabaseTester tester;
    private Connection connection;

    /** SQL command to create table MOVIES. */
    private static final String CREATE_MOVIES = "drop table if exists MOVIES; create table MOVIES ("
            + "  Id INTEGER IDENTITY," + "  Title VARCHAR(255)," + "  IsRented BOOLEAN," + "  ReleaseDate DATE,"
            + "  PriceCategory INTEGER" + ")";

    private static final String COUNT_SQL = "SELECT COUNT(*) FROM movies";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // load driver
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (Exception e) {
            System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            return;
        }

        // get connection to db
        Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:mrs", "sa", "");

        // create tables in db
        try {
            Statement st = null;
            st = conn.createStatement(); // statements
            int i = st.executeUpdate(CREATE_MOVIES); // run the query
            if (i == -1) {
                System.out.println("db error : " + CREATE_MOVIES);
            }
            st.close();
        } catch (SQLException ex2) {
            ex2.printStackTrace();
        } finally {
            conn.close();
        }

    }

    @Before
    public void setUp() throws Exception {
        tester = new JdbcDatabaseTester("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:mrs", "sa", "");
        connection = tester.getConnection().getConnection();
        InputStream stream = this.getClass().getResourceAsStream("MovieDaoTestData.xml");

        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new InputSource(stream));
        tester.setDataSet(dataSet);
        tester.onSetup();
    }

    @After
    public void tearDown() throws Exception {
        tester.onTearDown();
    }

    public void testDelete() throws Exception {

    }

    public void testGetAll() throws Exception {

    }

    public void testGetById() throws Exception {

    }

    public void testGetByTitle() throws Exception {

    }

    public void testSaveOrUpdate() throws Exception {

    }
}