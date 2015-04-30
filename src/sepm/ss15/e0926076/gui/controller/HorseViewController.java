package sepm.ss15.e0926076.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0926076.entities.Horse;
import sepm.ss15.e0926076.gui.wrapper.HorseWrapper;
import sepm.ss15.e0926076.service.ServiceException;
import sepm.ss15.e0926076.service.SimpleService;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by Sebastian on 24.03.2015.
 *
 * Controller for the HorseView, handling create, update, delete and list operations
 */
public class HorseViewController implements Initializable {

    private static final Logger logger = LogManager.getLogger(HorseViewController.class);
    private final String ENTITY = "Horse";

    private SimulationViewController simu_controller;

    private ObservableList<HorseWrapper> horse_data = FXCollections.observableArrayList();

    @FXML
    private TableView<HorseWrapper> horse_table;
    @FXML
    private TableColumn<HorseWrapper, Integer> col_ID;
    @FXML
    private TableColumn<HorseWrapper, String> col_name;
    @FXML
    private TableColumn<HorseWrapper, Integer> col_min_speed;
    @FXML
    private TableColumn<HorseWrapper, Integer> col_max_speed;

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

        // prefill the view with all Horses currently in database
        try {
            List<Horse> horses = new SimpleService().readHorses();
            List<HorseWrapper> wrappers = new ArrayList<HorseWrapper>();

            for (Horse h : horses) {
                wrappers.add(new HorseWrapper(h));
            }

            horse_data.addAll(wrappers);
        } catch (ServiceException e) {
            // do nothing
        }
        horse_table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        horse_table.setItems(horse_data);

        // set the columns of the TableView
        col_ID.setCellValueFactory(cell_data -> cell_data.getValue().horseIDProperty().asObject());
        col_name.setCellValueFactory(cell_data -> cell_data.getValue().horseNameProperty());
        col_min_speed.setCellValueFactory(cell_data -> cell_data.getValue().horseMinSpeedProperty().asObject());
        col_max_speed.setCellValueFactory(cell_data -> cell_data.getValue().horseMaxSpeedProperty().asObject());

        simu_controller = new SimulationViewController();
    }

    /**
     * Handle the event in which the button was pressed to create a new Horse element, shows a new Stage
     */
    @FXML
    private void newHorseClicked() {
        logger.info("btn NEW clicked");

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sepm/ss15/e0926076/gui/view/HorseEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialog_stage = new Stage();
            dialog_stage.setTitle("New Horse");
            dialog_stage.initModality(Modality.WINDOW_MODAL);
            dialog_stage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(page);
            dialog_stage.setScene(scene);

            HorseEditDialogController controller = loader.getController();
            controller.setDialogStage(dialog_stage);

            dialog_stage.showAndWait();

            // OK button was pressed, save it in database
            if (controller.isOKClicked()) {
                Horse saved = new SimpleService().createHorse(controller.getHorseWrapper().getHorse());
                if (saved != null) {
                    horse_data.add(new HorseWrapper(saved));
                }
            }
        } catch (Exception e) {
            AlertsAndWarnings.exceptionWarningAlert(e);
        }
    }

    /**
     * Handle the event in which the button was pressed to edit an existing Horse element, loads same stage as
     * newHorseClicked() but prefills it with the Horse parameters
     */
    @FXML
    private void editHorseClicked() {
        logger.info("btn EDIT clicked");

        int selected_index = horse_table.getSelectionModel().getSelectedIndex();
        List<Integer> selected = horse_table.getSelectionModel().getSelectedIndices();

        logger.info("size of selected indices: " + selected.size());
        logger.info("indices:");
        for (Integer i : selected) {
            logger.info(i);
        }

        if (selected_index >= 0) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/sepm/ss15/e0926076/gui/view/HorseEditDialog.fxml"));
                AnchorPane page = (AnchorPane) loader.load();

                Stage dialog_stage = new Stage();
                dialog_stage.setTitle("Edit Horse");
                dialog_stage.initModality(Modality.WINDOW_MODAL);
                dialog_stage.initOwner(Main.getPrimaryStage());
                Scene scene = new Scene(page);
                dialog_stage.setScene(scene);

                HorseEditDialogController controller = loader.getController();
                controller.setDialogStage(dialog_stage);
                controller.setHorse(horse_table.getItems().get(selected_index));

                dialog_stage.showAndWait();

                // OK button was pressed, save updated parameters in database
                if (controller.isOKClicked() && controller.getHorseWrapper().getId() > 0) {
                    Horse updated = new SimpleService().updateHorse(controller.getHorseWrapper().getHorse());
                    horse_data.remove(selected_index);
                    horse_data.add(new HorseWrapper(updated));
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
    private void deleteHorseClicked() {
        logger.info("btn DELETE clicked");

        int selected_index = horse_table.getSelectionModel().getSelectedIndex();

        if (selected_index >= 0) {
            Optional<ButtonType> result = AlertsAndWarnings.confirmDeleteAlert("Confirm Action",
                    "Please confirm:", "Are you sure you want to delete this " + ENTITY + "?");

            if (result.get() == ButtonType.OK) {
                logger.info("delete confirmed.");
                try {
                    new SimpleService().deleteHorse( horse_table.getItems().get(selected_index).getHorse());
                    horse_table.getItems().remove(selected_index);
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
     * Handle the event in which the Show Horses button was clicked, opens a dialog in where to choose whether to list
     * all horses or only horses with a certain name
     */
    @FXML
    private void listHorsesClicked() {
        logger.info("btn LIST clicked");

        List<Horse> horses;
        List<HorseWrapper> wrappers = new ArrayList<>();

        String name = AlertsAndWarnings.confirmListingOptionDialog(ENTITY);
        logger.info("name to look for: " + name);

        // cancelled
        if (name.equals("CANCEL") || name.equals("ERROR")) {
            return;
        }
        // list all
        else if (name.length() == 0) {
            try {
                horses = new SimpleService().readHorses();

                for (Horse h : horses) {
                    wrappers.add(new HorseWrapper(h));
                }
                horse_data.setAll(wrappers);
            } catch (ServiceException e) {
                AlertsAndWarnings.exceptionWarningAlert(e);
            }
        }
        // list by name
        else if (name.length() > 3 && name.length() <= 20) {
            try {
                horses = new SimpleService().readHorses(name);
                for (Horse h : horses) {
                    wrappers.add(new HorseWrapper(h));
                }
                horse_data.setAll(wrappers);

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
    private void horseStatisticsClicked() {
        logger.info("btn STATISTICS clicked");

        int selected_index = horse_table.getSelectionModel().getSelectedIndex();
        if (selected_index >= 0) {

            Map<Integer, Integer> map;
            try {
                map = new SimpleService().getStatistics(horse_table.getItems().get(selected_index).getHorse());

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
            logger.info("show statistics of selected entry");
        } else {
            AlertsAndWarnings.noSelectionAlert(ENTITY);
        }
    }

    /**
     * ContextMenu adds the selected Horse to the current simulation
     */
    @FXML
    private void contextToRace() {
        logger.info("context TORACE");

        int selected_index = horse_table.getSelectionModel().getSelectedIndex();
        if (selected_index >= 0) {
            logger.info("add horse to current simulation");
            simu_controller.addHorseToRace(horse_table.getItems().get(selected_index));
        } else {
            AlertsAndWarnings.noSelectionAlert(ENTITY);
        }
    }

    /**
     * Context Menu Edit Horse
     */
    @FXML
    private void contextEditHorse() {
        logger.info("context EDIT");
        editHorseClicked();
    }

    /**
     * Context Menu Delete Horse
     */
    @FXML
    private void contextDeleteHorse() {
        logger.info("context DELETE");
        deleteHorseClicked();
    }

    /**
     * Context Menu Show Statistics
     */
    @FXML
    private void contextShowStatistic() {
        logger.info("context STATISTICS");
        horseStatisticsClicked();
    }
}
