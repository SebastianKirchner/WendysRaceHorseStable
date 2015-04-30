package sepm.ss15.e0926076.gui.wrapper;

import javafx.beans.property.*;
import sepm.ss15.e0926076.entities.Race;

/**
 * Created by Sebastian on 26.03.2015.
 */
public class RaceWrapper extends Race {

    private Race race;
    private final IntegerProperty raceID;

    private final DoubleProperty averageSpeed;
    private final DoubleProperty randomSpeed;
    private final DoubleProperty luckFactor;
    private final DoubleProperty jockeySkill;

    private JockeyWrapper jockey;
    private HorseWrapper horse;

    public RaceWrapper() {
        this(null);
    }

    public RaceWrapper(Race r) {

        if (r == null) {
            raceID          = new SimpleIntegerProperty(0);
            averageSpeed    = new SimpleDoubleProperty(0.0);
            randomSpeed     = new SimpleDoubleProperty(0.0);
            luckFactor      = new SimpleDoubleProperty(0.0);
            jockeySkill     = new SimpleDoubleProperty(0.0);
        } else {
            raceID          = new SimpleIntegerProperty(r.getRaceID());
            averageSpeed    = new SimpleDoubleProperty(r.getAverageSpeed());
            randomSpeed     = new SimpleDoubleProperty(r.getRandomSpeed());
            luckFactor      = new SimpleDoubleProperty(r.getLuckFactor());
            jockeySkill     = new SimpleDoubleProperty(r.getJockeySkill());
            jockey          = new JockeyWrapper(r.getJockey());
            horse           = new HorseWrapper(r.getHorse());
            this.race       = r;
        }
    }

    public int getRaceID() {
        return raceID.get();
    }

    public void setRaceID(int raceID) {
        this.raceID.set(raceID);
    }

    public IntegerProperty raceIDProperty() {
        return raceID;
    }

    public HorseWrapper getHorse() {
        return this.horse;
    }

    public void setHorseWrapper(HorseWrapper h) {
        this.horse = h;
    }

    public JockeyWrapper getJockey() {
        return this.jockey;
    }

    public void setJockey(JockeyWrapper j) {
        this.jockey = j;
    }

    public double getRandomSpeed() {
        return randomSpeed.get();
    }

    public void setRandomSpeed(double randomSpeed) {
        this.randomSpeed.set(randomSpeed);
    }

    public DoubleProperty randomSpeedProperty() {
        return randomSpeed;
    }

    public double getLuckFactor() {
        return luckFactor.get();
    }

    public void setLuckFactor(double luckFactor) {
        this.luckFactor.set(luckFactor);
    }

    public DoubleProperty luckFactorProperty() {
        return luckFactor;
    }

    public double getJockeySkill() {
        return jockeySkill.get();
    }

    public void setJockeySkill(double jockeySkill) {
        this.jockeySkill.set(jockeySkill);
    }

    public DoubleProperty jockeySkillProperty() {
        return jockeySkill;
    }

    public double getAverageSpeed() {
        return this.averageSpeed.get();
    }

    public DoubleProperty averageSpeedProperty() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed.set(averageSpeed);
    }

    public Race getRace() {

        this.race.setJockey(jockey.getJockey());
        this.race.setHorse(horse.getHorse());
/*      this.race.setLuckFactor(getLuckFactor());
        this.race.setAverageSpeed(getAverageSpeed());
        this.race.setRandomSpeed(getRandomSpeed());
        this.race.setJockeySkill(getJockeySkill());*/
        return this.race;
    }

    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RaceWrapper rw = (RaceWrapper) o;
        return getRaceID() == rw.getRaceID() && getHorse().getId() == rw.getHorse().getId() && getJockey().getId() == rw.getJockey().getId();
    }

    public String toString() {
        return "Race{" +
                "race_ID=" + raceID.getValue() +
                ", horse_ID=" + horse.getId() +
                ", jockey_ID=" + jockey.getId() +
                '}';
    }

}
