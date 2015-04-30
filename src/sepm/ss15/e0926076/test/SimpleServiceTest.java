package sepm.ss15.e0926076.test;

import org.junit.After;
import org.junit.Before;
import sepm.ss15.e0926076.persistence.DatabaseConnector;
import sepm.ss15.e0926076.service.ServiceException;
import sepm.ss15.e0926076.service.SimpleService;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Sebastian on 19.03.2015.
 */
public class SimpleServiceTest extends AbstractServiceTest {

    private Connection con;

    @Before
    public void setup() throws ServiceException, SQLException {
        con = DatabaseConnector.getConnection();
        con.setAutoCommit(false);

        setService(new SimpleService());

        TestDatabaseSetup.setupHorseTestDB(con);
        TestDatabaseSetup.setupJockeyTestDB(con);
    }

    @After
    public void tearDown() throws SQLException {
        con.rollback();
    }

}
