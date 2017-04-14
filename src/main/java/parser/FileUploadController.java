package parser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import parser.storage.StorageFileNotFoundException;
import parser.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.HttpStatus;
import parser.plan.XlsPlanParser;
import parser.plan.entity.Discipline;
import parser.plan.entity.Employee;

@Controller
@RestController(value=FileUploadController.BASE_PATH)
@Api(value = "File management")
public class FileUploadController {
    public final static String BASE_PATH = "file-management";
    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService
                .loadAll()
                .map(path
                        -> MvcUriComponentsBuilder
                        .fromMethodName(FileUploadController.class, "serveFile", path.getFileName().toString())
                        .build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        storageService.store(file);
        try {
            XlsPlanParser parser = new XlsPlanParser(storageService.load(file.getOriginalFilename()).toFile());
            List<Employee> teachers = parser.getTeachers();
            System.out.println(teachers.size());
            List<Discipline> disciplines = parser.getDisciplines();
            System.out.println(disciplines.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
    
    

    @RequestMapping(method = RequestMethod.POST, path="/"+BASE_PATH+"/upload")
    @ApiOperation(value = "getEntityById")
    @ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = Employee.class),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ResponseEntity<List<Employee>> getTeachers(@RequestParam("file") MultipartFile file) throws IOException, InvalidFormatException {
        storageService.store(file);
        XlsPlanParser parser = new XlsPlanParser(storageService.load(file.getOriginalFilename()).toFile());
        List<Employee> teachers = parser.getTeachers();
        return new ResponseEntity(teachers, HttpStatus.OK);
    }

}
