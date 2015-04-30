package sepm.ss15.e0926076.persistence;

import sepm.ss15.e0926076.entities.Race;

import java.util.List;

/**
 * Created by Sebastian on 13.03.2015.
 *
 * Data Access Object for a race entity, can not be changed or deleted once it is saved
 */
public interface RaceDAO {

    /**
     * Creates an entry in the database with the race objects
     *
     * @param races list of Race-Objects to be written into database
     * @return the id of the race
     * @throws PersistenceException
     */
    int create(List<Race> races) throws PersistenceException;

    /**
     * List all races currently in database
     *
     * @return list containing all races found
     * @throws PersistenceException
     */
    List<Race> read() throws PersistenceException;

}
