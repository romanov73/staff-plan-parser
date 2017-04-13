package parser.plan;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import parser.plan.entity.Employee;
import parser.plan.entity.Specialty;

public class XlsPlanParser implements PlanParser {

    private final String[][] docData;
    private static final int COUNT_TEACHERS = 21;
    private static final int ROW_TEACHERS = 5;
    private static final int START_COL_TEACHERS = 43;
    private static final int START_ROW_SPEC = 10;
    private static final int COL_SPEC = 2;
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
                        cell.setCellType(CellType.STRING);
                        docData[cell.getAddress().getRow()][cell.getAddress().getColumn()] = cell.getStringCellValue();
                    }
                }
            }
        }
    }

    @Override
    public List<Employee> getTeachers() {
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < COUNT_TEACHERS; i++) {
            employees.add(new Employee(docData[ROW_TEACHERS][START_COL_TEACHERS + i]));
        }
        return employees;
    }

    @Override
    public List<Specialty> getSpecialties() {
        List<Specialty> specialties = new ArrayList<>();
        for (int i = START_ROW_SPEC; i < docData.length; i++) {
            if (docData[i][COL_SPEC] != null && !docData[i][COL_SPEC].equals("")) {
                Pattern p = Pattern.compile("^[0-9]{2}\\..*");
                Matcher m = p.matcher(docData[i][COL_SPEC]);
                if (m.matches()) {
                    specialties.add(new Specialty(docData[i][COL_SPEC],
                            docData[i][COL_PROFILE],
                            getIntegerSafe(docData[i][COL_COURSE]),
                            getIntegerSafe(docData[i][COL_COUNT_STUDENTS]),
                            getIntegerSafe(docData[i][COL_COUNT_STREAMS]),
                            getIntegerSafe(docData[i][COL_COUNT_GROUPS]),
                            getIntegerSafe(docData[i][COL_COUNT_SUBGROUPS])
                    ));
                }
            }
        }
        return specialties.stream().collect(Collectors.toList());
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
}
