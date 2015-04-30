package sepm.ss15.e0926076.test;

import org.junit.*;
import sepm.ss15.e0926076.persistence.DBHorseDAO;
import sepm.ss15.e0926076.persistence.DatabaseConnector;
import sepm.ss15.e0926076.persistence.HorseDAO;
import sepm.ss15.e0926076.persistence.PersistenceException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Sebastian on 19.03.2015.
 */
public class JDBCHorseDAOTest extends AbstractHorseDAOTest {

    private Connection con;

    @Before
    public void setup() throws SQLException, PersistenceException {
        con = DatabaseConnector.getConnection();
        con.setAutoCommit(false);

        setHorseDAO(new DBHorseDAO());

        TestDatabaseSetup.setupHorseTestDB(con);
    }

    @After
    public void tearDown() throws SQLException {
        con.rollback();
    }

}
