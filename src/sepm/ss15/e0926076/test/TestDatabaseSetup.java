package sepm.ss15.e0926076.test;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Sebastian on 19.03.2015.
 */
public class TestDatabaseSetup {
    public static void setupHorseTestDB(Connection connection) throws SQLException {
        connection.prepareStatement("DELETE FROM races").execute();
        connection.prepareStatement("DELETE FROM horses").execute();

        connection.prepareStatement("INSERT INTO horses(horse_name, min_speed, max_speed, is_deleted, pic_path) VALUES ('Anton', 50, 100, 0, 'resources/horse1.JPG')").execute();
        connection.prepareStatement("INSERT INTO horses(horse_name, min_speed, max_speed, is_deleted, pic_path) VALUES ('Berta', 55, 100, 0, 'resources/horse2.JPG')").execute();
        connection.prepareStatement("INSERT INTO horses(horse_name, min_speed, max_speed, is_deleted, pic_path) VALUES ('Caesar', 60, 100, 0, 'resources/horse3.JPG')").execute();
        connection.prepareStatement("INSERT INTO horses(horse_name, min_speed, max_speed, is_deleted, pic_path) VALUES ('Charly', 65, 100, 0, 'resources/horse4.JPG')").execute();
        connection.prepareStatement("INSERT INTO horses(horse_name, min_speed, max_speed, is_deleted, pic_path) VALUES ('Dora', 70, 100, 0, 'resources/horse5.JPG')").execute();
        connection.prepareStatement("INSERT INTO horses(horse_name, min_speed, max_speed, is_deleted, pic_path) VALUES ('Emil', 75, 100, 0, 'resources/horse6.JPG')").execute();
        connection.prepareStatement("INSERT INTO horses(horse_name, min_speed, max_speed, is_deleted, pic_path) VALUES ('Friedrich', 80, 100, 0, 'resources/horse7.JPG')").execute();
        connection.prepareStatement("INSERT INTO horses(horse_name, min_speed, max_speed, is_deleted, pic_path) VALUES ('Gustav', 85, 100, 0, 'resources/horse8.JPG')").execute();
        connection.prepareStatement("INSERT INTO horses(horse_name, min_speed, max_speed, is_deleted, pic_path) VALUES ('Heinrich', 90, 100, 0, 'resources/horse9.JPG')").execute();
        connection.prepareStatement("INSERT INTO horses(horse_name, min_speed, max_speed, is_deleted, pic_path) VALUES ('Ida', 95, 100, 0, 'resources/horse10.JPG')").execute();
    }

    public static void setupJockeyTestDB(Connection connection) throws SQLException {
        connection.prepareStatement("DELETE FROM races").execute();
        connection.prepareStatement("DELETE FROM jockeys").execute();

        connection.prepareStatement("INSERT INTO jockeys(jockey_name, sex, height, skill, is_deleted) VALUES ('Julia', 'w', 110, 0.0, 0)").execute();
        connection.prepareStatement("INSERT INTO jockeys(jockey_name, sex, height, skill, is_deleted) VALUES ('Julius', 'm', 120, 10.0, 0)").execute();
        connection.prepareStatement("INSERT INTO jockeys(jockey_name, sex, height, skill, is_deleted) VALUES ('Ludwig', 'm', 130, 20.0, 0)").execute();
        connection.prepareStatement("INSERT INTO jockeys(jockey_name, sex, height, skill, is_deleted) VALUES ('Niklas', 'm', 140, 30.9, 0)").execute();
        connection.prepareStatement("INSERT INTO jockeys(jockey_name, sex, height, skill, is_deleted) VALUES ('Otto', 'm', 150, 40.8, 0)").execute();
        connection.prepareStatement("INSERT INTO jockeys(jockey_name, sex, height, skill, is_deleted) VALUES ('Marie', 'w', 160, 50.7, 0)").execute();
        connection.prepareStatement("INSERT INTO jockeys(jockey_name, sex, height, skill, is_deleted) VALUES ('Rosa', 'w', 170, 60.7, 0)").execute();
        connection.prepareStatement("INSERT INTO jockeys(jockey_name, sex, height, skill, is_deleted) VALUES ('Sophie', 'w', 180, 70.6, 0)").execute();
        connection.prepareStatement("INSERT INTO jockeys(jockey_name, sex, height, skill, is_deleted) VALUES ('Ulrike', 'w', 190, 80.5, 0)").execute();
        connection.prepareStatement("INSERT INTO jockeys(jockey_name, sex, height, skill, is_deleted) VALUES ('Valencia', 'w', 200, 90.5, 0)").execute();
    }

    public static void setupRaceTestDB(Connection connection) throws SQLException {
        setupHorseTestDB(connection);
        setupJockeyTestDB(connection);
    }
}
