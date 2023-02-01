package best.ccg.JbAdminApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import best.ccg.JbAdminApp.entity.JBServerEmployee;
import best.ccg.JbAdminApp.service.JBServerEmployeeService;
import best.ccg.JbAdminApp.service.JBServerProductBlacklistService;

import static best.ccg.JbAdminApp.config.AppSettings.appSettingsHasIdentifiers;

@Controller
@RequestMapping("/jbemployee")
public class JBServerEmployeeController {
    static final Logger logger =
            LoggerFactory.getLogger(JBServerEmployeeController.class);
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

    @GetMapping("/list")
    public String listServerlog2(
            Model theModel,
            @RequestParam(required = false, defaultValue = "") String searchname,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int sort,
            @RequestParam(defaultValue = "0") int hideBlock,
            @RequestParam(defaultValue = "0") int empid
            ) {
        try {
            if (size > 100) size = 100;
            String s = "lastseenutildate";
            if (sort == 1) s = "lastseenerrordate";

            Pageable paging = PageRequest.of(page, size, Sort.by("isIdentified").and(Sort.by(s).descending()));
            Page<JBServerEmployee> serverLogs;

            serverLogs = jbServerEmployeeService.findAll5(searchname, paging, hideBlock);

            theModel.addAttribute("number", serverLogs.getNumber());
            theModel.addAttribute("totalPages", serverLogs.getTotalPages());
            theModel.addAttribute("totalElements", serverLogs.getTotalElements());
            theModel.addAttribute("size", serverLogs.getSize());
            theModel.addAttribute("data", serverLogs.getContent());
            theModel.addAttribute("sort", sort);
            theModel.addAttribute("searchname", searchname);
            theModel.addAttribute("empid", empid);
            theModel.addAttribute("allid", jbServerProductBlacklistService.findByValueAddifNotExist("ALL").getId());
            theModel.addAttribute("isHasIdentifiers", appSettingsHasIdentifiers);

            return "JBServerEmployee/list";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return e.getMessage();
        }
    }
}