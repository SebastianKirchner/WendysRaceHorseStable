package sepm.ss15.e0926076.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0926076.entities.Horse;
import sepm.ss15.e0926076.entities.Jockey;
import sepm.ss15.e0926076.entities.Race;
import sepm.ss15.e0926076.persistence.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Sebastian on 23.03.2015.
 */
public class SimpleService implements Service {

    private static final Logger logger = LogManager.getLogger(SimpleService.class);

    private HorseDAO    horse_dao;
    private JockeyDAO   jockey_dao;
    private RaceDAO     race_dao;

    public SimpleService() throws ServiceException {

        try {
            horse_dao   = new DBHorseDAO();
            jockey_dao  = new DBJockeyDAO();
            race_dao    = new DBRaceDAO();
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Save a horse in the database
     *
     * @param h Horse to be saved
     * @return Horse that was saved with ID
     * @throws IllegalArgumentException invalid Horse
     * @throws ServiceException error in Persistence
     */
    @Override
    public Horse createHorse(Horse h) throws IllegalArgumentException, ServiceException {

        if (isValidHorse(h)) {
            try {
                return horse_dao.create(h);
            } catch (PersistenceException e) {
                throw new ServiceException(e.getMessage());
            }
        }

        return null;
    }

    /**
     * Update a horse
     *
     * @param h Horse with updated values
     * @return Horse with updated values
     * @throws IllegalArgumentException invalid Horse
     * @throws ServiceException error in persistence
     */
    @Override
    public Horse updateHorse(Horse h) throws IllegalArgumentException, ServiceException {

        if (isValidHorse(h)) {
            try {
                return horse_dao.update(h);
            } catch (PersistenceException e) {
                throw new ServiceException(e.getMessage());
            }
        }
        return null;
    }

    /**
     * Delete a Horse
     *
     * @param h Horse to be deleted
     * @throws IllegalArgumentException invalid Horse
     * @throws ServiceException error in persistence
     */
    @Override
    public void deleteHorse(Horse h) throws IllegalArgumentException, ServiceException {

        if (isValidHorse(h)) {
            try {
                horse_dao.delete(h);
            } catch (PersistenceException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }

    /**
     * Read all Horses in Database
     *
     * @return List containing all Horses
     * @throws ServiceException error in persistence
     */
    @Override
    public List<Horse> readHorses() throws ServiceException {

        ArrayList<Horse> horses = new ArrayList<Horse>();

        try {
            horses = (ArrayList<Horse>) horse_dao.read();
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }

        if (horses == null || horses.isEmpty()) {
            throw new ServiceException("No Horses in Database.");
        }

        return horses;
    }

    /**
     * Read all Horses in Database with name
     *
     * @param name
     * @return List containing all Horses called name
     * @throws IllegalArgumentException invalid String
     * @throws ServiceException error in persistence
     */
    @Override
    public List<Horse> readHorses(String name) throws IllegalArgumentException, ServiceException {

        if (name == null || name.length() == 0) { throw new IllegalArgumentException("Invalid Name to look for"); }

        ArrayList<Horse> horses = (ArrayList<Horse>) readHorses();
        ArrayList<Horse> horses_by_name = new ArrayList<Horse>();

        for (Horse h : horses) {
            if (h.getName().equals(name)) {
                horses_by_name.add(h);
            }
        }

        if (horses_by_name == null || horses_by_name.isEmpty()) {
            throw new ServiceException("No Horses with name '" + name + "' in Database." );
        }

        return horses_by_name;
    }

    /**
     * Check a Horse entity to see if all parameters are valid
     *
     * @param h Horse to be validated
     * @return true if all parameters are valid
     * @throws IllegalArgumentException
     */
    private boolean isValidHorse(Horse h) throws IllegalArgumentException {
        if (h == null) {
            throw new IllegalArgumentException("No Horse was given.");
        }

        if (h.getName() == null || h.getName().length() >= 20) {
            throw new IllegalArgumentException("Invalid Name.");
        }

        if (h.getMinSpeed() < 50 || h.getMinSpeed() > 100 || h.getMaxSpeed() < 50 || h.getMaxSpeed() > 100 || h.getMinSpeed() > h.getMaxSpeed()) {
            throw new IllegalArgumentException("Invalid Speed, min- and max-speed must be between 50 and 100 km/h.");
        }

        return true;
    }

    /**
     * Save a jockey in Database
     *
     * @param j jockey to be saved
     * @return Jockey that was saved with ID
     * @throws IllegalArgumentException invalid Jockey
     * @throws ServiceException error in persistence
     */
    @Override
    public Jockey createJockey(Jockey j) throws IllegalArgumentException, ServiceException {

        if (isValidJockey(j)) {
            try {
                return jockey_dao.create(j);
            } catch (PersistenceException e) {
                throw new ServiceException(e.getMessage());
            }
        }

        return null;
    }

    /**
     * Update a Jockey
     *
     * @param j Jockey with updated values
     * @return Jockey with updated values
     * @throws IllegalArgumentException invalid Jockey
     * @throws ServiceException error in persistence
     */
    @Override
    public Jockey updateJockey(Jockey j) throws IllegalArgumentException, ServiceException {

        if (isValidJockey(j)) {
            try {
                return jockey_dao.update(j);
            } catch (PersistenceException e) {
                throw new ServiceException(e.getMessage());
            }
        }
        return null;
    }

    /**
     * Delete a Jockey
     *
     * @param j Jockey to be deleted
     * @throws IllegalArgumentException invalid Jockey
     * @throws ServiceException error in persistence
     */
    @Override
    public void deleteJockey(Jockey j) throws IllegalArgumentException, ServiceException {

        if (isValidJockey(j)) {
            try {
                jockey_dao.delete(j);
            } catch (PersistenceException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }

    /**
     * Read all Jockeys in Database
     *
     * @return List containing all Horses
     * @throws ServiceException error in persistence
     */
    @Override
    public List<Jockey> readJockeys() throws ServiceException {

        ArrayList<Jockey> jockeys = new ArrayList<Jockey>();

        try {
            jockeys = (ArrayList<Jockey>) jockey_dao.read();
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }

        if (jockeys == null || jockeys.isEmpty()) {
            throw new ServiceException("No Jockeys in Database.");
        }

        return jockeys;
    }

    /**
     * Read all Jockeys in Database with name
     *
     * @param name
     * @return List containing all Jockeys called name
     * @throws IllegalArgumentException invalid String
     * @throws ServiceException error in persistence
     */
    @Override
    public List<Jockey> readJockeys(String name) throws IllegalArgumentException, ServiceException {

        if (name == null || name.length() == 0) { throw new IllegalArgumentException("Invalid Name to look for"); }

        ArrayList<Jockey> jockeys = (ArrayList<Jockey>) readJockeys();
        ArrayList<Jockey> jockeys_by_name = new ArrayList<Jockey>();

        for (Jockey j : jockeys) {
            if (j.getName().equals(name)) {
                jockeys_by_name.add(j);
            }
        }

        if (jockeys_by_name == null || jockeys_by_name.isEmpty()) {
            throw new ServiceException("No Jockeys with name '" + name + "' in Database." );
        }

        return jockeys_by_name;
    }

    /**
     * Check a Jockey entity to see if all parameters are valid
     *
     * @param j Jockey to be validated
     * @return true if all parameters are valid
     * @throws IllegalArgumentException
     */
    private boolean isValidJockey(Jockey j) throws IllegalArgumentException {
        if (j == null) {
            throw new IllegalArgumentException("No Jockey was given. ");
        }

        if (j.getName() == null || j.getName().length() >= 20) {
            throw new IllegalArgumentException("Invalid Name. ");
        }

        if (j.getHeight() < 50 || j.getHeight() > 250) {
            throw new IllegalArgumentException("Invalid Height (must be >50cm and <250cm). ");
        }

        if (!j.getSex().equals("m") && !j.getSex().equals("w")) {
            throw new IllegalArgumentException("Sex must be either 'm' or 'w' " + j.getSex());
        }

        return true;
    }

    /**
     * Simulation of a race, calculates various values for each team and saves that simulation in the database
     *
     * @param teams the teams taking part in the simulation
     * @return an ordered list (first to last place) containing Race objects with all values set
     * @throws ServiceException
     */
    public List<Race> simulateRace(List<Race> teams) throws ServiceException {

        if (teams == null) { throw new IllegalArgumentException("Please choose valid Teams."); }

        List<Integer> check_horses  = new ArrayList<>();
        List<Integer> check_jockeys = new ArrayList<>();
        List<Race> new_races = new ArrayList<>();

        for (Race team : teams) {

            double rand_speed = 0, avg_speed = 0, jock_skill = 0, luck = 0;

            if (team.getJockey() == null || team.getHorse() == null) {
                throw new IllegalArgumentException("No Jockey or Horse in this team.");
            }

            if (!check_horses.contains(team.getHorse().getId())) { check_horses.add(team.getHorse().getId()); }
            else {
                throw new IllegalArgumentException("Your Horse '" + team.getHorse().getName() + "' with ID " + team.getHorse().getId() +
                        " is twice taking part in the race.");
            }

            if (!check_jockeys.contains(team.getJockey().getId())) { check_jockeys.add(team.getJockey().getId()); }
            else {
                throw new IllegalArgumentException("Your Jockey '" + team.getJockey().getName() + "' with ID " + team.getJockey().getId() +
                    " is twice taking part in the race.");
            }

            int min_max_diff    = team.getHorse().getMaxSpeed() - team.getHorse().getMinSpeed();
            rand_speed           = Math.random() * (double) min_max_diff + (double) team.getHorse().getMinSpeed();
            jock_skill          = 1 + (0.15 * (1.0/Math.PI)) * Math.atan(0.2 * team.getJockey().getSkill());
            luck                = Math.random()*0.1 + 0.95;

            avg_speed = rand_speed * jock_skill * luck;

            team.setAverageSpeed(BigDecimal.valueOf(avg_speed).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
            team.setRandomSpeed(BigDecimal.valueOf(rand_speed).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
            team.setJockeySkill(BigDecimal.valueOf(jock_skill).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
            team.setLuckFactor(BigDecimal.valueOf(luck).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
        }

        if (check_horses.size() != check_jockeys.size()) {
            throw new IllegalArgumentException("Number of Horses and Jockeys don't match up: " + check_horses.size() +
                    " Horses and " + check_jockeys.size() + " Jockeys!");
        }

        teams.sort(new Comparator<Race>() {
            @Override
            public int compare(Race o1, Race o2) {
                return o1.getAverageSpeed() <= o2.getAverageSpeed() ? 1 : -1;
            }
        });

        /*
        set the ranking
         */
        int place = 0;
        for (Race r : teams) {
            r.setPlacement(++place);
        }

        try {
            int race_id = race_dao.create(teams);
            new_races = readRaces(race_id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }

        new_races.sort(new Comparator<Race>() {
            @Override
            public int compare(Race o1, Race o2) {
                return o1.getAverageSpeed() <= o2.getAverageSpeed() ? 1 : -1;
            }
        });

        return new_races;
    }

    /**
     * Read all Races
     *
     * @return list of all races
     * @throws ServiceException
     */
    @Override
    public List<Race> readRaces() throws ServiceException {

        List<Race> races = new ArrayList<Race>();

        try {
            races = race_dao.read();
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }

        if (races == null || races.isEmpty()) {
            throw new ServiceException("No Races in Database.");
        }

        return races;
    }

    /**
     * Read all the Races that a certain horse has taken part in (gives back the full simulation)
     *
     * @param h the Horse for which to look for
     * @return the list containing all simulations the Horse was part of
     * @throws IllegalArgumentException
     * @throws ServiceException
     */
    @Override
    public List<Race> readRaces(Horse h) throws IllegalArgumentException, ServiceException {

        if (h == null) { throw new IllegalArgumentException("A Horse must be selected."); }

        List<Race> races;
        List<Race> races_by_horse = new ArrayList<Race>();

        try {
            races = readRaces();
        } catch (ServiceException e) {
            if (e.getMessage().equals("No Races in Database.")) {
                throw new ServiceException("Horse '" + h.getName() + "' with ID " + h.getId() + " has not taken part in any Races yet.");
            } else {
                throw new ServiceException(e);
            }
        }

        // set of all the race IDs that the Horse has been part of
        Set<Integer> race_IDs = new HashSet<Integer>();

        for (Race r : races) {
            if (r.getHorse().getId() == h.getId()) {
                race_IDs.add(r.getRaceID());
            }
        }

        if (race_IDs == null || race_IDs.isEmpty()) {
            throw new ServiceException("Horse '" + h.getName() + "' with ID " + h.getId() + " has not taken part in any Races yet." );
        }

        for (Integer i:race_IDs) {
            races_by_horse.addAll(readRaces(i));
        }

        return races_by_horse;
    }

    /**
     * Read all the Races that a certain jockey has taken part in (gives back the full simulation)
     *
     * @param j the Jockey for which to look for
     * @return the list containing all simulations the Jockey was part of
     * @throws IllegalArgumentException
     * @throws ServiceException
     */
    @Override
    public List<Race> readRaces(Jockey j) throws IllegalArgumentException, ServiceException {

        if (j == null) { throw new IllegalArgumentException("A Jockey must be selected."); }

        List<Race> races;
        try {
            races = readRaces();
        } catch (ServiceException e) {
            if (e.getMessage().equals("No Races in Database.")) {
                throw new ServiceException("Jockey '" + j.getName() + "' with ID " + j.getId() + " has not taken part in any Races yet.");
            } else {
                throw new ServiceException(e);
            }
        }
        List<Race> races_by_jockey = new ArrayList<Race>();

        // set of all the race IDs that the Jockey has been part of
        Set<Integer> race_IDs = new HashSet<Integer>();

        for (Race r : races) {
            if (r.getJockey().getId() == j.getId()) {
                race_IDs.add(r.getRaceID());
            }
        }

        if (race_IDs == null || race_IDs.isEmpty()) {
            throw new ServiceException("Jockey '" + j.getName() + "' with ID " + j.getId() + " has not taken part in any Races yet." );
        }

        for (Integer i:race_IDs) {
            races_by_jockey.addAll(readRaces(i));
        }

        return races_by_jockey;
    }

    /**
     * Read a certain Simulation with the given race ID
     *
     * @param race_id Simulation with this ID will be returned
     * @return List of Race Objects with the given ID
     * @throws IllegalArgumentException
     * @throws ServiceException
     */
    @Override
    public List<Race> readRaces(int race_id) throws IllegalArgumentException, ServiceException {

        if (race_id < 0) { throw new IllegalArgumentException(race_id + " is not a valid ID."); }

        List<Race> races = readRaces();
        List<Race> races_by_ID = new ArrayList<Race>();

        for (Race r : races) {
            if (r.getRaceID() == race_id) {
                races_by_ID.add(r);
            }
        }

        if (races_by_ID == null || races_by_ID.isEmpty()) {
            throw new ServiceException("There are no races with ID " + race_id);
        }

        return races_by_ID;
    }

    /**
     * Makes a simple statistic of how often the Horse has taken a certain place in all of its races
     *
     * @param h Horse for which to make the statistic
     * @return a Map containing the key as the position and the value as the nr. of times this position was had
     * @throws IllegalArgumentException
     * @throws ServiceException
     */
    @Override
    public Map<Integer, Integer> getStatistics(Horse h) throws IllegalArgumentException, ServiceException {
        if (h == null) { throw new IllegalArgumentException("A Horse must be selected."); }

        Map<Integer, Integer> placement = new HashMap<>();

        List<Race> races = readRaces(h);

        for (Race r : races) {

            if (h.getId() == r.getHorse().getId()) {
                placement.put(r.getPlacement(),
                        (placement.get(r.getPlacement()) != null ? placement.get(r.getPlacement()) + 1 : 1));
            }
        }
        return placement;
    }

    /**
     * Makes a simple statistic of how often the Jockey has taken a certain place in all of its races
     *
     * @param j Jockey for which to make the statistic
     * @return a Map containing the key as the position and the value as the nr. of times this position was had
     * @throws IllegalArgumentException
     * @throws ServiceException
     */
    @Override
    public Map<Integer, Integer> getStatistics(Jockey j) throws IllegalArgumentException, ServiceException {
        if (j == null) { throw new IllegalArgumentException("A Jockey must be selected."); }

        Map<Integer, Integer> placement = new HashMap<>();

        List<Race> races = readRaces(j);

        for (Race r : races) {

            if (j.getId().equals(r.getJockey().getId())) {
                placement.put(r.getPlacement(),
                        (placement.get(r.getPlacement()) != null ? placement.get(r.getPlacement()) + 1 : 1));
            }
        }
        return placement;
    }

    /**
     * Makes a simple statistic of how often a Jockey Horse combination has taken a certain place in all of its races
     *
     * @param h Horse that is part of the team
     * @param j Jockey that is part of the team
     * @return a Map containing the key as the position and the value as the nr. of times this position was had
     * @throws IllegalArgumentException
     * @throws ServiceException
     */
    @Override
    public Map<Integer, Integer> getStatistics(Horse h, Jockey j) throws IllegalArgumentException, ServiceException {
        if (j == null || h == null) { throw new IllegalArgumentException("A Jockey and a Horse must be selected."); }

        Map<Integer, Integer> placement = new HashMap<>();

        List<Race> races_j = readRaces(j);
        List<Race> races_h = readRaces(h);

        /*
        Go through the smaller List
         */
        if (races_h.size() > races_j.size()) {
            races_h = null;

            for (Race r : races_j) {
                if (j.getId().equals(r.getJockey().getId()) && h.getId().equals(r.getHorse().getId())) {
                    placement.put(r.getPlacement(),
                            placement.get(r.getPlacement()) != null ? placement.get(r.getPlacement()) + 1 : 1);
                }
            }
        } else {
            races_j = null;

            for (Race r : races_h) {
                if (j.getId().equals(r.getJockey().getId()) && h.getId().equals(r.getHorse().getId())) {
                    placement.put(r.getPlacement(),
                            placement.get(r.getPlacement()) != null ? placement.get(r.getPlacement()) + 1 : 1);
                }
            }
        }
        return placement;
    }
}
