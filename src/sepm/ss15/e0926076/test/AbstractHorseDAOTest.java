package sepm.ss15.e0926076.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import sepm.ss15.e0926076.entities.Horse;
import sepm.ss15.e0926076.persistence.HorseDAO;
import sepm.ss15.e0926076.persistence.PersistenceException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sebastian on 19.03.2015.
 */
public abstract class AbstractHorseDAOTest {

    private static final Logger logger = LogManager.getLogger(AbstractHorseDAOTest.class);

    protected HorseDAO horseDAO;

    protected void setHorseDAO(HorseDAO horse_dao) {
        this.horseDAO = horse_dao;
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWithNullShouldThrowException() throws IllegalArgumentException, PersistenceException {
        logger.info("-> create with null in");
        horseDAO.create(null);
        logger.info("-> create with null out");
    }

    @Test
    public void createWithValidParameter() throws PersistenceException {
        logger.info("-> create with valid in");

        Horse h = new Horse();
        h.setName("Test_Name");
        h.setMinSpeed(0);
        h.setMaxSpeed(999);
        h.setPicPath("resources/test.jpg");

        List<Horse> horses = horseDAO.read();
        assertFalse(horses.contains(h));
        horses = null;

        horseDAO.create(h);

        horses = horseDAO.read();

        for (Horse ho : horses) {
            if (ho.getName().equals("Test_Name")) {
                assertTrue(ho.getMinSpeed() == 0 && ho.getMaxSpeed() == 999);
            }
        }

        logger.info("-> create with valid out");
    }

    @Test
    public void readAll() throws PersistenceException {
        logger.info("-> read all in");

        ArrayList<Horse> horses = (ArrayList) horseDAO.read();
        assertTrue(horses.size() == 10);

        Horse h2 = null;
        for (Horse h: horses) {
            assertTrue(h != h2);
            h2 = h;
        }

        logger.info("-> read all out");
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateWithNullParam() throws IllegalArgumentException, PersistenceException {
        logger.info("-> update with null in");

        horseDAO.update(null);

        logger.info("-> update with null out");
    }

    @Test(expected = PersistenceException.class)
    public void updateWithInvalidParam() throws PersistenceException {
        logger.info("-> update with invalid in");

        Horse h = new Horse();

        // invalid ID
        h.setId(-1);
        h.setName("TEST_TO_FAIL");
        h.setMinSpeed(50);
        h.setMaxSpeed(100);
        h.setPicPath("resources/test.jpg");

        horseDAO.update(h);

        logger.info("-> update with invalid out");
    }

    @Test
    public void updateWithValidParam() throws PersistenceException {
        logger.info("-> update with valid in");

        ArrayList<Horse> horses = (ArrayList) horseDAO.read();
        Horse h_to_update = horses.get(1);

        h_to_update.setName("TEST_TO_SUCCEED");
        h_to_update.setMinSpeed(50);
        h_to_update.setMaxSpeed(100);
        h_to_update.setPicPath("resources/test.jpg");

        horseDAO.update(h_to_update);
        horses = null;

        horses = (ArrayList) horseDAO.read();

        for (Horse ho : horses) {
            logger.info(ho.toString());
            if (ho.getId() == h_to_update.getId()) {
                assertTrue(h_to_update.equals(ho));
                break;
            }
        }

        logger.info("-> update with valid out");
    }

    @Test(expected = PersistenceException.class)
    public void deleteInvalidObject() throws PersistenceException {
        logger.info("-> delete with invalid in");

        Horse h = new Horse();
        // invalid ID
        h.setId(-1);

        horseDAO.delete(h);
        logger.info("-> delete with invalid out");
    }

    @Test
    public void deleteValidObject() throws PersistenceException {
        logger.info("-> delete with valid in");

        ArrayList<Horse> horses = (ArrayList) horseDAO.read();

        Horse horse_to_delete = horses.get(1);

        horseDAO.delete(horse_to_delete);

        horses = null;
        horses = (ArrayList) horseDAO.read();

        for (Horse ho : horses) {
            assertFalse(horse_to_delete.getId() == ho.getId());
        }

        logger.info("-> delete with valid out");
    }

}