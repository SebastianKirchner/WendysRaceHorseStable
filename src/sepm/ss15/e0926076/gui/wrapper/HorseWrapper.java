package sepm.ss15.e0926076.gui.wrapper;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sepm.ss15.e0926076.entities.Horse;

/**
 * Created by Sebastian on 26.03.2015.
 */
public class HorseWrapper extends Horse {

    private final StringProperty horseName;
    private final IntegerProperty horseID;
    private final IntegerProperty horseMinSpeed;
    private final IntegerProperty horseMaxSpeed;
    private String pic_path;


    public HorseWrapper() {
        this(null);
    }

    public HorseWrapper(Horse h) {

        if (h== null) {
            this.horseName          = new SimpleStringProperty(null);
            this.horseID            = new SimpleIntegerProperty(0);
            this.horseMinSpeed      = new SimpleIntegerProperty(0);
            this.horseMaxSpeed      = new SimpleIntegerProperty(0);
        } else {
            this.horseName          = new SimpleStringProperty(h.getName());
            this.horseID            = new SimpleIntegerProperty(h.getId());
            this.horseMinSpeed      = new SimpleIntegerProperty(h.getMinSpeed());
            this.horseMaxSpeed      = new SimpleIntegerProperty(h.getMaxSpeed());
            this.pic_path           = h.getPicPath();
        }
    }

    public Integer getId() {

        return horseID.get();
    }

    public void setId(Integer id) {
        super.horse_id = id;
        this.horseID.set(id);
    }

    public IntegerProperty horseIDProperty() {
        return horseID;
    }

    public Integer getMinSpeed() {
        return horseMinSpeed.get();
    }

    public void setMinSpeed(Integer min_speed) {
        super.setMinSpeed(min_speed);
        this.horseMinSpeed.set(min_speed);
    }

    public IntegerProperty horseMinSpeedProperty() {
        return horseMinSpeed;
    }

    public Integer getMaxSpeed() {
        return horseMaxSpeed.get();
    }

    public void setMaxSpeed(Integer max_speed) {
        super.setMaxSpeed(max_speed);
        this.horseMaxSpeed.set(max_speed);
    }

    public IntegerProperty horseMaxSpeedProperty() {
        return horseMaxSpeed;
    }

    public String getName() {
        return horseName.get();
    }

    public void setName(String name) {
        super.setName(name);
        this.horseName.set(name);
    }

    public StringProperty horseNameProperty() {
        return horseName;
    }

    public void setPicPath(String path) {
        this.pic_path = path;
    }

    public String getPicPath() {
        return this.pic_path;
    }

    public Horse getHorse() {
        Horse horse = new Horse();
        horse.setId(getId());
        horse.setName(getName());
        horse.setMinSpeed(getMinSpeed());
        horse.setMaxSpeed(getMaxSpeed());
        horse.setPicPath(this.pic_path);
        return horse;
    }

    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        return getId() == ((HorseWrapper)o).getId();
    }
}
