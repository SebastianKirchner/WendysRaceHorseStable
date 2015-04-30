package sepm.ss15.e0926076.persistence;

import sepm.ss15.e0926076.entities.Jockey;

import java.util.List;

/**
 * Created by Sebastian on 13.03.2015.
 *
 * Data Access Object for a jockey entity
 */
public interface JockeyDAO {

    /**
     * Creates an entry in the database with the Jockey.
     *
     * @param j Jockey to be written into database
     * @return the Jockey with the unique ID
     * @throws PersistenceException
     */
    Jockey create(Jockey j) throws PersistenceException;

    /**
     * List all jockeys currently in database
     *
     * @return list containing all jockeys found
     * @throws PersistenceException
     */
    List<Jockey> read() throws PersistenceException;

    /**
     * Update the given Jockey
     *
     * @param j Jockey containing the ID of Jockey to be updated and new values
     * @return the Jockey with updated values
     * @throws PersistenceException
     */
    Jockey update(Jockey j) throws PersistenceException;

    /**
     * Delete the given Jockey from the database
     *
     * @param j Jockey to be deleted
     * @throws PersistenceException
     */
    void delete(Jockey j) throws PersistenceException;
}
