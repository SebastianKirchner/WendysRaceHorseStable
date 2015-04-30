package sepm.ss15.e0926076.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0926076.entities.Horse;
import sepm.ss15.e0926076.gui.wrapper.HorseWrapper;

import java.io.File;

/**
 * Created by Sebastian on 25.03.2015.
 *
 * Controller for the Stage that handles creating and updating a Horse
 */
public class HorseEditDialogController {

    private static final Logger logger = LogManager.getLogger(HorseEditDialogController.class);

    private HorseWrapper wrapper;

    private Stage dialog_stage;

    private boolean ok_clicked = false, new_image_clicked = false;

    @FXML private ImageView profile_img;

    @FXML private Label label_id;
    @FXML private TextField field_horse_name;
    @FXML private TextField field_min_speed;
    @FXML private TextField field_max_speed;

    private String horse_name, pic_path;
    private int min_speed, max_speed, id;

    @FXML
    public void initialize() {
    }

    public void setDialogStage(Stage stage) {
        dialog_stage = stage;
    }

    /**
     * Set the information of the Horse in the TextFields and Labels
     *
     * @param h Horse which parameters are to be filled into the labels
     */
    public void setHorse(Horse h) {
        label_id.setText(Integer.toString(h.getId()));
        field_horse_name.setText(h.getName());
        field_min_speed.setText(Integer.toString(h.getMinSpeed()));
        field_max_speed.setText(Integer.toString(h.getMaxSpeed()));
        profile_img.setImage(new Image(pathToUri(h.getPicPath())));
        this.pic_path = h.getPicPath();
    }

    /**
     *
     * @return true if the dialog was exited by clicking OK
     */
    public boolean isOKClicked() {
        return ok_clicked;
    }

    /**
     *
     * @return the created Horse
     */
    public HorseWrapper getHorseWrapper() {
        return this.wrapper;
    }

    /**
     * Sets the Horse after Dialog was clicked okay with the input values
     */
    @FXML
    private void confirmDialogClicked( ) {
        logger.info("btn OK clicked");
        Horse new_horse = new Horse();

        if (isInputValid()) {
            logger.info("valid input");

            new_horse.setName(this.horse_name);
            new_horse.setMinSpeed(this.min_speed);
            new_horse.setMaxSpeed(this.max_speed);
            new_horse.setId(id);
            new_horse.setPicPath(this.pic_path);

            this.wrapper = new HorseWrapper(new_horse);

            ok_clicked = true;
            new_image_clicked = false;
            dialog_stage.close();
        }
    }

    /**
     * Dialog was cancelled, close the stage
     */
    @FXML
    private void cancelDialogClicked() {
        logger.info("btn CANCEL clicked");
        new_image_clicked = false;
        ok_clicked = false;
        dialog_stage.close();
    }

    /**
     * Open FileChooser to choose a picture as profile picture for the Horse, only accepts
     * jpg, jpeg and png
     */
    @FXML
    private void chooseProfilePictureClicked() {
        logger.info("choose profile picture ...");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        try {
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("IMAGES", "*.jpg; *.jpeg; *.png")
            );
        } catch (Exception e) {
            logger.error("file chooser exception");
            e.printStackTrace();

        }
        File file = fileChooser.showOpenDialog(Main.getPrimaryStage());
        if (file != null) {
            new_image_clicked = true;
            profile_img.setImage(new Image(pathToUri(file.getPath())));
            this.pic_path = file.getPath();
            logger.info(this.pic_path);
        }
    }

    /**
     * Checks whether the input is valid or not
     * @return true if all fields have valid input, if not error dialog is shown and false is returned
     */
    private boolean isInputValid() {
        logger.info("validate input ...");

        String error_msg = "";
        this.min_speed = 0;
        this.max_speed = 0;

        if (field_horse_name.getText() == null || field_horse_name.getText().length() < 3 || field_horse_name.getText().length() > 20) {
            error_msg += "No valid horse name!\n";
        }
        this.horse_name = field_horse_name.getText();

        if (field_min_speed.getText() == null || field_min_speed.getText().length() == 0) {
            error_msg += "No valid minimum speed!\n";
        } else {
            try {
                this.min_speed = Integer.parseInt(field_min_speed.getText());
                if (this.min_speed < 50 || this.min_speed > 100) {
                    error_msg += "Minimum speed must be between 50 and 100!\n";
                }
            } catch (NumberFormatException e) {
                error_msg += "No valid minimum speed (must be an Integer)!\n";
            }
        }

        if (field_max_speed.getText() == null || field_max_speed.getText().length() == 0) {
            error_msg += "No valid maximum speed!\n";
        } else {
            try {
                this.max_speed = Integer.parseInt(field_max_speed.getText());
                if (this.max_speed < 50 || this.max_speed > 100) {
                    error_msg += "Maximum speed must be between 50 and 100!\n";
                } else if (this.max_speed < this.min_speed) {
                    error_msg += "Maximum speed must be larger than minimum speed!\n";
                }
            } catch (NumberFormatException e) {
                error_msg += "No valid maximum speed (must be an Integer)!\n";
            }
        }

        if (label_id.getText() != null) {
            try {
                id = Integer.parseInt(label_id.getText());
            } catch (NumberFormatException e) {
                id = -1;
            }
        }

        if (pic_path == null && !new_image_clicked) {
            error_msg += "Please choose a Photo.";
        }

        if (error_msg.length() == 0) {
            return true;
        } else {
            AlertsAndWarnings.inputErrorAlert("Invalid Input", "Please correct invalid fields", error_msg);
            return false;
        }
    }

    /**
     * Converts the given path into a URI
     *
     * @param path to the picture
     * @return uri of the path
     */
    private String pathToUri(String path) {

        if (new_image_clicked) {
            return "file:/" + path.replace("\\", "/");
        } else {
            return "file:/" + new java.io.File("").getAbsolutePath().replace("\\", "/") + "/" + path.replace("\\", "/");
        }
    }
}
