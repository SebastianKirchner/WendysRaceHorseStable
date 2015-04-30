package sepm.ss15.e0926076.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0926076.entities.Jockey;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian on 15.03.2015.
 *
 * Implementation of the Jockey DAO, methods to create, update, delete and/or read a Jockey entity
 */
public class DBJockeyDAO implements JockeyDAO {

    private static final Logger logger = LogManager.getLogger(DBJockeyDAO.class);

    private Connection con;

    // SQL Statements for CRUD methods
    private PreparedStatement Stmt_create, Stmt_read, Stmt_update, Stmt_delete, Stmt_max_id;
    private String create, read, update, delete, max_id;

    public DBJockeyDAO() throws PersistenceException {

        create  = "INSERT INTO jockeys(jockey_name, sex, height, skill, is_deleted) VALUES (?, ?, ?, ?, 0)";
        read    = "SELECT jockey_ID, jockey_name, sex, height, skill FROM jockeys WHERE is_deleted = 0";
        update  = "UPDATE jockeys SET jockey_name = ?, sex = ?, height = ?, skill = ? WHERE jockey_ID = ? AND is_deleted = 0";
        delete  = "UPDATE jockeys SET is_deleted = 1 WHERE jockey_ID = ? AND is_deleted = 0";
        max_id  = "SELECT jockey_ID FROM jockeys WHERE jockey_ID = (SELECT MAX(jockey_ID) FROM jockeys)";

        try {
            con = DatabaseConnector.getConnection();
        } catch (SQLException e) {
            logger.error("SQL-Exception in Constructor (establishing connection to DB)");
            throw new PersistenceException("Error trying to connect to database.");
        }
    }

    /**
     * Creates an entry in the database with the Jockey.
     *
     * @param j Jockey to be written into database
     * @return the Jockey with the unique ID
     * @throws PersistenceException
     */
    @Override
    public Jockey create(Jockey j) throws PersistenceException {

        if (j == null) { throw new IllegalArgumentException(); }
        logger.info(j.toString() + "is to be saved.");

        int id = -1;
        try {
            Stmt_create = con.prepareStatement(create);
            Stmt_create.setString(1, j.getName());
            Stmt_create.setString(2, j.getSex());
            Stmt_create.setInt(3, j.getHeight());
            Stmt_create.setDouble(4, j.getSkill());
            Stmt_create.executeUpdate();

            if (Stmt_create != null) { Stmt_create.close(); }

            /*
            get the ID of the saved jockey
             */
            Stmt_max_id = con.prepareStatement(max_id);
            ResultSet result_ID = Stmt_max_id.executeQuery();

            if (result_ID.next()) {
                id = result_ID.getInt("jockey_ID");
                j.setId(id);
            }

        } catch (SQLException e) {
            logger.error("ERROR while saving " + j.toString());
            throw new PersistenceException("ERROR while saving " + j.toString());
        }

        logger.info(j.toString() + "was saved.");
        return j;
    }

    /**
     * List all jockeys currently in database
     *
     * @return list containing all jockeys found
     * @throws PersistenceException
     */
    @Override
    public List<Jockey> read() throws PersistenceException {
        logger.info("-> Read all elements in table 'jockeys'");

        List<Jockey> jockeys = new ArrayList<Jockey>();

        try {
            Stmt_read = con.prepareStatement(read);
            ResultSet result = Stmt_read.executeQuery();

            while (result.next()) {
                Jockey j = new Jockey();
                j.setId(result.getInt("jockey_ID"));
                j.setName(result.getString("jockey_name"));
                j.setSex(result.getString("sex"));
                j.setHeight(result.getInt("height"));
                j.setSkill(result.getDouble("skill"));
                jockeys.add(j);
            }

            if (Stmt_read != null) { Stmt_read.close(); }

        } catch (SQLException e) {
            logger.error("ERROR while trying to read all elements");
            throw new PersistenceException("ERROR while trying to read all elements");
        }

        logger.info("-> # of elements found in table: " + jockeys.size());
        return jockeys;
    }

    /**
     * Update the given Jockey
     *
     * @param j Jockey containing the ID of Jockey to be updated and new values
     * @return the Jockey with updated values
     * @throws PersistenceException
     */
    @Override
    public Jockey update(Jockey j) throws PersistenceException {

        if (j == null) {throw new IllegalArgumentException("Given Jockey is not a valid object.");}

        logger.info("-> Update Jockey with ID: " + j.getId());

        int result = 0;

        try {
            Stmt_update = con.prepareStatement(update);
            Stmt_update.setString(1, j.getName());
            Stmt_update.setString(2, j.getSex());
            Stmt_update.setInt(3, j.getHeight());
            Stmt_update.setDouble(4, j.getSkill());
            Stmt_update.setInt(5, j.getId());
            result = Stmt_update.executeUpdate();

            if (Stmt_update != null) { Stmt_update.close(); }

        } catch (SQLException e) {
            logger.error("ERROR trying to update " + j.toString());
            throw new PersistenceException("ERROR trying to update " + j.toString());
        }

        // valid upate => only one row has been changed
        if (result != 1) { throw new PersistenceException("Unexpected Result after delete action, # of changes in database: " + result); }

        logger.info("-> Update successful for " + j.toString());

        return j;
    }

    /**
     * Delete the given Jockey from the database
     *
     * @param j Jockey to be deleted
     * @throws PersistenceException
     */
    @Override
    public void delete(Jockey j) throws PersistenceException {

        if (j == null) {throw new IllegalArgumentException("Given Jockey is not a valid object.");}
        logger.info("-> Delete element: " + j.toString());

        int result = 0;
        try {
            Stmt_delete = con.prepareStatement(delete);
            Stmt_delete.setInt(1, j.getId());
            result = Stmt_delete.executeUpdate();

            if (Stmt_delete != null) { Stmt_delete.close(); }

        } catch (SQLException e) {
            logger.info("ERROR while deleting " + j.toString());
            throw new PersistenceException("ERROR while deleting " + j.toString());
        }

        if (result != 1) {
            logger.info("-> Delete not successful, number of updates: " + result);
            throw new PersistenceException("Unexpected Result after delete action, # of changes in database: " + result);
        }

        logger.info("-> Successfully deleted " + j.toString());
    }
}
