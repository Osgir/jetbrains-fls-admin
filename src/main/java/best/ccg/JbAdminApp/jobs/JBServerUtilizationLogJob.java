package best.ccg.JbAdminApp.jobs;

import best.ccg.JbAdminApp.config.AppSettings;
import best.ccg.JbAdminApp.entity.*;
import best.ccg.JbAdminApp.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static best.ccg.JbAdminApp.service.LogDataService.getListLogData;

@Service
@Transactional
public class JBServerUtilizationLogJob {

    private static final Logger logger = LoggerFactory.getLogger(JBServerUtilizationLogJob.class);
    final JBServerProductUtilizationService jbServerProductUtilizationService;
    final JBServerComputerService jbServerComputerService;
    final JBServerEmployeeService jbServerEmployeeService;
    final JBServerProductBlacklistService jbServerProductBlacklistService;
    final JBServerLogUtilizationService jbServerLogUtilizationService;

    IdentifiedEmployeesService identifiedEmployeesService;
    IdentifiedComputersService identifiedComputersService;


    public JBServerUtilizationLogJob(JBServerProductUtilizationService jbServerProductUtilizationService, JBServerComputerService jbServerComputerService, JBServerEmployeeService jbServerEmployeeService, JBServerProductBlacklistService jbServerProductBlacklistService, JBServerLogUtilizationService jbServerLogUtilizationService) {
        this.jbServerProductUtilizationService = jbServerProductUtilizationService;
        this.jbServerComputerService = jbServerComputerService;
        this.jbServerEmployeeService = jbServerEmployeeService;
        this.jbServerProductBlacklistService = jbServerProductBlacklistService;
        this.jbServerLogUtilizationService = jbServerLogUtilizationService;
    }

    @Scheduled(cron = "0 0 * * * *")
    protected void ImportData3() {
        logger.warn("start");
        long start = System.currentTimeMillis();
        List<LogData> logDataList = getListLogData(AppSettings.appSettingsReportlink);
        if (logDataList != null) {
            try {
                Set<String> compNameList = new HashSet<>();
                Set<String> empNameList = new HashSet<>();
                Set<String> productBLList = new HashSet<>();
                Set<String> productUtilList = new HashSet<>();
                logger.debug("start logDataList 1 iterate");
                for (LogData logData : logDataList) {
                    compNameList.add(logData.getHostName());
                    empNameList.add(logData.getUserName());
                    productBLList.add(logData.getProductBL());
                    productUtilList.add(logData.getLicense() + ";" + logData.getProduct());
                }
                logger.debug("finish logDataList 1 iterate");
                HashMap<String, JBServerComputer> comps = jbServerComputerService.getByListNames(compNameList);
                HashMap<String, JBServerEmployee> employee = jbServerEmployeeService.getByListNames(empNameList);
                HashMap<String, JBServerProductBlacklist> productBlack = jbServerProductBlacklistService.getHashMapProductBlacklistBySetNames(productBLList);
                HashMap<String, JBServerProductUtilization> productUtil = jbServerProductUtilizationService.getByListNames(productUtilList, productBlack);

                checkEmployeeIdentifier(employee);
                checkComputerIdentifier(comps);

                Set<JBServerLogUtilization> jbServerLogUtilization = new HashSet<>();
                Set<JBServerEmployee> emp1 = new HashSet<>();
                Set<JBServerComputer> comp1 = new HashSet<>();
                logger.debug("start logDataList 2 iterate");

                for (LogData logData : logDataList) {

                    JBServerProductBlacklist bl = productBlack.get(logData.getProductBL());
                    JBServerComputer jbServerComputer = comps.get(logData.getHostName());
                    JBServerEmployee jbServerEmployee = employee.get(logData.getUserName());
                    JBServerProductUtilization jbServerProductUtilization = productUtil.get(logData.getLicense() + ":" + logData.getVersion());

                    if (jbServerComputer == null || jbServerEmployee == null || jbServerProductUtilization == null || bl == null) {
                        logger.error("jbServerComputer or jbServerEmployee or jbServerProductUtilization or bl is null. logdata:" + logData);
                        logger.error("bl:" + bl);
                        logger.error("jbServerComputer:" + jbServerComputer);
                        logger.error("jbServerEmployee:" + jbServerEmployee);
                        logger.error("jbServerProductUtilization:" + jbServerProductUtilization);

                        continue;
                    }
                    jbServerLogUtilization.add(new JBServerLogUtilization(LocalDateTime.now(), jbServerComputer, jbServerEmployee, jbServerProductUtilization));

                    setComputerUniqUtilProduct(bl, jbServerComputer);
                    setEmployeeUniqUtilProduct(bl, jbServerEmployee);
                    setEmployeeComputer(jbServerComputer, jbServerEmployee);

                    comp1.add(jbServerComputer);
                    emp1.add(jbServerEmployee);
                }

                logger.debug("finish logDataList 2 iterate");

                jbServerLogUtilizationService.saveAll(jbServerLogUtilization);
                jbServerEmployeeService.saveAll(emp1);
                jbServerComputerService.saveAll(comp1);

            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        } else {
            logger.debug("logDataList null");
        }

        long finish = System.currentTimeMillis() - start;
        logger.warn("execution time, ms: " + finish);

    }


    private void checkComputerIdentifier(HashMap<String, JBServerComputer> comps) {
        List<String> identifiedComputers = identifiedComputersService.findAll().stream().map(IdentifiedComputers::getName).collect(Collectors.toList());

        for (Map.Entry<String, JBServerComputer> entry : comps.entrySet()) {
            String key = entry.getKey();
            JBServerComputer value = entry.getValue();
            if (identifiedComputers.contains(key)) value.setIdentified(true);
        }
    }

    private void checkEmployeeIdentifier(HashMap<String, JBServerEmployee> employee) {
        List<String> identifiedEmployees = identifiedEmployeesService.findAll().stream().map(IdentifiedEmployees::getName).collect(Collectors.toList());
        for (Map.Entry<String, JBServerEmployee> entry : employee.entrySet()) {
            String key = entry.getKey();
            JBServerEmployee value = entry.getValue();
            if (identifiedEmployees.contains(key)) value.setIsIdentified(true);
        }
    }

    private void setEmployeeComputer(JBServerComputer jbServerComputer, JBServerEmployee jbServerEmployee) {
        if (jbServerEmployee.getJbServerComputers() != null) {
            jbServerEmployee.getJbServerComputers().add(jbServerComputer);
        } else {
            jbServerEmployee.setJbServerComputers(new HashSet<>(List.of(jbServerComputer)));
        }
    }

    private void setEmployeeUniqUtilProduct(JBServerProductBlacklist bl, JBServerEmployee jbServerEmployee) {
        if (jbServerEmployee.getUniqUtilProduct() != null) {
            jbServerEmployee.getUniqUtilProduct().add(bl);
        } else {
            jbServerEmployee.setUniqUtilProduct(new HashSet<>(List.of(bl)));
        }
        jbServerEmployee.setLastseenutildate(LocalDateTime.now());
    }

    private void setComputerUniqUtilProduct(JBServerProductBlacklist bl, JBServerComputer jbServerComputer) {
        if (jbServerComputer.getUniqUtilProduct() != null) jbServerComputer.getUniqUtilProduct().add(bl);
        else {
            jbServerComputer.setUniqUtilProduct(new HashSet<>(List.of(bl)));
        }
        jbServerComputer.setLastseenutildate(LocalDateTime.now());
    }


}