package sepm.ss15.e0926076.gui.wrapper;

import javafx.beans.property.*;
import sepm.ss15.e0926076.entities.Jockey;

/**
 * Created by Sebastian on 26.03.2015.
 */
public class JockeyWrapper extends Jockey {

    private Jockey jockey;

    private final IntegerProperty jockeyID;
    private final DoubleProperty skill;
    private final IntegerProperty height;
    private final StringProperty name;
    private final StringProperty sex;

    public JockeyWrapper() { this(null); }

    public JockeyWrapper(Jockey j) {

        if (j == null) {
            this.jockeyID   = new SimpleIntegerProperty(0);
            this.height     = new SimpleIntegerProperty(0);
            this.name       = new SimpleStringProperty(null);
            this.sex        = new SimpleStringProperty(null);
            this.skill      = new SimpleDoubleProperty(0.0);
        } else {
            this.jockeyID   = new SimpleIntegerProperty(j.getId());
            this.height     = new SimpleIntegerProperty(j.getHeight());
            this.name       = new SimpleStringProperty(j.getName());
            this.sex        = new SimpleStringProperty(j.getSex());
            this.skill      = new SimpleDoubleProperty(j.getSkill());
            this.jockey     = j;
        }
    }
    public Integer getId() {
        return jockeyID.get();
    }

    public void setId(Integer id) {
        this.jockeyID.set(id);
    }

    public IntegerProperty jockeyIDProperty() {
        return jockeyID;
    }

    public Double getSkill() {
        return skill.get();
    }

    public void setSkill(Double skill) {
        this.skill.set(skill);
    }

    public DoubleProperty skillProperty() {
        return skill;
    }

    public Integer getHeight() {
        return height.get();
    }

    public void setHeight(Integer height) {
        this.height.set(height);
    }

    public IntegerProperty heightProperty() {
        return height;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getSex() {
        return sex.get();
    }

    public void setSex(String sex) {
        this.sex.set(sex);
    }

    public StringProperty sexProperty() {
        return sex;
    }

    public Jockey getJockey() {

        this.jockey.setName(getName());
        this.jockey.setSkill(getSkill());
        this.jockey.setSex(getSex());
        this.jockey.setHeight(getHeight());
        return this.jockey;
    }

    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        return this.jockey.getId() == ((JockeyWrapper)o).getJockey().getId();
    }
}
