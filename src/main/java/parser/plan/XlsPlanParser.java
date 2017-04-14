package parser.plan;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import parser.plan.entity.Discipline;
import parser.plan.entity.Employee;
import parser.plan.entity.Specialty;

public class XlsPlanParser implements PlanParser {

    private final String[][] docData;
    private static final int COUNT_TEACHERS = 21;
    private static final int ROW_TEACHERS = 5;
    private static final int START_COL_TEACHERS = 43;
    private static final int START_ROW_SPEC = 10;
    private static final int COL_SPEC = 2;
    private static final int COL_DISCIPLINE = COL_SPEC + 4;
    private static final int COL_PROFILE = COL_SPEC + 2;
    private static final int COL_COURSE = COL_SPEC + 5;
    private static final int COL_COUNT_STUDENTS = COL_SPEC + 6;
    private static final int COL_COUNT_STREAMS = COL_SPEC + 7;
    private static final int COL_COUNT_GROUPS = COL_SPEC + 8;
    private static final int COL_COUNT_SUBGROUPS = COL_SPEC + 9;

    public XlsPlanParser(File xlsFile) throws IOException, InvalidFormatException {
        Workbook wb = WorkbookFactory.create(new FileInputStream(xlsFile));
        Sheet sheet = wb.getSheetAt(0);
        Row row;
        Cell cell;

        int rows; // No of rows
        rows = sheet.getPhysicalNumberOfRows();

        int cols = 0; // No of columns
        int tmp = 0;

        // This trick ensures that we get the data properly even if it doesn't start from first few rows
        for (int i = 0; i < 10 || i < rows; i++) {
            row = sheet.getRow(i);
            if (row != null) {
                tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                if (tmp > cols) {
                    cols = tmp;
                }
            }
        }
        docData = new String[rows][cols];
        for (int r = 0; r < rows; r++) {
            row = sheet.getRow(r);
            if (row != null) {
                for (int c = 0; c < cols; c++) {
                    cell = row.getCell((short) c);
                    if (cell != null
                            && cell.getAddress() != null) {
                        String value = null;
                        try {
                            value = cell.getNumericCellValue() + "";
                        } catch (Exception ex) {

                        }
                        if ((value == null) || (value.equals("0.0"))) {
                            if (cell.getCellTypeEnum() == CellType.FORMULA) {
                                FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
                                CellValue cellValue = evaluator.evaluate(cell);
                                if (cellValue.getCellTypeEnum() == CellType.NUMERIC) {
                                    value = cellValue.getNumberValue() + "";
                                } else {
                                    value = cellValue.getStringValue();
                                }
                            } else {
                                cell.setCellType(CellType.STRING);
                                value = cell.getStringCellValue();
                            }
                        }
                        docData[cell.getAddress().getRow()][cell.getAddress().getColumn()] = value;
                    }
                }
            }
        }
    }

    @Override
    public List<Employee> getTeachers() {
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < COUNT_TEACHERS; i++) {
            Employee teacher = new Employee(docData[ROW_TEACHERS][START_COL_TEACHERS + i]);
            employees.add(teacher);
            setTeacherDisciplines(teacher, START_COL_TEACHERS + i);
        }
        return employees;
    }

    @Override
    public List<Discipline> getDisciplines() {
        List<Discipline> disciplines = new ArrayList<>();
        for (int i = START_ROW_SPEC; i < docData.length; i++) {
            if (docData[i][COL_SPEC] != null && !docData[i][COL_SPEC].equals("")) {
                Pattern p = Pattern.compile("^[0-9]{2}\\..*");
                Matcher m = p.matcher(docData[i][COL_SPEC]);
                if (m.matches()) {
                    disciplines.add(getDisciplineByRow(i));
                }
            }
        }
        return disciplines.stream().collect(Collectors.toList());
    }

    private Discipline getDisciplineByRow(int row) {
        return new Discipline(
                new Specialty(docData[row][COL_SPEC],
                        docData[row][COL_PROFILE]),
                docData[row][COL_DISCIPLINE],
                getIntegerSafe(docData[row][COL_COURSE]),
                getIntegerSafe(docData[row][COL_COUNT_STUDENTS]),
                getIntegerSafe(docData[row][COL_COUNT_STREAMS]),
                getIntegerSafe(docData[row][COL_COUNT_GROUPS]),
                getIntegerSafe(docData[row][COL_COUNT_SUBGROUPS])
        );
    }

    private Integer getIntegerSafe(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        Integer value;
        try {
            value = Integer.valueOf(str);
        } catch (Exception ex) {
            value = null;
        }
        return value;
    }

    private void setTeacherDisciplines(Employee teacher, int col) {
        for (int i = ROW_TEACHERS + 5; i < docData.length; i++) {
            if (docData[i][col] != null && !docData[i][col].equals("")) {
                Double hours = null;
                try {
                    hours = Double.valueOf(docData[i][col]);
                } catch (Exception ex) {
                }
                teacher.getHours().put(getDisciplineByRow(i), hours);
            }
        }
    }
}
