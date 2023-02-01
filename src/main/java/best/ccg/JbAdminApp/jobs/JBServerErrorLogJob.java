package best.ccg.JbAdminApp.jobs;

import best.ccg.JbAdminApp.config.AppSettings;
import best.ccg.JbAdminApp.entity.*;
import best.ccg.JbAdminApp.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Service
@Transactional
public class JBServerErrorLogJob {

    private static final Logger logger = LoggerFactory.getLogger(JBServerErrorLogJob.class);

    @Autowired
    JBServerLogErrorService jbServerLogErrorService;
    @Autowired
    JBServerProductErrorService jbServerProductErrorService;
    @Autowired
    JBServerComputerService jbServerComputerService;
    @Autowired
    JBServerEmployeeService jbServerEmployeeService;
    @Autowired
    JBServerProductBlacklistService jbServerProductBlacklistService;
    @Autowired
    LogProcessService logProcessService;

    @Scheduled(cron = "0 0/6 * * * *")
//    @Bean
    protected void setListstringfromFile() {
        long start = System.currentTimeMillis();
        TreeMap<LocalDateTime, LogData> logData = getListLogdata(AppSettings.appSettingsErrorLogJobFilepath);
        if (logData != null) {
            Set<JBServerProductError> jbServerProductErrorListNew = new HashSet<>();
            Set<JBServerComputer> jbServerComputerListNew = new HashSet<>();
            Set<JBServerComputer> jbServerComputerListExist = new HashSet<>();
            Set<JBServerEmployee> jbServerEmployeeListNew = new HashSet<>();
            Set<JBServerLogError> jbServerLogErrorList = new HashSet<>();

            Map<String, JBServerProductError> dBProductList = jbServerProductErrorService.findAlltoHashmap();
            Map<String, JBServerComputer> dBComputerList = jbServerComputerService.findAlltoHashmap();
            Map<String, JBServerEmployee> dBEmployeeList = jbServerEmployeeService.findAlltoHashmapEntity();

            for (Map.Entry<LocalDateTime, LogData> entry : logData.entrySet()) {
                LogData log = entry.getValue();

                JBServerProductBlacklist jbServerProductBlacklist = getJbServerProductBlacklist(dBProductList, log);
                checkProduct(dBProductList, jbServerProductErrorListNew, log);
                logProcessService.checkComputers(dBComputerList, jbServerComputerListNew, log.hostName, null, jbServerProductBlacklist, log.dateTime, jbServerComputerListExist);
                logProcessService.checkEmployee(dBEmployeeList, jbServerEmployeeListNew, log.userName, null, jbServerProductBlacklist, log.dateTime);
            }

            if (jbServerComputerListNew.size() > 0) {
                dBComputerList = logProcessService.saveNewComputers(jbServerComputerListNew);
            }
            if (jbServerComputerListExist.size() > 0) {
                logProcessService.updateExistComputers(jbServerComputerListExist);
            }
            if (jbServerEmployeeListNew.size() > 0) {
                dBEmployeeList = logProcessService.saveNewEmployeeAndCheckIdentifiered(jbServerEmployeeListNew);
            }
            if (jbServerProductErrorListNew.size() > 0) {
                dBProductList = getStringJBServerProductErrorMap(jbServerProductErrorListNew);
            }

            Set<JBServerEmployee> employeeComputerRelations = new HashSet<>();
            for (Map.Entry<LocalDateTime, LogData> entry : logData.entrySet()) {

                LogData log = entry.getValue();

                JBServerComputer jbServerComputer = dBComputerList.get(log.hostName);
                JBServerEmployee jbServerEmployee = dBEmployeeList.get(log.userName);
                jbServerEmployeeService.checkEmployeeComputerRelations(employeeComputerRelations, jbServerComputer, jbServerEmployee);
                jbServerLogErrorList.add(new JBServerLogError(log.dateTime, log.errorString, dBProductList.get(log.productFamilyId), jbServerEmployee, jbServerComputer));

            }
            jbServerLogErrorService.saveAll(jbServerLogErrorList);

            if (!CollectionUtils.isEmpty(employeeComputerRelations)) {
                jbServerEmployeeService.saveAll(employeeComputerRelations);
            }
        } else {
            logger.error("stringList empty, or no new rercord");
        }

        long finish = System.currentTimeMillis() - start;
        logger.warn("execution time, ms: " + finish);
    }

    private LocalDateTime getLastDBRecord() {
        LocalDateTime t = jbServerLogErrorService.getMaxDatetime();
        if (t == null) t = LocalDateTime.now().minusMonths(12);
        return t;
    }

    public static void checkProduct(Map<String, JBServerProductError> dBProductList
            , Set<JBServerProductError> jbServerProductErrorListNew
            , LogData v) {
        String productKey = v.productFamilyId;
        if (!dBProductList.containsKey(productKey)) {
            jbServerProductErrorListNew.add(new JBServerProductError(productKey));
        }
    }

    private TreeMap<LocalDateTime, LogData> getListLogdata(String filePath) {
        try {
            BufferedReader log_file_reader = new BufferedReader(new FileReader(filePath));
            String str;
            TreeMap<LocalDateTime, LogData> logDataMap = new TreeMap<>();
            LocalDateTime lastDbrecord = getLastDBRecord();
            while ((str = log_file_reader.readLine()) != null) {
                if (str.indexOf("Tickets obtaining prohibited for ObtainTicketRequest") > 0) {
                    LocalDateTime dateTime = getLocalDateTime(str.substring(1, 24));
                    if (dateTime.isAfter(lastDbrecord)) {
                        LogData logData = new LogData(
                                dateTime
                                , getSubstringByValue(str, "productFamilyId").toUpperCase()
                                , getSubstringByValue(str, "userName").toUpperCase()
                                , JBServerComputer.getCompName(getSubstringByValue(str, "hostName"))
                                , str);
                        logDataMap.put(dateTime, logData);
                    }
                }
            }
            log_file_reader.close();
            return logDataMap;
        } catch (Exception se) {
            logger.error(se.getMessage());
        }
        logger.error("file not found" + filePath);
        return null;
    }

    private Map<String, JBServerProductError> getStringJBServerProductErrorMap(Set<JBServerProductError> jbServerProductErrorListNew) {
        Map<String, JBServerProductError> dBProductList;
        jbServerProductErrorService.saveAll(jbServerProductErrorListNew);
        dBProductList = jbServerProductErrorService.findAlltoHashmap();
        return dBProductList;
    }

    private JBServerProductBlacklist getJbServerProductBlacklist(Map<String, JBServerProductError> dBProductList, LogData log) {
        JBServerProductBlacklist jbServerProductBlacklist;
        JBServerProductError jbServerProductError = dBProductList.get(log.productFamilyId);
        if (jbServerProductError == null) {
            jbServerProductErrorService.saveAndFlush(new JBServerProductError(log.productFamilyId.toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("All")));
            dBProductList = jbServerProductErrorService.findAlltoHashmap();
            jbServerProductError = dBProductList.get(log.productFamilyId);
        }

        jbServerProductBlacklist = jbServerProductError.getJbServerProductBlacklist();
        return jbServerProductBlacklist;
    }

    private static String getSubstringByValue(String vStr, String vSearchWord) {
        int i = vStr.indexOf(vSearchWord);
        int start = vStr.substring(i).indexOf("'") + 1;
        int finish = vStr.substring(i + start).indexOf("'");
        return vStr.substring(i + start, i + start + finish);
    }

    public static class LogData {
        String userName;
        String hostName;
        LocalDateTime dateTime;
        String productFamilyId;
        String errorString;

        public LogData(LocalDateTime dateTime, String productFamilyId, String userName, String hostName, String errorString) {
            this.dateTime = dateTime;
            this.productFamilyId = productFamilyId;
            this.userName = userName;
            this.hostName = hostName;
            this.errorString = errorString;
        }
    }

    public static LocalDateTime getLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
        return LocalDateTime.parse(date, formatter);
    }


}

