package sepm.ss15.e0926076.service;

import sepm.ss15.e0926076.entities.Horse;
import sepm.ss15.e0926076.entities.Jockey;
import sepm.ss15.e0926076.entities.Race;

import java.util.List;
import java.util.Map;

/**
 * Created by Sebastian on 23.03.2015.
 *
 * Service Interface, acts as connection between Persistence and GUI layers
 */
public interface Service {

    /**
     * Save a horse in the database
     *
     * @param h Horse to be saved
     * @return Horse that was saved with ID
     * @throws IllegalArgumentException invalid Horse
     * @throws ServiceException error in Persistence
     */
    Horse createHorse(Horse h) throws IllegalArgumentException, ServiceException;

    /**
     * Update a horse
     *
     * @param h Horse with updated values
     * @return Horse with updated values
     * @throws IllegalArgumentException invalid Horse
     * @throws ServiceException error in persistence
     */
    Horse updateHorse(Horse h) throws IllegalArgumentException, ServiceException;

    /**
     * Delete a Horse
     *
     * @param h Horse to be deleted
     * @throws IllegalArgumentException invalid Horse
     * @throws ServiceException error in persistence
     */
    void deleteHorse(Horse h) throws IllegalArgumentException, ServiceException;

    /**
     * Read all Horses in Database
     *
     * @return List containing all Horses
     * @throws ServiceException error in persistence
     */
    List<Horse> readHorses() throws ServiceException;

    /**
     * Read all Horses in Database with name
     *
     * @param name
     * @return List containing all Horses called name
     * @throws IllegalArgumentException invalid String
     * @throws ServiceException error in persistence
     */
    List<Horse> readHorses(String name) throws IllegalArgumentException, ServiceException;

    /**
     * Save a jockey in Database
     *
     * @param j jockey to be saved
     * @return Jockey that was saved with ID
     * @throws IllegalArgumentException invalid Jockey
     * @throws ServiceException error in persistence
     */
    Jockey createJockey(Jockey j) throws IllegalArgumentException, ServiceException;

    /**
     * Update a Jockey
     *
     * @param j Jockey with updated values
     * @return Jockey with updated values
     * @throws IllegalArgumentException invalid Jockey
     * @throws ServiceException error in persistence
     */
    Jockey updateJockey(Jockey j) throws IllegalArgumentException, ServiceException;

    /**
     * Delete a Jockey
     *
     * @param j Jockey to be deleted
     * @throws IllegalArgumentException invalid Jockey
     * @throws ServiceException error in persistence
     */
    void deleteJockey(Jockey j) throws IllegalArgumentException, ServiceException;

    /**
     * Read all Jockeys in Database
     *
     * @return List containing all Horses
     * @throws ServiceException error in persistence
     */
    List<Jockey> readJockeys() throws ServiceException;

    /**
     * Read all Jockeys in Database with name
     *
     * @param name
     * @return List containing all Jockeys called name
     * @throws IllegalArgumentException invalid String
     * @throws ServiceException error in persistence
     */
    List<Jockey> readJockeys(String name) throws IllegalArgumentException, ServiceException;

    /**
     * Simulation of a race, calculates various values for each team and saves that simulation in the database
     *
     * @param teams the teams taking part in the simulation
     * @return an ordered list (first to last place) containing Race objects with all values set
     * @throws ServiceException
     */
    List<Race> simulateRace(List<Race> teams) throws ServiceException;

    /**
     * Read all Races
     *
     * @return list of all races
     * @throws ServiceException
     */
    List<Race> readRaces() throws ServiceException;

    /**
     * Read all the Races that a certain horse has taken part in (gives back the full simulation)
     *
     * @param h the Horse for which to look for
     * @return the list containing all simulations the Horse was part of
     * @throws IllegalArgumentException
     * @throws ServiceException
     */
    List<Race> readRaces(Horse h) throws IllegalArgumentException, ServiceException;

    /**
     * Read all the Races that a certain jockey has taken part in (gives back the full simulation)
     *
     * @param j the Jockey for which to look for
     * @return the list containing all simulations the Jockey was part of
     * @throws IllegalArgumentException
     * @throws ServiceException
     */
    List<Race> readRaces(Jockey j) throws IllegalArgumentException, ServiceException;

    /**
     * Read a certain Simulation with the given race ID
     *
     * @param race_id Simulation with this ID will be returned
     * @return List of Race Objects with the given ID
     * @throws IllegalArgumentException
     * @throws ServiceException
     */
    List<Race> readRaces(int race_id) throws IllegalArgumentException, ServiceException;

    /**
     * Makes a simple statistic of how often the Horse has taken a certain place in all of its races
     *
     * @param h Horse for which to make the statistic
     * @return a Map containing the key as the position and the value as the nr. of times this position was had
     * @throws IllegalArgumentException
     * @throws ServiceException
     */
    Map<Integer, Integer> getStatistics(Horse h) throws IllegalArgumentException, ServiceException;

    /**
     * Makes a simple statistic of how often the Jockey has taken a certain place in all of its races
     *
     * @param j Jockey for which to make the statistic
     * @return a Map containing the key as the position and the value as the nr. of times this position was had
     * @throws IllegalArgumentException
     * @throws ServiceException
     */
    Map<Integer, Integer> getStatistics(Jockey j) throws IllegalArgumentException, ServiceException;

    /**
     * Makes a simple statistic of how often a Jockey Horse combination has taken a certain place in all of its races
     *
     * @param h Horse that is part of the team
     * @param j Jockey that is part of the team
     * @return a Map containing the key as the position and the value as the nr. of times this position was had
     * @throws IllegalArgumentException
     * @throws ServiceException
     */
    Map<Integer, Integer> getStatistics(Horse h, Jockey j) throws IllegalArgumentException, ServiceException;
}
