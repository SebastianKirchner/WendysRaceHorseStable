package sepm.ss15.e0926076.gui.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0926076.persistence.DatabaseConnector;

import java.sql.SQLException;

public class Main extends Application {

    private static final Logger logger = LogManager.getLogger(Main.class);

    private static Stage primary_stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            this.primary_stage = primaryStage;
            Parent root = FXMLLoader.load(getClass().getResource("/sepm/ss15/e0926076/gui/view/Main.fxml"));
            Scene scene = new Scene(root);
            primary_stage.setTitle("Wendys Race Horse Stable");
            primary_stage.setScene(scene);
            primary_stage.setResizable(false);
            primary_stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
            return primary_stage;
    }

    public static void main(String[] args) {


       /* Result result = JUnitCore.runClasses(SimpleServiceTest.class);
        for (Failure f : result.getFailures()) {
            logger.info(f.toString());
        }*/


      /* try {
            TestDatabaseSetup.setupHorseTestDB(DatabaseConnector.getConnection());
            TestDatabaseSetup.setupJockeyTestDB(DatabaseConnector.getConnection());
        } catch (SQLException e) {

        }*/


        launch(args);

        try {
            DatabaseConnector.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
