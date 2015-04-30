package sepm.ss15.e0926076.entities;

/**
 * Created by Sebastian on 12.03.2015.
 *
 * Entity for Horse, contains no logic (except equals method)
 */
public class Horse {

    protected Integer horse_id;
    protected Integer min_speed;
    protected Integer max_speed;
    protected String name;
    protected String pic_path;

    public Horse() {};

    public Integer getId() {
        return horse_id;
    }

    public void setId(Integer id) {
        this.horse_id = id;
    }

    public Integer getMinSpeed() {
        return min_speed;
    }

    public void setMinSpeed(Integer min_speed) {
        this.min_speed = min_speed;
    }

    public Integer getMaxSpeed() {
        return max_speed;
    }

    public void setMaxSpeed(Integer max_speed) {
        this.max_speed = max_speed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicPath() {
        return pic_path;
    }

    public void setPicPath(String pic_path) {
        this.pic_path = pic_path;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Horse h = (Horse) o;
        return h.getId() == this.horse_id &&
                h.getName().equals(this.name) &&
                h.getMinSpeed() == this.min_speed &&
                h.getMaxSpeed() == this.max_speed &&
                h.pic_path.equals(this.getPicPath());
    }

    @Override
    public String toString() {
        return "Horse{" +
                "horse_id=" + horse_id +
                ", name='" + name + '\'' +
                ", min_speed=" + min_speed +
                ", max_speed=" + max_speed +
                '}';
    }
}
