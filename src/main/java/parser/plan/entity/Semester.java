package parser.plan.entity;

import java.util.List;

public class Semester {

    private enum Type {
        SPRING("весна"), AUTUMN("осень");
        private final String val;

        private Type(String val) {
            this.val = val;
        }

        public String getVal() {
            return val;
        }
    }
        
    private List<Specialty> specialties;

    public List<Specialty> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<Specialty> specialties) {
        this.specialties = specialties;
    }

}
