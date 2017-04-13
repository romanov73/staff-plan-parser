package parser.plan;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
            employees.add(new Employee(docData[ROW_TEACHERS][START_COL_TEACHERS+i]));
        }
        return employees;
    }

    @Override
    public List<Specialty> getSpecialties() {
        throw new RuntimeException("Not implemented yet");
    }
}
