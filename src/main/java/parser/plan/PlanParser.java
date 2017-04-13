package parser.plan;

import java.util.List;
import parser.plan.entity.Employee;
import parser.plan.entity.Specialty;

public interface PlanParser {
    List<Employee> getTeachers();
    List<Specialty> getSpecialties();
}
