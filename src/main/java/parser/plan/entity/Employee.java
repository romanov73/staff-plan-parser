package parser.plan.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Employee extends BaseEntity {
    private Map<Discipline, Double> hours;
    
    public Employee(String name) {
        this.name = name;
        this.hours = new HashMap<>();
    }

    public Map<Discipline, Double> getHours() {
        return hours;
    }

    public void setHours(Map<Discipline, Double> hours) {
        this.hours = hours;
    }

    @Override
    public String toString() {
        return "Employee{" + "name=" + name + '}';
    }
}
