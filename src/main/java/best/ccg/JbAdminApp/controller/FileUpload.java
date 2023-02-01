package best.ccg.JbAdminApp.controller;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import best.ccg.JbAdminApp.entity.IdentifiedComputers;
import best.ccg.JbAdminApp.entity.IdentifiedEmployees;
import best.ccg.JbAdminApp.service.IdentifiedComputersService;
import best.ccg.JbAdminApp.service.IdentifiedEmployeesService;

import javax.servlet.ServletException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/file")
public class FileUpload {

    private static final Logger logger
            = LoggerFactory.getLogger(FileUpload.class);


    private IdentifiedComputersService identifiedComputersService;
    private IdentifiedEmployeesService identifiedEmployeesService;

    @Autowired
    public void setIdentifiedComputersService(IdentifiedComputersService identifiedComputersService) {
        this.identifiedComputersService = identifiedComputersService;
    }

    @Autowired
    public void setIdentifiedEmployeesService(IdentifiedEmployeesService identifiedEmployeesService) {
        this.identifiedEmployeesService = identifiedEmployeesService;
    }

    @GetMapping("/index")
    public String index() {
        try {

            return "uploadfile";
        } catch (Exception e) {

            return e.getMessage();
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    ModelAndView register(@RequestParam MultipartFile file, @RequestParam String type)
            throws IOException {
        System.out.println(1);
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            String filename = file.getOriginalFilename();
            String uploadPath = "uploads/";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadPath + filename));
            stream.write(bytes);
            stream.flush();
            stream.close();

            File initialFile = new File(uploadPath + filename);
            InputStream inputStream = new FileInputStream(initialFile);
            String fileData = IOUtils.toString(inputStream, String.valueOf(StandardCharsets.UTF_8));
            IOUtils.closeQuietly(inputStream);
            if (isJSONValid(fileData)) {
                JSONObject jsonarray = new JSONObject(new JSONTokener(fileData));

                if (type.equals("0")) {

                    JSONArray str = jsonarray.getJSONArray("Logins");
                    Set<IdentifiedEmployees> identifiedEmployees = new HashSet<>();
                    str.forEach(s -> identifiedEmployees.add(new IdentifiedEmployees(s.toString().toUpperCase())));
                    if (identifiedEmployees.size() > 0) {
                        identifiedEmployeesService.saveAll(identifiedEmployees);
                    }
                } else if (type.equals("1")) {

                    JSONArray str = jsonarray.getJSONArray("Computers");
                    Set<IdentifiedComputers> identifiedComputers = new HashSet<>();
                    str.forEach(s -> identifiedComputers.add(new IdentifiedComputers(s.toString().toUpperCase())));

                    if (identifiedComputers.size() > 0) {
                        identifiedComputersService.saveAll(identifiedComputers);
                    }

                }
            }
        }
        return new ModelAndView("/uploadfile");
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}