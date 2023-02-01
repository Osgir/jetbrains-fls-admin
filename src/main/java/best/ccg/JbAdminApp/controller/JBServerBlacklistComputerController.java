package best.ccg.JbAdminApp.controller;

import best.ccg.JbAdminApp.entity.JBServerBlacklistComputer;
import best.ccg.JbAdminApp.entity.JBServerComputer;
import best.ccg.JbAdminApp.entity.JBServerProductBlacklist;
import best.ccg.JbAdminApp.service.JBServerBlacklistComputerService;
import best.ccg.JbAdminApp.service.JBServerComputerService;
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
public class JBServerBlacklistComputerController {

    JBServerProductBlacklistService jbServerProductBlacklistService;
    JBServerComputerService jbServerComputerService;
    JBServerBlacklistComputerService jbServerBlacklistComputerService;

    @Autowired
    public void setJbServerBlacklistComputerService(JBServerBlacklistComputerService jbServerBlacklistComputerService) {
        this.jbServerBlacklistComputerService = jbServerBlacklistComputerService;
    }

    @Autowired
    public void setJbServerComputerService(JBServerComputerService jbServerComputerService) {
        this.jbServerComputerService = jbServerComputerService;
    }

    @Autowired
    public void setJbServerProductBlacklistService(JBServerProductBlacklistService jbServerProductBlacklistService) {
        this.jbServerProductBlacklistService = jbServerProductBlacklistService;
    }

    static final Logger log =
            LoggerFactory.getLogger(JBServerBlacklistComputerController.class);

    @GetMapping("/addcopmuter")
    public String addCopmuter(@RequestParam("computerid") String computerId,
                              @RequestParam("productid") String jbServerProductBlacklist_id) {
        try {
            Long computerId_ = Long.parseLong(computerId);
            Long jbServerProductBlacklist_id_ = Long.parseLong(jbServerProductBlacklist_id);
            JBServerProductBlacklist product = jbServerProductBlacklistService.getByID(jbServerProductBlacklist_id_);
            JBServerComputer computer = jbServerComputerService.getByID(computerId_);
            JBServerBlacklistComputer bl = new JBServerBlacklistComputer(computer, product);
            log.warn("User: {} add: {} to blacklist;", UserController.getUser(), computer.getName());
            jbServerBlacklistComputerService.save(bl);

        } catch (NumberFormatException e) {
            log.error(e.getMessage());
        }
        return "redirect:/jbemployee/list";
    }

    @GetMapping("/deletecomputer")
    public String deleteEmployee(@RequestParam("computerid") String computerId,
                                 @RequestParam("productid") String jbServerProductBlacklist_id) {
        try {
            Long computerId_ = Long.parseLong(computerId);
            Long jbServerProductBlacklist_id_ = Long.parseLong(jbServerProductBlacklist_id);
            JBServerComputer computer = jbServerComputerService.getByID(computerId_);

            log.warn("User: {} deleted: {} from blacklist;", UserController.getUser(), computer.getName());
            jbServerBlacklistComputerService.deleteByComputerIdAndProductIdBlacklist(computerId_, jbServerProductBlacklist_id_);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
        }
        return "redirect:/jbemployee/list";
    }
}