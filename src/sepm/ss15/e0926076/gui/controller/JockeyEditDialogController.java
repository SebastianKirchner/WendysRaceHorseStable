package sepm.ss15.e0926076.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0926076.entities.Jockey;
import sepm.ss15.e0926076.gui.wrapper.JockeyWrapper;

/**
 * Created by Sebastian on 25.03.2015.
 *
 * Controller for the Stage that handles creating and updating a Jockey
 */
public class JockeyEditDialogController {

    private static final Logger logger = LogManager.getLogger(JockeyEditDialogController.class);

    private JockeyWrapper wrapper;

    private Stage dialog_stage;

    private boolean ok_clicked = false;

    @FXML private Label label_id;
    @FXML private TextField field_jockey_name;
    @FXML private TextField field_height;
    @FXML private TextField field_skill;
    @FXML private RadioButton radio_male;
    @FXML private RadioButton radio_female;

    private String name, sex;
    private int id, height;
    private double skill;

    @FXML
    public void initialize() {
    }

    public void setDialogStage(Stage stage) {
        dialog_stage = stage;
    }

    /**
     * Set the information of the Jockey in the TextFields and Labels
     *
     * @param j Jockey whose parameters are to be filled into the labels
     */
    public void setJockey(Jockey j) {
        label_id.setText(Integer.toString(j.getId()));
        field_jockey_name.setText(j.getName());
        if (j.getSex().equals("m")) { radio_male.setSelected(true); }
        else { radio_female.setSelected(true); }
        field_height.setText(Integer.toString(j.getHeight()));
        field_skill.setText(Double.toString(j.getSkill()));
    }

    /**
     *
     * @return true if the dialog was exited by clicking OK
     */
    public boolean isOKClicked() {
        return this.ok_clicked;
    }

    /**
     *
     * @return the created Jockey
     */
    public JockeyWrapper getJockeyWrapper() {
        return this.wrapper;
    }

    /**
     * Sets the Jockey after Dialog was clicked okay with the input values
     */
    @FXML
    private void confirmDialogClicked() {
        logger.info("btn OK clicked");
        Jockey new_jockey = new Jockey();

        if (isInputValid()) {
            logger.info("valid input");

            new_jockey.setId(id);
            new_jockey.setName(name);
            new_jockey.setSex(sex);
            new_jockey.setHeight(height);
            new_jockey.setSkill(skill);

            this.wrapper = new JockeyWrapper(new_jockey);

            ok_clicked = true;
            dialog_stage.close();
        }
    }

    /**
     * Dialog was cancelled, close the stage
     */
    @FXML
    private void cancelDialogClicked() {
        logger.info("btn CANCEL clicked");
        dialog_stage.close();
    }

    /**
     * Checks whether the input is valid or not
     * @return true if all fields have valid input, if not error dialog is shown and false is returned
     */
    private boolean isInputValid() {
        logger.info("validate input ...");

        String error_msg = "";
        this.height = 0;
        this.skill = 0;

        if (field_jockey_name.getText() == null || field_jockey_name.getText().length() < 3 || field_jockey_name.getText().length() > 20) {
            error_msg += "Please check jockey name!\n";
        }
        this.name = field_jockey_name.getText();

        if (!radio_male.isSelected() && !radio_female.isSelected()) {
            error_msg += "Please choose a sex (only 2 options are given for reasons of simplicity)\n";
        }
        this.sex = radio_male.isSelected() ? "m" : "w";

        if (field_height.getText() == null || field_height.getText().length() == 0) {
            error_msg += "Please check jockey height!\n";
        } else {
            try {
                this.height = Integer.parseInt(field_height.getText());
                if (this.height < 50 || this.height > 250) {
                    error_msg += "Height must be between 50 and 250 (cm)!\n";
                }
            } catch (NumberFormatException e) {
                error_msg += "No valid height (must be an Integer)!\n";
            }
        }

        if (field_skill.getText() == null || field_skill.getText().length() == 0) {
            error_msg += "Please check jockey skill!\n";
        } else {
            try {
                this.skill = Double.parseDouble(field_skill.getText());
            } catch (NumberFormatException e) {
                error_msg += "No valid skill (must be a number)!\n";
            }
        }

        if (label_id.getText() != null) {
            try {
                this.id = Integer.parseInt(label_id.getText());
            } catch (NumberFormatException e) {
                id = -1;
            }
        }

        if (error_msg.length() == 0) {
            return true;
        } else {
            AlertsAndWarnings.inputErrorAlert("Invalid Input", "Please correct invalid fields", error_msg);
            return false;
        }
    }
}
