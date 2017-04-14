package parser.plan.entity;

import java.util.Objects;

public class Discipline extends BaseEntity {
    private final Specialty specialty;
    private final Integer course;
    private final Integer countStudents;
    private final Integer countGroups;
    private final Integer countSubGroups;
    private final Integer countStreams;
    
    public Discipline(Specialty specialty, String name, 
            Integer course, Integer countStudents, Integer countStreams,
            Integer countGroups, Integer countSubGroups) {
         this.specialty = specialty;
        this.name = name;
        this.course = course;
        this.countStudents = countStudents;
        this.countGroups = countGroups;
        this.countSubGroups = countSubGroups;
        this.countStreams = countStreams;
    }

    public Specialty getSpecialty() {
        return specialty;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.name);
        hash = 71 * hash + Objects.hashCode(this.specialty);
        hash = 71 * hash + Objects.hashCode(this.course);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Discipline other = (Discipline) obj;
        if (!Objects.equals(this.specialty, other.specialty)) {
            return false;
        }
        if (!Objects.equals(this.course, other.course)) {
            return false;
        }
        return true;
    }
    
}
