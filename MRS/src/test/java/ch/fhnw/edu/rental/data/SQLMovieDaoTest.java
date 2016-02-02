package ch.fhnw.edu.rental.data;

import static org.dbunit.Assertion.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import ch.fhnw.edu.rental.model.Movie;
import ch.fhnw.edu.rental.model.NewReleasePriceCategory;
import ch.fhnw.edu.rental.model.PriceCategory;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.dbunit.DatabaseUnitException;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.InputSource;

/**
 * Created by mariuskueng on 28.01.16.
 */
public class SQLMovieDaoTest {

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

    @Test(expected = RuntimeException.class)
    public void testDelete() throws Exception {
        MovieDAO dao = new SQLMovieDAO(connection);
        List<Movie> movielist = dao.getByTitle("The Room");
        Movie theRoom = movielist.get(1);

        // delete non-existing movie
        dao.delete(new Movie("The Hateful Eight", NewReleasePriceCategory.getInstance()));
        assertEquals(1, dao.getByTitle("The Room").size());

        // delete the room
        dao.delete(theRoom);
        assertEquals(0, dao.getByTitle("The Room").size());

        // provoke SQLException
        connection.close();
        dao.getByTitle("The Room");
    }

    @Test(expected = RuntimeException.class)
    public void testGetAll() throws Exception {
        MovieDAO dao = new SQLMovieDAO(connection);
        List<Movie> allMovies = dao.getAll();

        assertEquals(3, allMovies.size());
    }

    @Test(expected = RuntimeException.class)
    public void testGetById() throws Exception {
        MovieDAO dao = new SQLMovieDAO(connection);
        List<Movie> movielist = dao.getByTitle("The Room");
        assertEquals(1000, movielist.get(1).getId());

        // provoke SQLException
        connection.close();
        dao.getByTitle("The Room");
    }

    @Test(expected = RuntimeException.class)
    public void testGetByTitle() throws Exception {
        MovieDAO dao = new SQLMovieDAO(connection);
        List<Movie> movielist = dao.getByTitle("The Room");
        assertEquals("The Room", movielist.get(1).getTitle());

        // provoke SQLException
        connection.close();
        dao.getByTitle("The Room");
    }

    @Test(expected = RuntimeException.class)
    public void testSaveOrUpdate() throws Exception {
        MovieDAO dao = new SQLMovieDAO(connection);
        List<Movie> movielist = dao.getByTitle("The Room");
        Movie theRoom = movielist.get(1);
        assertEquals("The Room", theRoom.getTitle());

        theRoom.setTitle("The Room 2");
        dao.saveOrUpdate(theRoom);

        assertEquals("The Room 2", dao.getByTitle("The Room").get(1).getTitle());

        // provoke SQLException
        connection.close();
        dao.getByTitle("The Room");
    }
}