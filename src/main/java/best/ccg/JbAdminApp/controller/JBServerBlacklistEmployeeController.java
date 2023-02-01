package best.ccg.JbAdminApp.controller;

import best.ccg.JbAdminApp.entity.JBServerBlacklistEmployee;
import best.ccg.JbAdminApp.entity.JBServerEmployee;
import best.ccg.JbAdminApp.entity.JBServerProductBlacklist;
import best.ccg.JbAdminApp.service.JBServerBlacklistEmployeeService;
import best.ccg.JbAdminApp.service.JBServerEmployeeService;
import best.ccg.JbAdminApp.service.JBServerProductBlacklistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/blacklist")
public class JBServerBlacklistEmployeeController {

    JBServerBlacklistEmployeeService jbServerBlacklistEmployeeService;
    JBServerEmployeeService jbServerEmployeeService;
    JBServerProductBlacklistService jbServerProductBlacklistService;

    @Autowired
    public void setJbServerProductBlacklistService(JBServerProductBlacklistService jbServerProductBlacklistService) {
        this.jbServerProductBlacklistService = jbServerProductBlacklistService;
    }

    @Autowired
    public void setJbServerEmployeeService(JBServerEmployeeService jbServerEmployeeService) {
        this.jbServerEmployeeService = jbServerEmployeeService;
    }

    @Autowired
    public void setJbServerBlacklistEmployeeService(JBServerBlacklistEmployeeService jbServerBlacklistEmployeeService) {
        this.jbServerBlacklistEmployeeService = jbServerBlacklistEmployeeService;
    }


    static final Logger log =
            LoggerFactory.getLogger(JBServerBlacklistEmployeeController.class);

    @GetMapping("/addemployee")
    public String addEmployee(@RequestParam("employeeid") String employee_id,
                              @RequestParam("productid") String jbServerProductBlacklist_id) {
        try {
            long employee_id_ = Long.parseLong(employee_id);
            Long jbServerProductBlacklist_id_ = Long.parseLong(jbServerProductBlacklist_id);
            JBServerProductBlacklist product = jbServerProductBlacklistService.getByID(jbServerProductBlacklist_id_);
            JBServerEmployee employee = jbServerEmployeeService.getByID(employee_id_);
            JBServerBlacklistEmployee bl = new JBServerBlacklistEmployee(employee, product);
            log.warn("User: {} add: {} to blacklist;", UserController.getUser(), employee.getName());
            jbServerBlacklistEmployeeService.save(bl);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
        }
        return "redirect:/jbemployee/list";
    }

    @GetMapping("/deleteemployee")
    public String deleteEmployee(@RequestParam("employeeid") String employee_id,
                                 @RequestParam("productid") String jbServerProductBlacklist_id) {
        try {
            long employee_id_ = Long.parseLong(employee_id);
            Long jbServerProductBlacklist_id_ = Long.parseLong(jbServerProductBlacklist_id);
            JBServerEmployee employee = jbServerEmployeeService.getByID(employee_id_);
            log.warn("User: {} deleted: {} from blacklist;", UserController.getUser(), employee.getName());
            jbServerBlacklistEmployeeService.deleteByIdEmployeeIdAndIdProductBlacklistId(employee_id_, jbServerProductBlacklist_id_);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
        }
        return "redirect:/jbemployee/list";
    }
}