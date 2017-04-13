package parser.plan.entity;

import java.util.List;

public class Specialty extends BaseEntity {
    private List<Discipline> disciplines;
    private final String profile;
    private final Integer course;
    private final Integer countStudents;
    private final Integer countGroups;
    private final Integer countSubGroups;
    private final Integer countStreams;

    public Specialty(String name, String profile, 
            Integer course, Integer countStudents, Integer countStreams,
            Integer countGroups, Integer countSubGroups) {
        this.name = name;
        this.profile = profile;
        this.course = course;
        this.countStudents = countStudents;
        this.countGroups = countGroups;
        this.countSubGroups = countSubGroups;
        this.countStreams = countStreams;
    }

    public void setDisciplines(List<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    public String getProfile() {
        return profile;
    }

    public Integer getCourse() {
        return course;
    }
    public Integer getCountStudents() {
        return countStudents;
    }

    public Integer getCountGroups() {
        return countGroups;
    }

    public Integer getCountSubGroups() {
        return countSubGroups;
    }

    public Integer getCountStreams() {
        return countStreams;
    }
}
