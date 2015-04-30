package sepm.ss15.e0926076.gui.controller;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

/**
 * Created by Sebastian on 25.03.2015.
 *
 * Purely static methods for displaying error/warning dialogs as well as confirmation dialogs
 *
 * Source: http://code.makery.ch/blog/javafx-dialogs-official/
 */
public class AlertsAndWarnings {


    /**
     * Shows a Dialog saying that a selection is missing
     *
     * @param entity the object that has not been selected
     */
    public static void noSelectionAlert(String entity) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Selection");
        alert.setHeaderText("No " + entity + " Selected!");
        alert.setContentText("Please select a " + entity + " from the table.");
        alert.initOwner(Main.getPrimaryStage());

        alert.showAndWait();
    }

    /**
     * Dialog handling any kind of errors, title, header and errors can be set via parameters
     *
     * @param title title of the dialog
     * @param header header of the dialog
     * @param errors input errors
     */
    public static void inputErrorAlert(String title, String header, String errors) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(Main.getPrimaryStage());
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(errors);

        alert.showAndWait();
    }

    /**
     * Dialog handling exceptions, shows exception message as well as th whole stacktrace
     *
     * @param e Exception that has been thrown
     */
    public static void exceptionWarningAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(Main.getPrimaryStage());

        alert.setTitle("Exception Dialog");
        alert.setHeaderText("An error occurred while trying to process your request");
        alert.setContentText(e.getMessage());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label,0,0);
        expContent.add(textArea,0,1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    /**
     * Dialog handling the confimation of a delete action
     *
     * @param title title for the dialog
     * @param header header for the dialog
     * @param content content of the dialog
     * @return OK or cancel
     */
    public static Optional<ButtonType> confirmDeleteAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initOwner(Main.getPrimaryStage());

        return alert.showAndWait();
    }

    /**
     * Dialog handling the listing Options, either list all or list by name
     *
     * @param entity Entity that is to be listed
     * @return CANCEL=cancel the action; ERROR=other error; empty String=list all; String name
     */
    public static String confirmListingOptionDialog(String entity) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(Main.getPrimaryStage());
        alert.setTitle("Confirmation your action");
        alert.setHeaderText(null);
        alert.setContentText("Choose between listing all the " + entity.toLowerCase() + "s OR listing all with a certain name.");

        ButtonType btn_list_all     = new ButtonType("List all...");
        ButtonType btn_list_by_name = new ButtonType("List by name...");
        ButtonType btn_cancel       = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btn_list_all, btn_list_by_name, btn_cancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btn_list_all) {
            return "";
        } else if (result.get() == btn_list_by_name) {
            return textInputDialog("name", "Name Input Dialog", "The name has to have at least 3 characters",
                    "Please enter the name to look for:");
        } else if (result.get() == btn_cancel) {
            return "CANCEL";
        } else {
            return "ERROR";
        }
    }

    /**
     * Dialog handling the listing Options, either list all, list by horse, jockey or race ID
     *
     * @return emptyString=list all; "horse" + ID = list horse; "jockey" + id = jockey; "race" + id = race id;
     *              CANCEL=cancel the action; ERROR=other error;
     */
    public static String confirmRaceListDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(Main.getPrimaryStage());
        alert.setTitle("Confirm your action");
        alert.setHeaderText(null);
        alert.setContentText("Choose between listing all the races OR listing all with a certain ID, Jockey or Horse.");

        ButtonType btn_list_all     = new ButtonType("List all...");
        ButtonType btn_list_by_h    = new ButtonType("List by Horse...");
        ButtonType btn_list_by_j    = new ButtonType("List by Jockey...");
        ButtonType btn_list_by_id   = new ButtonType("List by Race ID...");
        ButtonType btn_cancel       = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btn_list_all, btn_list_by_h, btn_list_by_j, btn_list_by_id, btn_cancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btn_list_all) {
            return "";
        } else if (result.get() == btn_list_by_h) {
            return "horse" + textInputDialog("Horse ID", "Input Dialog", "List all races with a certain horse.",
                    "Please enter the Horse ID to look for:");
        }  else if (result.get() == btn_list_by_j) {
            return "jockey" + textInputDialog("Jockey ID", "Input Dialog", "List all races with a certain jockey.",
                    "Please enter the Jockey ID to look for:");
        }  else if (result.get() == btn_list_by_id) {
            return "race" + textInputDialog("Race ID", "Input Dialog", "List all races with a certain Race ID.",
                    "Please enter the Race ID to look for:");
        } else if (result.get() == btn_cancel) {
            return "CANCEL";
        } else {
            return "ERROR";
        }
    }

    /**
     * helper method handling text input
     *
     * @param inp the input in
     * @param title title of the dialog
     * @param header header of the dialog
     * @param content content of the dialog
     * @return string that has been input or CANCEL if there is no input
     */
    private static String textInputDialog(String inp, String title, String header, String content) {

        TextInputDialog dialog = new TextInputDialog(inp);
        dialog.initOwner(Main.getPrimaryStage());
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) { return result.get(); }
        else                    { return "CANCEL"; }
    }
}
