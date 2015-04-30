package sepm.ss15.e0926076.entities;

/**
 * Created by Sebastian on 12.03.2015.
 *
 * Entity for Jockey, contains no logic (except equals method)
 */
public class Jockey {

    private Integer jockey_id;
    private Double skill;
    private Integer height;
    private String name;
    private String sex;

    public Jockey() {};

    public Integer getId() {
        return jockey_id;
    }

    public void setId(Integer id) {
        this.jockey_id = id;
    }

    public Double getSkill() {
        return skill;
    }

    public void setSkill(Double skill) {
        this.skill = skill;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Jockey j = (Jockey) o;
        return j.getId() == this.getId() &&
                j.getName().equals(this.getName()) &&
                j.getHeight() == this.getHeight() &&
                j.getSex().equals(this.getSex());
    }

    @Override
    public String toString() {
        return "Jockey{" +
                "jockey_id=" + jockey_id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", height=" + height +
                ", skill=" + skill +
                '}';
    }
}
