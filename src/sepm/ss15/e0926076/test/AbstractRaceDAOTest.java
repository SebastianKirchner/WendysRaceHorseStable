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
import sepm.ss15.e0926076.persistence.RaceDAO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sebastian on 20.03.2015.
 */
public abstract class AbstractRaceDAOTest {

    private static final Logger logger = LogManager.getLogger(AbstractRaceDAOTest.class);

    protected RaceDAO raceDAO;

    protected void setRaceDAO(RaceDAO race_dao) {
        this.raceDAO = race_dao;
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWithNullShouldThrowException() throws IllegalArgumentException, PersistenceException {
        logger.info("-> create with null in");
        raceDAO.create(null);
        logger.info("-> create with null out");
    }

    @Test
    public void createWithValidParameter() throws PersistenceException {
        logger.info("-> create with valid in");

        ArrayList<Horse> horses     = (ArrayList<Horse>) new DBHorseDAO().read();
        ArrayList<Jockey> jockeys   = (ArrayList<Jockey>) new DBJockeyDAO().read();
        ArrayList<Race> new_races   = new ArrayList<Race>();

        for (int i = 1; i<5; ++i) {
            Race r = new Race();
            r.setRaceID(1);
            r.setHorse(horses.get(i));
            r.setJockey(jockeys.get(i));
            r.setAverageSpeed(i * 33.3);
            r.setRandomSpeed(i * 31.1);
            r.setJockeySkill(jockeys.get(i).getSkill() + 4.5);
            r.setLuckFactor(1.1);
            new_races.add(r);
        }

        ArrayList<Race> old_races = (ArrayList) raceDAO.read();
        assertTrue(old_races.size() == 0);

        raceDAO.create(new_races);

        old_races = (ArrayList) raceDAO.read();

        assertTrue(old_races.size() == 4);

        logger.info("-> create with valid out");
    }

    @Test
    public void readAll() throws PersistenceException {
        logger.info("-> read all in");

        ArrayList<Horse> horses     = (ArrayList<Horse>) new DBHorseDAO().read();
        ArrayList<Jockey> jockeys   = (ArrayList<Jockey>) new DBJockeyDAO().read();
        ArrayList<Race> new_races   = new ArrayList<Race>();

        logger.info("new races:");
        for (int i = 1; i<5; ++i) {
            Race r = new Race();
            r.setRaceID(1);
            r.setHorse(horses.get(i));
            r.setJockey(jockeys.get(i));
            r.setAverageSpeed(i*33);
            r.setRandomSpeed(i*31.1);
            r.setJockeySkill(jockeys.get(i).getSkill()+4.5);
            r.setLuckFactor(1.1);
            new_races.add(r);
        }

        raceDAO.create(new_races);

        ArrayList<Race> races = (ArrayList) raceDAO.read();
        assertTrue(races.size() == 4);

        Race h2 = null;
        for (Race h: races) {
            assertTrue(!h.equals(h2));
            h2 = h;
        }

        logger.info("-> read all out");
    }
}
