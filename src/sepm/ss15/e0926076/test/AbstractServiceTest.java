package sepm.ss15.e0926076.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import sepm.ss15.e0926076.entities.Horse;
import sepm.ss15.e0926076.entities.Jockey;
import sepm.ss15.e0926076.entities.Race;
import sepm.ss15.e0926076.persistence.DBHorseDAO;
import sepm.ss15.e0926076.persistence.DBJockeyDAO;
import sepm.ss15.e0926076.persistence.PersistenceException;
import sepm.ss15.e0926076.service.Service;
import sepm.ss15.e0926076.service.ServiceException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Sebastian on 09.04.2015.
 */
public abstract class AbstractServiceTest {

    private static final Logger logger = LogManager.getLogger(AbstractServiceTest.class);

    protected Service service;

    protected void setService(Service s) {
        this.service = s;
    }

    @Test(expected = IllegalArgumentException.class)
    public void simulateWithNullShouldThrowException() throws IllegalArgumentException, ServiceException {
        logger.info("-> simulate with null in");
        service.simulateRace(null);
        logger.info("-> simulate with null out");
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWithUnbalancedTeams() throws ServiceException, PersistenceException {
        logger.info("-> simulate with unbalanced in");

        List<Horse> horses = new DBHorseDAO().read();
        List<Jockey> jockeys = new DBJockeyDAO().read();
        List<Race> unbalanced = new ArrayList<Race> ();

        Race r1 = new Race();
        Race r2 = new Race();

        r1.setHorse(horses.get(0));
        r1.setJockey(jockeys.get(0));
        r2.setJockey(jockeys.get(1));
        unbalanced.add(r1);
        unbalanced.add(r2);

        assertTrue(unbalanced.size() == 2);

        List<Race> races = service.simulateRace(unbalanced);

        logger.info("-> simulate with unbalanced out");
    }

    @Test
    public void checkSortedWithValidInput() throws PersistenceException, ServiceException {
        logger.info("-> simulate with valid in");

        List<Horse> horses = new DBHorseDAO().read();
        List<Jockey> jockeys = new DBJockeyDAO().read();
        List<Race> valid = new ArrayList<Race> ();

        assertTrue(horses.size() == jockeys.size());

        for (int i=0; i<horses.size(); ++i) {
            Race r = new Race();
            r.setHorse(horses.get(i));
            r.setJockey(jockeys.get(i));
            valid.add(r);
        }

        assertTrue(horses.size() == valid.size() && valid.size() == jockeys.size());

        List<Race> races = service.simulateRace(valid);

        for (int j=1; j< races.size(); ++j) {
            Race race = races.get(j);
            assertTrue(races.get(j-1).getAverageSpeed() >= race.getAverageSpeed());
            assertTrue(races.get(j-1).getRandomSpeed() >= 0 && races.get(j-1).getRandomSpeed() >= 0.0);
        }

        logger.info("-> simulate with valid out");
    }
}