package sepm.ss15.e0926076.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0926076.entities.Jockey;
import sepm.ss15.e0926076.gui.wrapper.JockeyWrapper;
import sepm.ss15.e0926076.service.ServiceException;
import sepm.ss15.e0926076.service.SimpleService;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by Sebastian on 24.03.2015.
 *
 * Controller for the JockeyView, handling create, update, delete and list operations
 */
public class JockeyViewController implements Initializable {

    private static final Logger logger = LogManager.getLogger(JockeyViewController.class);
    private static final String ENTITY = "Jockey";

    private SimulationViewController simu_controller;

    private ObservableList<JockeyWrapper> jockey_data = FXCollections.observableArrayList();

    @FXML private TableView<JockeyWrapper> jockey_table;
    @FXML private TableColumn<JockeyWrapper, Integer> col_id;
    @FXML private TableColumn<JockeyWrapper, String> col_name;
    @FXML private TableColumn<JockeyWrapper, String> col_sex;
    @FXML private TableColumn<JockeyWrapper, Integer> col_height;
    @FXML private TableColumn<JockeyWrapper, Double> col_skill;

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

        try {
            List<Jockey> jockeys = new SimpleService().readJockeys();
            List<JockeyWrapper> wrappers = new ArrayList<JockeyWrapper>();

            for (Jockey h: jockeys) {
                wrappers.add(new JockeyWrapper(h));
            }

            jockey_data.addAll(wrappers);
        } catch (ServiceException e) {

        }
        jockey_table.setItems(jockey_data);

        col_id.setCellValueFactory(cell_data -> cell_data.getValue().jockeyIDProperty().asObject());
        col_name.setCellValueFactory(cell_data -> cell_data.getValue().nameProperty());
        col_sex.setCellValueFactory(cell_data -> cell_data.getValue().sexProperty());
        col_height.setCellValueFactory(cell_data -> cell_data.getValue().heightProperty().asObject());
        col_skill.setCellValueFactory(cell_data -> cell_data.getValue().skillProperty().asObject());

        simu_controller = new SimulationViewController();
    }

    /**
     * Handle the event in which the button was pressed to create a new Jockey element, shows a new Stage
     */
    @FXML
    private void newJockeyClicked() {
        logger.info("btn NEW clicked");

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sepm/ss15/e0926076/gui/view/JockeyEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialog_stage = new Stage();
            dialog_stage.setTitle("New Jockey");
            dialog_stage.initModality(Modality.WINDOW_MODAL);
            dialog_stage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(page);
            dialog_stage.setScene(scene);

            JockeyEditDialogController controller = loader.getController();
            controller.setDialogStage(dialog_stage);

            dialog_stage.showAndWait();

            // OK button was pressed, save it in database
            if (controller.isOKClicked()) {
                Jockey saved = new SimpleService().createJockey(controller.getJockeyWrapper().getJockey());
                if (saved != null) {
                    jockey_data.add( new JockeyWrapper(saved));
                }
            }
        } catch (Exception e) {
            AlertsAndWarnings.exceptionWarningAlert(e);
        }
    }

    /**
     * Handle the event in which the button was pressed to edit an existing Jockey element, loads same stage as
     * newJockeyClicked() but prefills it with the Jockey parameters
     */
    @FXML
    private void editJockeyClicked() {
        logger.info("btn EDIT clicked");

        int selected_index = jockey_table.getSelectionModel().getSelectedIndex();

        if (selected_index >= 0) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/sepm/ss15/e0926076/gui/view/JockeyEditDialog.fxml"));
                AnchorPane page = (AnchorPane) loader.load();

                Stage dialog_stage = new Stage();
                dialog_stage.setTitle("Edit Jockey");
                dialog_stage.initModality(Modality.WINDOW_MODAL);
                dialog_stage.initOwner(Main.getPrimaryStage());
                Scene scene = new Scene(page);
                dialog_stage.setScene(scene);

                JockeyEditDialogController controller = loader.getController();
                controller.setDialogStage(dialog_stage);
                controller.setJockey(jockey_table.getItems().get(selected_index));

                dialog_stage.showAndWait();

                // OK button was pressed, save updated parameters in database
                if (controller.isOKClicked() && controller.getJockeyWrapper().getId() > 0) {
                    Jockey updated = new SimpleService().updateJockey(controller.getJockeyWrapper().getJockey());
                    jockey_data.remove(selected_index);
                    jockey_data.add( new JockeyWrapper(updated));
                }
            } catch (Exception e) {
                AlertsAndWarnings.exceptionWarningAlert(e);
            }
        } else {
            AlertsAndWarnings.noSelectionAlert(ENTITY);
        }
    }

    /**
     * Handle the event in which the delete button was pressed
     */
    @FXML
    private void deleteJockeyClicked() {
        logger.info("btn DELETE clicked");

        int selected_index = jockey_table.getSelectionModel().getSelectedIndex();

        if (selected_index >= 0) {
            Optional<ButtonType> result = AlertsAndWarnings.confirmDeleteAlert("Confirm Action",
                    "Please confirm:", "Are you sure you want to delete this " + ENTITY + "?");

            if (result.get() == ButtonType.OK) {
                logger.info("delete confirmed.");
                try {
                    new SimpleService().deleteJockey((Jockey) jockey_table.getItems().get(selected_index));
                    jockey_table.getItems().remove(selected_index);
                } catch (ServiceException e) {
                    AlertsAndWarnings.exceptionWarningAlert(e);
                }
            } else {
                logger.info("delete cancelled.");
                return;
            }
        } else {
            AlertsAndWarnings.noSelectionAlert(ENTITY);
        }
    }

    /**
     * Handle the event in which the Show Jockeys button was clicked, opens a dialog in where to choose whether to list
     * all jockeys or only jockeys with a certain name
     */
    @FXML
    private void listJockeysClicked() {
        logger.info("btn LIST clicked");

        List<Jockey> jockeys;
        List<JockeyWrapper> wrappers = new ArrayList<JockeyWrapper>();

        String name = AlertsAndWarnings.confirmListingOptionDialog(ENTITY);
        logger.info("name to look for: " + name);

        // cancelled
        if (name.equals("CANCEL") || name.equals("ERROR")) {
            return;
        }
        // list all
        else if (name.length() == 0) {
            try {
                jockeys = new SimpleService().readJockeys();

                for (Jockey h:jockeys) {
                    wrappers.add(new JockeyWrapper(h));
                }
                jockey_data.removeAll();
                jockey_data.setAll(wrappers);
            } catch (ServiceException e) {
                AlertsAndWarnings.exceptionWarningAlert(e);
            }
        }
        // list by name
        else if (name.length() > 3 && name.length() <= 20) {
            try {
                jockeys = new SimpleService().readJockeys(name);
                jockey_data.removeAll();
                for (Jockey h: jockeys) {
                    wrappers.add(new JockeyWrapper(h));
                }
                jockey_data.setAll(wrappers);

            } catch (ServiceException e) {
                AlertsAndWarnings.exceptionWarningAlert(e);
            }
        } else {
            return;
        }
    }

    /**
     * Handle the event in which the show Statistics button was clicked, the Statistics are shown as a PieChart
     */
    @FXML
    private void jockeyStatisticsClicked() {
        logger.info("btn STATISTICS clicked");

        int selected_index = jockey_table.getSelectionModel().getSelectedIndex();
        if (selected_index >= 0) {

            Map<Integer, Integer> map = null;
            try {
                map = new SimpleService().getStatistics(jockey_table.getItems().get(selected_index).getJockey());

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
                stage.setTitle("Placement Statistic for Jockey");
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
            logger.info("show statistics of selected entry");
        } else {
            AlertsAndWarnings.noSelectionAlert(ENTITY);
        }
    }

    /**
     * ContextMenu adds the selected Jockey to the current simulation
     */
    @FXML
    private void contextToRace() {
        logger.info("context PROFILE");

        int selected_index = jockey_table.getSelectionModel().getSelectedIndex();
        if (selected_index >= 0) {
            logger.info("show profile of selected entry");
            simu_controller.addJockeyToRace(jockey_table.getItems().get(selected_index));
        } else {
            AlertsAndWarnings.noSelectionAlert(ENTITY);
        }

    }

    /**
     * Context Menu Edit Horse
     */
    @FXML
    private void contextEditJockey() {
        logger.info("context EDIT");
        editJockeyClicked();
    }

    /**
     * Context Menu Delete Horse
     */
    @FXML
    private void contextDeleteJockey() {
        logger.info("context DELETE");
        deleteJockeyClicked();
    }

    /**
     * Context Menu Show Statistics
     */
    @FXML
    private void contextShowStatistic() {
        jockeyStatisticsClicked();
    }

}
