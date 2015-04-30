package sepm.ss15.e0926076.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0926076.entities.Horse;
import sepm.ss15.e0926076.entities.Jockey;
import sepm.ss15.e0926076.entities.Race;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian on 15.03.2015.
 *
 * Implementation of the Race DAO, methods to create and read a Race DAO (Race can not be changed/or deleted once it was saved)
 */
public class DBRaceDAO implements RaceDAO {

    private static final Logger logger = LogManager.getLogger(DBRaceDAO.class);

    private Connection con;

    // SQL Statements for CRUD methods
    private PreparedStatement Stmt_create, Stmt_read, Stmt_max_id;
    private String create, read, max_id;

    public DBRaceDAO() throws PersistenceException {

        create  = "INSERT INTO races(race_ID, horse_ID, jockey_ID, average_speed, random_speed, jockey_skill, luck_factor, placement) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        read    = "SELECT r.race_ID, r.horse_ID, r.jockey_ID, r.average_speed, r.random_speed, r.jockey_skill, r.luck_factor, r.placement, " +
                "h.horse_name, h.min_speed, h.max_speed, h.pic_path, " +
                "j.jockey_name, j.sex, j.height, j.skill " +
                "FROM races r, horses h, jockeys j " +
                "WHERE h.horse_ID = r.horse_ID AND j.jockey_ID = r.jockey_ID";
        max_id = "SELECT race_ID FROM races WHERE race_ID = (SELECT MAX(race_ID) FROM races)";

        try {
            con = DatabaseConnector.getConnection();
        } catch (SQLException e) {
            logger.error("SQL-Exception in Constructor (establishing connection to DB)");
            throw new PersistenceException("Error trying to connect to database.");
        }
    }

    /**
     * Creates an entry in the database with the race objects
     *
     * @param races list of Race-Objects to be written into database
     * @return the id of the race
     * @throws PersistenceException
     */
    public int create(List<Race> races) throws PersistenceException {

        if (races == null || races.isEmpty()) {
            logger.error("INVALID parameter => list holds no elements");
            throw new IllegalArgumentException("INVALID parameter => list holds no elements");
        }
        logger.info(races.size() + " races are to be saved.");

        int new_ID = -1;
        Race debug_race = null;

        try {
            Stmt_max_id = con.prepareStatement(max_id);
            Stmt_create = con.prepareStatement(create);

            ResultSet result_ID = Stmt_max_id.executeQuery();

            if (result_ID.next()) {
                new_ID = result_ID.getInt("race_ID") + 1;
                logger.info("race_ID set as " + new_ID);
            } else {
                logger.info("first race to be saved, set race_ID to 1.");
                new_ID = 1;
            }

            if (new_ID <= 0) {
                logger.error("ERROR reading race_ID -> abort.");
                throw new PersistenceException("ERROR reading race_ID: " + new_ID);
            }

            for (Race r : races) {
                debug_race = r;
                Stmt_create.setInt(1, new_ID);
                Stmt_create.setInt(2, r.getHorse().getId());
                Stmt_create.setInt(3, r.getJockey().getId());
                Stmt_create.setDouble(4, r.getAverageSpeed());
                Stmt_create.setDouble(5, r.getRandomSpeed());
                Stmt_create.setDouble(6, r.getJockeySkill());
                Stmt_create.setDouble(7, r.getLuckFactor());
                Stmt_create.setInt(8, r.getPlacement());
                Stmt_create.executeUpdate();
                Stmt_create.clearParameters();
                logger.info("SAVE SUCCESSFUL Race_ID " + new_ID + ": " + r.toString());
            }

            if (Stmt_create != null) { Stmt_create.close(); }

        } catch (SQLException e) {
            logger.error("ERROR while saving " + debug_race.toString());
            throw new PersistenceException("ERROR while saving " + debug_race.toString());
        }

        logger.info(races.size() + " races were saved.");
        return new_ID;
    }

    /**
     * List all races currently in database
     *
     * @return list containing all races found
     * @throws PersistenceException
     */
    @Override
    public List<Race> read() throws PersistenceException {
        logger.info("-> Read all elements in table 'races");

        List<Race> races = new ArrayList<Race>();

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

                Jockey j = new Jockey();
                j.setId(result.getInt("jockey_ID"));
                j.setName(result.getString("jockey_name"));
                j.setSex(result.getString("sex"));
                j.setHeight(result.getInt("height"));
                j.setSkill(result.getDouble("skill"));

                Race r = new Race();
                r.setHorse(h);
                r.setJockey(j);
                r.setRaceID(result.getInt("race_ID"));
                r.setAverageSpeed(result.getDouble("average_speed"));
                r.setRandomSpeed(result.getDouble("random_speed"));
                r.setJockeySkill(result.getDouble("jockey_skill"));
                r.setLuckFactor(result.getDouble("luck_factor"));
                r.setPlacement(result.getInt("placement"));

                races.add(r);
            }

            if (Stmt_read != null) { Stmt_read.close(); }

        } catch (SQLException e) {
            logger.error("ERROR while trying to read all elements");
            throw new PersistenceException("ERROR while trying to read all elements");
        }

        logger.info("-> # of elements found in table: " + races.size());
        return races;
    }
}
