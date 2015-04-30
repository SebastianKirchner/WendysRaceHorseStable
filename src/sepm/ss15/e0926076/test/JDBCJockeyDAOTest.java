package sepm.ss15.e0926076.test;

import org.junit.After;
import org.junit.Before;
import sepm.ss15.e0926076.persistence.DBJockeyDAO;
import sepm.ss15.e0926076.persistence.DatabaseConnector;
import sepm.ss15.e0926076.persistence.JockeyDAO;
import sepm.ss15.e0926076.persistence.PersistenceException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Sebastian on 20.03.2015.
 */
public class JDBCJockeyDAOTest extends AbstractJockeyDAOTest {
    private Connection con;
    private JockeyDAO dao;

    @Before
    public void setup() throws SQLException, PersistenceException {
        con = DatabaseConnector.getConnection();
        con.setAutoCommit(false);

        setJockeyDAO(new DBJockeyDAO());

        TestDatabaseSetup.setupJockeyTestDB(con);
    }

    @After
    public void tearDown() throws SQLException {
        con.rollback();
    }

}
