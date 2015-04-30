package sepm.ss15.e0926076.entities;

/**
 * Created by Sebastian on 13.03.2015.
 *
 * Entity for Race, contains no logic (except equals method)
 */
public class Race {

    private int race_ID, placement;

    private double average_speed, random_speed, luck_factor, jockey_skill;

    private Jockey jockey;
    private Horse horse;

    public double getRandomSpeed() {
        return random_speed;
    }

    public void setRandomSpeed(double random_speed) {
        this.random_speed = random_speed;
    }

    public double getLuckFactor() {
        return luck_factor;
    }

    public void setLuckFactor(double luck_factor) {
        this.luck_factor = luck_factor;
    }

    public double getJockeySkill() {
        return jockey_skill;
    }

    public void setJockeySkill(double jockey_skill) {
        this.jockey_skill = jockey_skill;
    }

    public int getRaceID() {
        return race_ID;
    }

    public void setRaceID(int race_ID) {
        this.race_ID = race_ID;
    }

    public Horse getHorse() {
        return this.horse;
    }

    public void setHorse(Horse h) {
        this.horse = h;
    }

    public Jockey getJockey() {
        return this.jockey;
    }

    public void setJockey(Jockey j) {
        this.jockey = j;
    }

    public double getAverageSpeed() {
        return average_speed;
    }

    public void setAverageSpeed(double average_speed) {
        this.average_speed = average_speed;
    }

    public void setPlacement(int place) {
        this.placement = place;
    }

    public int getPlacement() {
        return this.placement;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Race r = (Race) o;
        return r.getRaceID() == this.race_ID &&
                r.getHorse().equals(this.horse) &&
                r.getJockey().equals(this.jockey);
    }

    @Override
    public String toString() {
        return "Race{" +
                "race_ID=" + race_ID +
                ", horse_ID=" + horse.getId() +
                ", placement=" + placement +
                ", jockey_ID=" + jockey.getId() +
                ", average_speed=" + average_speed +
                ", random_speed=" + random_speed +
                ", jockey_skill=" + jockey_skill +
                ", luck_factor=" + luck_factor +
                '}';
    }
}
