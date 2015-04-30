package sepm.ss15.e0926076.test;

import org.junit.After;
import org.junit.Before;
import sepm.ss15.e0926076.persistence.DBRaceDAO;
import sepm.ss15.e0926076.persistence.DatabaseConnector;
import sepm.ss15.e0926076.persistence.PersistenceException;
import sepm.ss15.e0926076.persistence.RaceDAO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Sebastian on 20.03.2015.
 */
public class JDBCRaceDAOTest extends AbstractRaceDAOTest {

    private Connection con;

    @Before
    public void setup() throws SQLException, PersistenceException {
        con = DatabaseConnector.getConnection();
        con.setAutoCommit(false);

        setRaceDAO(new DBRaceDAO());

        TestDatabaseSetup.setupRaceTestDB(con);
    }

    @After
    public void tearDown() throws SQLException {
        con.rollback();
    }

}
