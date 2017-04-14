package parser.plan;

import java.util.List;
import parser.plan.entity.Employee;
import parser.plan.entity.Discipline;

public interface PlanParser {
    List<Employee> getTeachers();
    List<Discipline> getDisciplines();
}
