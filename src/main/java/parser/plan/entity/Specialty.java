package parser.plan.entity;

public class Specialty extends BaseEntity {
    private final String profile;

    public Specialty(String name, String profile) {
               this.profile = profile;
               this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    @Override
    public String toString() {
        return "Speciality{" + "name=" + name + '}';
    }
}
