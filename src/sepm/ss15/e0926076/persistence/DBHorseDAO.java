package sepm.ss15.e0926076.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0926076.entities.Horse;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian on 13.03.2015.
 *
 * Implementation of the Horse DAO, methods to create, update, delete and/or read a Horse entity
 */
public class DBHorseDAO implements HorseDAO {

    private static final Logger logger = LogManager.getLogger(DBHorseDAO.class);

    private Connection con;

    // SQL Statements for CRUD methods
    private PreparedStatement Stmt_create, Stmt_read, Stmt_update, Stmt_delete, Stmt_max_id;
    private String create, read, update, delete, max_id;

    public DBHorseDAO() throws PersistenceException {

        create  = "INSERT INTO horses(horse_name, min_speed, max_speed, is_deleted, pic_path) VALUES  (?, ?, ?, 0, ?)";
        read    = "SELECT horse_id, horse_name, min_speed, max_speed, pic_path FROM horses WHERE is_deleted = 0";
        update  = "UPDATE horses SET horse_name = ?, min_speed = ?, max_speed = ?, pic_path = ? WHERE horse_id = ? AND is_deleted = 0";
        delete  = "UPDATE horses SET is_deleted = 1 WHERE horse_ID = ? AND is_deleted = 0";
        max_id  = "SELECT horse_id FROM horses WHERE horse_id = (SELECT MAX(horse_id) FROM horses)";

        try {
            con = DatabaseConnector.getConnection();
        } catch (SQLException e) {
            logger.error("SQL-Exception in Constructor (Getting Connection).");
            throw new PersistenceException("Error trying to connect to database.");
        }
    }

    /**
     * Creates an entry in the database with the horse.
     *
     * @param h Horse to be written into database
     * @return the Horse with the unique ID
     * @throws PersistenceException
     */
    @Override
    public Horse create(Horse h) throws IllegalArgumentException, PersistenceException {

        if (h == null) { throw new IllegalArgumentException("Given Horse is not a valid object."); }
        logger.info(h.toString() + " is to be saved.");

        try {
            Stmt_create = con.prepareStatement(create);

            // get the ID the horse will receive for naming of the picture
            h.setId(getMaxID());

            // move the profile picture to a set folder
            h = movePicture(h);

            Stmt_create.setString(1, h.getName());
            Stmt_create.setInt(2, h.getMinSpeed());
            Stmt_create.setInt(3, h.getMaxSpeed());
            Stmt_create.setString(4, h.getPicPath());
            Stmt_create.executeUpdate();

            if (Stmt_create != null) { Stmt_create.close(); }

        }  catch (SQLException e) {
            logger.error("ERROR while saving " + h.toString());
            throw new PersistenceException("ERROR while saving" + h.toString());
        }

        logger.info(h.toString() + " was saved.");
        return h;
    }

    /**
     * List all horses currently in database
     *
     * @return list containing all horses found
     * @throws PersistenceException
     */
    @Override
    public List<Horse> read() throws PersistenceException {
        logger.info("-> read all elements in table 'horses'");

        List<Horse> horses = new ArrayList<Horse>();

        try {
            Stmt_read = con.prepareStatement(read);
            ResultSet result = Stmt_read.executeQuery();

            while (result.next()) {
                Horse h = new Horse();
                h.setId(result.getInt("horse_id"));
                h.setName(result.getString("horse_name"));
                h.setMinSpeed(result.getInt("min_speed"));
                h.setMaxSpeed(result.getInt("max_speed"));
                h.setPicPath(result.getString("pic_path"));
                horses.add(h);
            }

            if (Stmt_read != null) { Stmt_read.close(); }

        } catch (SQLException e) {
            logger.error("ERROR while trying to read all elements.");
            throw new PersistenceException("ERROR while trying to read all elements.");
        }

        logger.info("-> # of elements found in table: " + horses.size());
        return horses;
    }

    /**
     * Update the given Horse
     *
     * @param h Horse containing the ID of Horse to be updated and new values
     * @return the Horse with updated values
     * @throws PersistenceException
     */
    @Override
    public Horse update(Horse h) throws PersistenceException {

        if (h == null) { throw new IllegalArgumentException("Given Horse is not a valid object."); }

        logger.info("-> Update Horse with ID: " + h.getId());

        int result = 0;

        try {
            Stmt_update = con.prepareStatement(update);

            // move the picture to set location
            h = movePicture(h);

            Stmt_update.setString(1, h.getName());
            Stmt_update.setInt(2, h.getMinSpeed());
            Stmt_update.setInt(3, h.getMaxSpeed());
            Stmt_update.setString(4, h.getPicPath());
            Stmt_update.setInt(5, h.getId());
            result = Stmt_update.executeUpdate();

            if (Stmt_update != null) { Stmt_update.close(); }

        } catch (SQLException e) {
            logger.error("ERROR trying to update");
            throw new PersistenceException("ERROR trying to update Horse " + "(ID=" +h.getId()+", Name=" + h.getName() +")");
        }

        // valid upate => only one row has been changed
        if (result != 1) { throw new PersistenceException("Unexpected Result after delete action, # of changes in database: " + result); }

        logger.info("-> Update successful for " + h.toString());

        return h;
    }

    /**
     * Delete the given horse from the database
     *
     * @param h Horse to be deleted
     * @throws PersistenceException
     */
    @Override
    public void delete(Horse h) throws PersistenceException {

        if (h == null) { throw new IllegalArgumentException("Given Horse is not a valid object."); }
        logger.info("-> Delete element: " + h.toString());

        int result = 0;
        try {
            Stmt_delete = con.prepareStatement(delete);
            Stmt_delete.setInt(1, h.getId());
            result = Stmt_delete.executeUpdate();

            if (Stmt_delete != null) { Stmt_delete.close(); }

        } catch (SQLException e) {
            logger.info("ERROR while deleting " + h.toString());
            throw new PersistenceException("ERROR while trying to delete " + h.toString());
        }

        if (result != 1) {
            logger.info("-> Delete not successful, number of updates: " + result);
            throw new PersistenceException("Unexpected Result after delete action, # of changes in database: " + result);
        }

        // delete the picture file of this horse entity
        if (new File(h.getPicPath()).exists()) {
            new File(h.getPicPath()).delete();
        }

        logger.info("-> Successfully deleted " + h.toString());
    }

    /**
     * Helper method to get the maximum ID
     *
     * @return the current maximum ID in database + 1 (= the ID of the next object)
     * @throws PersistenceException
     */
    private int getMaxID() throws PersistenceException {

        int id = 1;

        try {
            Stmt_max_id = con.prepareStatement(max_id);

            ResultSet result_ID = Stmt_max_id.executeQuery();

            if (result_ID.next()) {
                id = result_ID.getInt("horse_id") + 1;
            }
        } catch (SQLException e) {
            throw new PersistenceException("ERROR trying to get maximum ID");
        }

        return id;
    }

    /**
     * Move the profile picture to a set location, on update deletes the old picture first
     * before moving the new one
     *
     * @param h Horse of which the picture will be moved
     * @return the Horse with the new picture path set
     * @throws PersistenceException
     */
    private Horse movePicture(Horse h) throws PersistenceException{

        File f = new File(h.getPicPath());
        String extension;
        try {
            extension = f.getPath().substring(f.getPath().lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new PersistenceException("The given path is not valid: \n" + (f.getPath() != null ? f.getPath() : "NO PATH GIVEN"));
        }
        String new_path = "resources/horse"+h.getId()+extension;

        File new_f = new File(new_path);

        /*
         new path == old path means Horse is being updated, but the picture has not been
         changed, therefore the old picture must not be deleted
          */
        if (new_f.exists() && !new_path.equals(h.getPicPath())) {
            new_f.delete();
        }

        f.renameTo(new File(new_path));

        h.setPicPath(new_path);
        return h;
    }
}
