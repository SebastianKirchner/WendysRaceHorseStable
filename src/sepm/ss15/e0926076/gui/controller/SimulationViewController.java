package sepm.ss15.e0926076.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0926076.entities.Race;
import sepm.ss15.e0926076.gui.wrapper.HorseWrapper;
import sepm.ss15.e0926076.gui.wrapper.JockeyWrapper;
import sepm.ss15.e0926076.gui.wrapper.RaceWrapper;
import sepm.ss15.e0926076.service.ServiceException;
import sepm.ss15.e0926076.service.SimpleService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Sebastian on 29.03.2015.
 */
public class SimulationViewController implements Initializable{

    private static final Logger logger = LogManager.getLogger(SimulationViewController.class);

    private static Stage simu_view_stage = null;

    private static ObservableList<HorseWrapper> horses = FXCollections.observableArrayList();
    private static ObservableList<JockeyWrapper> jockeys = FXCollections.observableArrayList();
    private static ObservableList<RaceWrapper> teams = FXCollections.observableArrayList();

    @FXML private TableView<HorseWrapper> horse_table;
    @FXML private TableView<JockeyWrapper> jockey_table;
    @FXML private TableView<RaceWrapper> team_table;

    @FXML private TableColumn<RaceWrapper, Integer> col_team_horse;
    @FXML private TableColumn<RaceWrapper, Integer> col_team_jockey;


    @FXML private TableColumn<HorseWrapper, Integer> col_h_id;
    @FXML private TableColumn<HorseWrapper, Integer> col_h_min_speed;
    @FXML private TableColumn<HorseWrapper, Integer> col_h_max_speed;

    @FXML private TableColumn<JockeyWrapper, Integer> col_j_id;
    @FXML private TableColumn<JockeyWrapper, Integer> col_j_height;
    @FXML private TableColumn<JockeyWrapper, Double> col_j_skill;

    public SimulationViewController() {

    }

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

        horse_table.setItems(horses);
        jockey_table.setItems(jockeys);
        team_table.setItems(teams);

        col_team_horse.setCellValueFactory(cell_data -> cell_data.getValue().getHorse().horseIDProperty().asObject());
        col_team_jockey.setCellValueFactory(cell_data -> cell_data.getValue().getJockey().jockeyIDProperty().asObject());

        col_h_id.setCellValueFactory(cell_data -> cell_data.getValue().horseIDProperty().asObject());
        col_h_min_speed.setCellValueFactory(cell_data -> cell_data.getValue().horseMinSpeedProperty().asObject());
        col_h_max_speed.setCellValueFactory(cell_data -> cell_data.getValue().horseMaxSpeedProperty().asObject());

        col_j_id.setCellValueFactory(cell_data -> cell_data.getValue().jockeyIDProperty().asObject());
        col_j_skill.setCellValueFactory(cell_data -> cell_data.getValue().skillProperty().asObject());
        col_j_height.setCellValueFactory(cell_data -> cell_data.getValue().heightProperty().asObject());
    }

    /**
     * Add a Horse to the current Simulation. If the Horse is already part of the Simulation, an
     * Error DIalog is shown.
     *
     * @param h Horse that is to be added to the simulation
     * @return true if the horse was added
     */
    public boolean addHorseToRace(HorseWrapper h) {
        logger.info("horse added to race");

        boolean ret = false;

        if (!horses.contains(h)) {
            ret =  horses.add(h);
            setSimulationStage().show();
        } else {
            AlertsAndWarnings.inputErrorAlert("Invalid Action", "Each Horse can only take part in the same race once.",
                    "Yo tried to add a Horse that has already been added to the race");
        }

        return ret;
    }

    /**
     * Add a Jockey to the current Simulation. If the Jockey is already part of the Simulation, an
     * Error DIalog is shown.
     *
     * @param j Jockey that is to be added to the simulation
     * @return true if the jockey was added
     */
    public boolean addJockeyToRace(JockeyWrapper j) {
        logger.info("jockey added to race");

        boolean ret = false;

        if (!jockeys.contains(j)) {
            ret = jockeys.add(j);
            setSimulationStage().show();
        } else {
            AlertsAndWarnings.inputErrorAlert("Invalid Action", "Each Jockey can only take part in the same race once.",
                    "You tried to add a Jockey that has already been added to the race");
        }

        return false;
    }

    public List<RaceWrapper> getRaces() {
        return teams;
    }

    /**
     * Clear the Data of Horses and Jockeys. Is called after the Simulation is started.
     */
    public static void clearTeams() {
        horses.clear();
        jockeys.clear();
        teams.clear();
    }

    /**
     * Teams up a Horse and a Jockey to form a Race Object. Each Horse and Jockey can only take part in
     * one Simulation once.
     */
    @FXML
    private void teamUpClicked() {
        logger.info("team up clicked");

        int selected_horse = horse_table.getSelectionModel().getSelectedIndex();
        int selected_jockey = jockey_table.getSelectionModel().getSelectedIndex();

        if (selected_horse >= 0 && selected_jockey >= 0 ) {
            HorseWrapper horse = new HorseWrapper(horse_table.getItems().get(selected_horse).getHorse());
            JockeyWrapper jockey = new JockeyWrapper(jockey_table.getItems().get(selected_jockey).getJockey());

            for (RaceWrapper r: teams) {
                if (r.getHorse().equals(horse)) {
                    AlertsAndWarnings.inputErrorAlert("Invalid Action", "Each horse can only take part in the same race once.",
                            "You tried to make a team with a horse that is already part of a team.");
                    return;
                }

                if (r.getJockey().equals(jockey)) {
                    AlertsAndWarnings.inputErrorAlert("Invalid Action", "Each jockey can only take part in the same race once.",
                            "You tried to make a team with a jockey that is already part of a team.");
                    return;
                }
            }

            Race r = new Race();
            r.setJockey(jockey.getJockey());
            r.setHorse(horse.getHorse());
            RaceWrapper race = new RaceWrapper(r);
            race.setHorseWrapper(horse);
            race.setJockey(jockey);

            teams.add(race);
            horses.remove(selected_horse);
            jockeys.remove(selected_jockey);
            logger.info("# of teams: " + teams.size());

        } else {
            AlertsAndWarnings.inputErrorAlert("Invalid Action", "A team consists of one jockey and one horse.",
                    "You tried to add up " + (selected_horse >= 0 ? 1:0) + " horse(s) with " + (selected_jockey >= 0 ? 1 : 0) + " jockey(s).");
        }
    }

    /**
     * Start the Simulation with the current teams, set the data in RaceViewController.
     */
    @FXML
    private void startSimulationStarted() {
        logger.info("btn SIMULATE clicked");

        if (getRaces() == null || getRaces().isEmpty()) {
            AlertsAndWarnings.inputErrorAlert("Race Simulation Error", null, "There are no teams chosen to participate in the race, " +
                    "please choose teams first.");
            return;
        }
        RaceViewController.setRaceData(getRaces());

        List<Race> races = new ArrayList<>();
        List<Race> result;
        List<RaceWrapper> wrappers = new ArrayList<>();

        for (RaceWrapper rw : getRaces()) {
            races.add(rw.getRace());
        }

        /*
         * Simulate the race and set the data for the RaceView
         */
        try {
            result = new SimpleService().simulateRace(races);

            for (Race r : result) {
                wrappers.add(new RaceWrapper(r));
            }
            RaceViewController.setRaceData(wrappers);
        } catch (ServiceException e) {
            AlertsAndWarnings.exceptionWarningAlert(e);
        }
        clearTeams();
    }

    /**
     * Delete a Team from the Simulation.
     */
    @FXML
    private void deleteTeamClicked() {

        int selected_team = team_table.getSelectionModel().getSelectedIndex();

        RaceWrapper to_delete = null;

        boolean found = false;

        if (selected_team >= 0) {
            to_delete = team_table.getItems().get(selected_team);

            Optional<ButtonType> result = AlertsAndWarnings.confirmDeleteAlert("Confirm Action", "You are trying to delete the following team: "
                            + "Horse " + to_delete.getHorse().getId() + " (" + to_delete.getHorse().getName() + ") and Jockey " + to_delete.getJockey().getId() + " (" + to_delete.getJockey().getName() + ").", "Are you sure you want " +
                            "to remove this team from the simulation?");
            if (result.get() == ButtonType.OK) {
                teams.remove(to_delete);
            } else {
                return;
            }

        } else {
            AlertsAndWarnings.noSelectionAlert("team");
        }
    }

    /**
     * Delete a Horse from the current Simulation, if it is in a Team, only the Horse will be removed,
     * but not the Jockey.
     */
    @FXML
    private void contextDeleteHorse() {
        int selected_horse = horse_table.getSelectionModel().getSelectedIndex();

        if (selected_horse >= 0) {
            HorseWrapper hw = horse_table.getItems().get(selected_horse);
            RaceWrapper rw = inRace(hw);

            if (rw != null) {
                Optional<ButtonType> result = AlertsAndWarnings.confirmDeleteAlert("Confirm Action", "You are trying to delete a horse (" +
                        hw.getId() + "/" +hw.getName() + ") which is currently in a team with the jockey: " + rw.getJockey().getId() +
                        " (" + rw.getJockey().getName() + ").",  "Are you sure you want to remove this horse from the simulation?");

                if (result.get() == ButtonType.OK) {
                    teams.remove(rw);
                    horses.remove(hw);
                }
            } else {
                Optional<ButtonType> result = AlertsAndWarnings.confirmDeleteAlert("Confirm Action",
                        "Please confirm:", "Are you sure you want to delete this horse?");

                if (result.get() == ButtonType.OK) {
                    horses.remove(hw);
                }
            }
        } else {
            AlertsAndWarnings.noSelectionAlert("Horse");
        }
    }

    /**
     * Delete a Jockey from the current Simulation, if it is in a Team, only the Jockey will be removed,
     * but not the Horse.
     */
    @FXML
    private void contextDeleteJockey() {
        int selected_jockey = jockey_table.getSelectionModel().getSelectedIndex();

        if (selected_jockey >= 0) {
            JockeyWrapper jw = jockey_table.getItems().get(selected_jockey);
            RaceWrapper rw = inRace(jw);

            if (rw != null) {
                Optional<ButtonType> result = AlertsAndWarnings.confirmDeleteAlert("Confirm Action", "You are trying to delete a jockey (" +
                        jw.getId() + "/" + jw.getName() + ") which is currently in a team with the horse: " + rw.getHorse().getId() +
                        " (" + rw.getHorse().getName() + ").", "Are you sure you want to remove this jockey from the simulation?");

                if (result.get() == ButtonType.OK) {
                    teams.remove(rw);
                    jockeys.remove(jw);
                }
            } else {
                Optional<ButtonType> result = AlertsAndWarnings.confirmDeleteAlert("Confirm Action",
                        "Please confirm:", "Are you sure you want to delete this jockey?");

                if (result.get() == ButtonType.OK) {
                    jockeys.remove(jw);
                }
            }
        } else {
            AlertsAndWarnings.noSelectionAlert("Jockey");
        }
    }

    /**
     *
     * @param h Horse of which to check in which Race it is
     * @return the Race which the Horse is part of
     */
    private RaceWrapper inRace(HorseWrapper h) {
        for (RaceWrapper r : teams) {
            if (h.getId().equals( r.getHorse().getId())) { return r; }
        }
        return null;
    }

    /**
     *
     * @param j Jockey of which to check in which Race it is
     * @return the Race which the Jockey is part of
     */
    private RaceWrapper inRace(JockeyWrapper j) {
        for (RaceWrapper r : teams) {
            if (j.getId().equals(r.getJockey().getId())) { return r; }
        }
        return null;
    }

    /**
     * Set the SimulationStage, if it already exists return it.
     * @return
     */
    private Stage setSimulationStage() {

        if (simu_view_stage == null) {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sepm/ss15/e0926076/gui/view/SimulationView.fxml"));

            try {
                AnchorPane page = loader.load();

                simu_view_stage = new Stage();
                simu_view_stage.setTitle("Current Simulation");
                simu_view_stage.initModality(Modality.NONE);
                simu_view_stage.initOwner(Main.getPrimaryStage());
                simu_view_stage.setResizable(true);

                Scene scene = new Scene(page);
                simu_view_stage.setScene(scene);

                return simu_view_stage;

            } catch (Exception e) {
                AlertsAndWarnings.exceptionWarningAlert(e);
                return null;
            }
        } else {
            return simu_view_stage;
        }
    }
}
