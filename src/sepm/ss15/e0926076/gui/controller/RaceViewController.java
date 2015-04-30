package sepm.ss15.e0926076.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0926076.entities.Horse;
import sepm.ss15.e0926076.entities.Jockey;
import sepm.ss15.e0926076.entities.Race;
import sepm.ss15.e0926076.gui.wrapper.RaceWrapper;
import sepm.ss15.e0926076.service.ServiceException;
import sepm.ss15.e0926076.service.SimpleService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Sebastian on 25.03.2015.
 *
 * Controller for the RaceView, handling showing and simulating races
 */
public class RaceViewController implements Initializable {

    private static final Logger logger = LogManager.getLogger(RaceViewController.class);
    private SimulationViewController simu_controller;

    private static ObservableList<RaceWrapper> race_data = FXCollections.observableArrayList();

    @FXML
    private TableView<RaceWrapper> race_table;
    @FXML
    private TableColumn<RaceWrapper, Integer> col_race_id;
    @FXML
    private TableColumn<RaceWrapper, Integer> col_race_horse_id;
    @FXML
    private TableColumn<RaceWrapper, String> col_race_horse_name;
    @FXML
    private TableColumn<RaceWrapper, Double> col_race_avg_speed;
    @FXML
    private TableColumn<RaceWrapper, Double> col_race_rand_speed;
    @FXML
    private TableColumn<RaceWrapper, Integer> col_race_jockey_id;
    @FXML
    private TableColumn<RaceWrapper, String> col_race_jockey_name;
    @FXML
    private TableColumn<RaceWrapper, Double> col_race_jockey_skill;
    @FXML
    private TableColumn<RaceWrapper, Double> col_race_luck;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // prefill the view with all Races currently in database
        try {
            List<Race> races = new SimpleService().readRaces();
            List<RaceWrapper> wrappers = new ArrayList<RaceWrapper>();

            for (Race r : races) {
                wrappers.add(new RaceWrapper(r));
            }

            race_data.addAll(wrappers);
        } catch (ServiceException e) {

        }
        race_table.setItems(race_data);

        col_race_id.setCellValueFactory(cell_data -> cell_data.getValue().raceIDProperty().asObject());
        col_race_horse_id.setCellValueFactory(cell_data -> cell_data.getValue().getHorse().horseIDProperty().asObject());
        col_race_horse_name.setCellValueFactory(cell_data -> cell_data.getValue().getHorse().horseNameProperty());
        col_race_avg_speed.setCellValueFactory(cell_data -> cell_data.getValue().averageSpeedProperty().asObject());
        col_race_rand_speed.setCellValueFactory(cell_data -> cell_data.getValue().randomSpeedProperty().asObject());
        col_race_jockey_id.setCellValueFactory(cell_data -> cell_data.getValue().getJockey().jockeyIDProperty().asObject());
        col_race_jockey_name.setCellValueFactory(cell_data -> cell_data.getValue().getJockey().nameProperty());
        col_race_jockey_skill.setCellValueFactory(cell_data -> cell_data.getValue().jockeySkillProperty().asObject());
        col_race_luck.setCellValueFactory(cell_data -> cell_data.getValue().luckFactorProperty().asObject());

        simu_controller = new SimulationViewController();
    }

    /**
     * Handle the event in which the Show Races button was clicked, opens a dialog in where to choose whether to list
     * all races or all races of a certain horse/jockey or all races with a certain ID
     */
    @FXML
    private void listRacesClicked() {
        logger.info("btn SHOW clicked");

        List<Race> races;
        List<RaceWrapper> wrappers = new ArrayList<>();

        String result = AlertsAndWarnings.confirmRaceListDialog();
        logger.info("ID and entity to look for: " + result);

        // cancelled
        if (result.equals("CANCEL") || result.equals("ERROR")) {
            return;
        }
        // list all
        else if (result.length() == 0) {
            try {
                races = new SimpleService().readRaces();

                for (Race r : races) {
                    wrappers.add(new RaceWrapper(r));
                }
                race_data.setAll(wrappers);
            } catch (ServiceException e) {
                AlertsAndWarnings.exceptionWarningAlert(e);
            }
        }
        // list by horse
        else if (result.contains("horse")) {
            try {
                int h_id = Integer.parseInt(result.substring(("horse").length()));
                Horse h = new Horse();
                h.setId(h_id);

                races = new SimpleService().readRaces(h);

                for (Race r : races) {
                    wrappers.add(new RaceWrapper(r));
                }
                race_data.setAll(wrappers);
            } catch (NumberFormatException e) {
                AlertsAndWarnings.inputErrorAlert("Invalid Input", null, "Please try again with a valid ID (real positive integer).");
            } catch (ServiceException e) {
                AlertsAndWarnings.exceptionWarningAlert(e);
            }
        }
        // list by jockey
        else if (result.contains("jockey")) {
            try {
                int j_id = Integer.parseInt(result.substring(("jockey").length()));
                Jockey j = new Jockey();
                j.setId(j_id);

                races = new SimpleService().readRaces(j);

                for (Race r : races) {
                    wrappers.add(new RaceWrapper(r));
                }
                race_data.setAll(wrappers);
            } catch (NumberFormatException e) {
                AlertsAndWarnings.inputErrorAlert("Invalid Input", null, "Please try again with a valid ID (real positive integer).");
            } catch (ServiceException e) {
                AlertsAndWarnings.exceptionWarningAlert(e);
            }
        }
        // list by raceID
        else if (result.contains("race")) {
            try {
                int r_id = Integer.parseInt(result.substring(("race").length()));

                races = new SimpleService().readRaces(r_id);

                for (Race r : races) {
                    wrappers.add(new RaceWrapper(r));
                }
                race_data.setAll(wrappers);
            } catch (NumberFormatException e) {
                AlertsAndWarnings.inputErrorAlert("Invalid Input", null, "Please try again with a valid ID (real positive integer).");
            } catch (ServiceException e) {
                AlertsAndWarnings.exceptionWarningAlert(e);
            }
        } else {
            return;
        }
    }


    public static void setRaceData(List<RaceWrapper> races) {
        race_data.setAll(races);
    }

    /**
     * Handle the event in which the show Statistics button was clicked, the Statistics are shown as a PieChart
     */
    @FXML
    private void showStatisticsClicked() {

        int selected_index = race_table.getSelectionModel().getSelectedIndex();

        if (selected_index >= 0) {

            Map<Integer, Integer> map = null;

            try {
                map = new SimpleService().getStatistics(race_table.getItems().get(selected_index).getHorse().getHorse(), race_table.getItems().get(selected_index).getJockey().getJockey());

                ObservableList<PieChart.Data> pie_chart_data = FXCollections.observableArrayList();
                for (Integer i : map.keySet()) {
                    pie_chart_data.add(new PieChart.Data(i +
                            (i % 10 == 1 ? (i % 11 != 0 ? "st" : "th") : (i % 10 == 2 ? "nd" : (i % 10 == 3 ? "rd" : "th"))) +
                            "( #"+map.get(i) +")", map.get(i)));
                }
                StatisticsViewController.setData(pie_chart_data);

                /*
                 *  start the new stage showing the pie chart
                 */
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/sepm/ss15/e0926076/gui/view/StatisticsView.fxml"));

                AnchorPane page = (AnchorPane) loader.load();
                Stage stage = new Stage();
                stage.setTitle("Placement Statistic for Horse");
                stage.initModality(Modality.WINDOW_MODAL);
                stage.setWidth(500);
                stage.setHeight(500);
                stage.initOwner(Main.getPrimaryStage());

                Scene scene = new Scene(page);
                stage.setScene(scene);

                StatisticsViewController.setStage(stage);

                stage.show();

            } catch (ServiceException e) {
                AlertsAndWarnings.exceptionWarningAlert(e);
            } catch (IOException e) {
                AlertsAndWarnings.exceptionWarningAlert(e);
            }
        } else {
            AlertsAndWarnings.noSelectionAlert("Team");
        }
    }

    /**
     * Context Menu Show Statistics
     */
    @FXML
    private void contextShowStatistics() {
        showStatisticsClicked();
    }
}