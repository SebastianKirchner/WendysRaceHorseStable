package sepm.ss15.e0926076.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import sepm.ss15.e0926076.entities.Jockey;
import sepm.ss15.e0926076.persistence.PersistenceException;
import sepm.ss15.e0926076.persistence.JockeyDAO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sebastian on 20.03.2015.
 */
public abstract class AbstractJockeyDAOTest {

    private static final Logger logger = LogManager.getLogger(AbstractJockeyDAOTest.class);

    protected JockeyDAO jockeyDAO;

    protected void setJockeyDAO(JockeyDAO jockey_dao) {
        this.jockeyDAO = jockey_dao;
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWithNullShouldThrowException() throws IllegalArgumentException, PersistenceException {
        logger.info("-> create with null in");
        jockeyDAO.create(null);
        logger.info("-> create with null out\n ");
    }

    @Test
    public void createWithValidParameter() throws PersistenceException {
        logger.info("-> create with valid in");

        double assert_skill = 66.6;
        int assert_height = 500;

        Jockey j = new Jockey();
        j.setName("Test_Name");
        j.setSex("m");
        j.setHeight(assert_height);
        j.setSkill(assert_skill);

        List<Jockey> jockeys = jockeyDAO.read();
        assertFalse(jockeys.contains(j));
        jockeys = null;

        jockeyDAO.create(j);

        jockeys = jockeyDAO.read();

        for (Jockey jo : jockeys) {
            if (jo.getName().equals("Test_Name")) {
                assertTrue(jo.getHeight() == assert_height && j.getSkill() == assert_skill);
            }
        }

        logger.info("-> create with valid out\n ");
    }

    @Test
    public void readAll() throws PersistenceException {
        logger.info("-> read all in");

        ArrayList<Jockey> jockeys = (ArrayList) jockeyDAO.read();
        assertTrue(jockeys.size() == 10);

        Jockey j2 = null;
        for (Jockey j : jockeys) {
            assertTrue(j != j2);
            j2 = j;
        }

        logger.info("-> read all out\n ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateWithNullParam() throws IllegalArgumentException, PersistenceException {
        logger.info("-> update with null in");

        jockeyDAO.update(null);

        logger.info("-> update with null out\n ");
    }

    @Test(expected = PersistenceException.class)
    public void updateWithInvalidParam() throws PersistenceException {
        logger.info("-> update with invalid in");

        Jockey j = new Jockey();

        // invalid ID
        j.setId(-1);
        j.setName("TEST");
        j.setSkill(44.4);
        j.setHeight(200);
        j.setSex("w");

        jockeyDAO.update(j);

        logger.info("-> update with invalid out\n ");
    }

    @Test
    public void updateWithValidParam() throws PersistenceException {
        logger.info("-> update with valid in");

        ArrayList<Jockey> jockeys = (ArrayList) jockeyDAO.read();
        Jockey j_to_update = jockeys.get(1);


        j_to_update.setName("TEST_TO_SUCCEED");
        j_to_update.setSkill(444.4);

        jockeyDAO.update(j_to_update);
        jockeys = null;

        jockeys = (ArrayList) jockeyDAO.read();

        for (Jockey jo : jockeys) {
            if (jo.getId() == j_to_update.getId()) {
                assertTrue(j_to_update.equals(jo));
                break;
            }
        }

        logger.info("-> update with valid out\n ");
    }

    @Test(expected = PersistenceException.class)
    public void deleteInvalidObject() throws PersistenceException {
        logger.info("-> delete with invalid in");

        Jockey j = new Jockey();
        j.setId(-1); // invalid ID
        jockeyDAO.delete(j);

        logger.info("-> delete with invalid out\n ");
    }

    @Test
    public void deleteValidObject() throws PersistenceException {
        logger.info("-> delete with valid in");

        ArrayList<Jockey> jockeys = (ArrayList) jockeyDAO.read();
        Jockey jockey_to_delete = jockeys.get(1);
        jockeyDAO.delete(jockey_to_delete);

        jockeys = null;
        jockeys = (ArrayList) jockeyDAO.read();

        for (Jockey jo : jockeys) {
            assertFalse(jockey_to_delete.getId() == jo.getId());
        }

        logger.info("-> delete with valid out\n ");
    }

}

