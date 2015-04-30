package sepm.ss15.e0926076.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Sebastian on 25.03.2015.
 *
 * Controller for the Stage that handles showing the Statistics as a PieChart
 */
public class StatisticsViewController implements Initializable{

    private static final Logger logger = LogManager.getLogger(StatisticsViewController.class);
    private static ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

    private static Stage stage;

    @FXML private PieChart pie_chart;

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
        pie_chart.setData(data);
    }

    public static void setStage(Stage s) {
        stage = s;
    }


    public static void setData(ObservableList<PieChart.Data> new_data) {
        data = new_data;
    }

}
