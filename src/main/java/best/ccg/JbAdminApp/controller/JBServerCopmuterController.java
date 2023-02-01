package best.ccg.JbAdminApp.controller;

import best.ccg.JbAdminApp.entity.JBServerComputer;
import best.ccg.JbAdminApp.entity.JBServerEmployee;
import best.ccg.JbAdminApp.service.JBServerComputerService;
import best.ccg.JbAdminApp.service.JBServerEmployeeService;
import best.ccg.JbAdminApp.service.JBServerProductBlacklistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static best.ccg.JbAdminApp.config.AppSettings.appSettingsHasIdentifiers;


@Controller
@RequestMapping("/jbemployee")
public class JBServerCopmuterController {
    static final Logger logger =
            LoggerFactory.getLogger(JBServerCopmuterController.class);

    JBServerComputerService jbServerComputerService;
    JBServerProductBlacklistService jbServerProductBlacklistService;
    JBServerEmployeeService jbServerEmployeeService;

    @Autowired
    public void setJbServerProductBlacklistService(JBServerProductBlacklistService jbServerProductBlacklistService) {
        this.jbServerProductBlacklistService = jbServerProductBlacklistService;
    }

    @Autowired
    public void setJbServerComputerService(JBServerComputerService jbServerComputerService) {
        this.jbServerComputerService = jbServerComputerService;
    }

    @Autowired
    public void setJbServerEmployeeService(JBServerEmployeeService jbServerEmployeeService) {
        this.jbServerEmployeeService = jbServerEmployeeService;
    }

    @GetMapping("/computer/list")
    public String dataCopmuter2(Model theModel,
                                @RequestParam(defaultValue = "0") int empid,
                                @RequestParam(defaultValue = "0") int pageComp,
                                @RequestParam(defaultValue = "20") int sizeComp) {

        if(empid==0)
            return "JBServerComputer/list_empty";

        Pageable paging = PageRequest.of(pageComp, sizeComp);
        Page<JBServerComputer> serverLogs = jbServerComputerService.findAll3(empid, paging);
        JBServerEmployee jbServerEmployee = jbServerEmployeeService.getByID(empid);

        theModel.addAttribute("number", serverLogs.getNumber());
        theModel.addAttribute("totalPages", serverLogs.getTotalPages());
        theModel.addAttribute("totalElements", serverLogs.getTotalElements());
        theModel.addAttribute("size", serverLogs.getSize());
        theModel.addAttribute("data", serverLogs.getContent());
        theModel.addAttribute("employee", jbServerEmployee);

        theModel.addAttribute("allid", jbServerProductBlacklistService.findByValueAddifNotExist("ALL").getId());
        theModel.addAttribute("empid", empid);
        theModel.addAttribute("isHasIdentifiers", appSettingsHasIdentifiers);
        return "JBServerComputer/list";
    }
}
