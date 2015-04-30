package sepm.ss15.e0926076.persistence;

import sepm.ss15.e0926076.entities.Horse;
import java.util.List;

/**
 * Created by Sebastian on 13.03.2015.
 *
 * Data Access Object for a horse entity
 */
public interface HorseDAO {

    /**
     * Creates an entry in the database with the horse.
     *
     * @param h Horse to be written into database
     * @return the Horse with the unique ID
     * @throws PersistenceException
     */
    Horse create(Horse h) throws PersistenceException;

    /**
     * List all horses currently in database
     *
     * @return list containing all horses found
     * @throws PersistenceException
     */
    List<Horse> read() throws PersistenceException;

    /**
     * Update the given Horse
     *
     * @param h Horse containing the ID of Horse to be updated and new values
     * @return the Horse with updated values
     * @throws PersistenceException
     */
    Horse update(Horse h) throws PersistenceException;

    /**
     * Delete the given horse from the database
     *
     * @param h Horse to be deleted
     * @throws PersistenceException
     */
    void delete(Horse h) throws PersistenceException;
}
