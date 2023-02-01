package best.ccg.JbAdminApp.service;

import best.ccg.JbAdminApp.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LogProcessService {

    private static final Logger logger = LoggerFactory.getLogger(LogProcessService.class);


    JBServerEmployeeService jbServerEmployeeService;
    IdentifiedEmployeesService identifiedEmployeesService;

    JBServerComputerService jbServerComputerService;

    @Autowired
    public void setJbServerComputerService(JBServerComputerService jbServerComputerService) {
        this.jbServerComputerService = jbServerComputerService;
    }

    IdentifiedComputersService identifiedComputersService;

    @Autowired
    public void setIdentifiedComputersService(IdentifiedComputersService identifiedComputersService) {
        this.identifiedComputersService = identifiedComputersService;
    }

    @Autowired
    public void setIdentifiedEmployeesService(IdentifiedEmployeesService identifiedEmployeesService) {
        this.identifiedEmployeesService = identifiedEmployeesService;
    }

    @Autowired
    public void setJbServerEmployeeService(JBServerEmployeeService jbServerEmployeeService) {
        this.jbServerEmployeeService = jbServerEmployeeService;
    }

    public Map<String, JBServerEmployee> saveNewEmployeeAndCheckIdentifiered(Set<JBServerEmployee> jbServerEmployeeListNew) {

        try {
            List<String> identifiedEmployees = identifiedEmployeesService.findAll().stream().map(IdentifiedEmployees::getName).collect(Collectors.toList());

            Map<String, JBServerEmployee> dBEmployeeList;
            jbServerEmployeeListNew.forEach(e -> {
                if (identifiedEmployees.contains(e.getIdentificationname())) e.setIsIdentified(true);
            });
            jbServerEmployeeService.saveAll(jbServerEmployeeListNew);
            dBEmployeeList = jbServerEmployeeService.findAlltoHashmapEntity();
            return dBEmployeeList;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public Map<String, JBServerComputer> saveNewComputers(Set<JBServerComputer> jbServerComputerListNew) {

        try {
            List<String> identifiedComputers = identifiedComputersService.findAll().stream().map(IdentifiedComputers::getName).collect(Collectors.toList());
            Map<String, JBServerComputer> dBComputerList;
            jbServerComputerListNew.forEach(c -> {
                if (identifiedComputers.contains(c.getIdentificationname())) c.setIdentified(true);
            });
            jbServerComputerService.saveAll(jbServerComputerListNew);

            dBComputerList = jbServerComputerService.findAlltoHashmap();
            return dBComputerList;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public void updateExistComputers(Set<JBServerComputer> jbServerComputerListNew) {
        try {
            jbServerComputerService.saveAll(jbServerComputerListNew);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkComputers(Map<String, JBServerComputer> inMemoryComputerList
            , Set<JBServerComputer> jbServerComputerListNew
            , String name
            , JBServerProductBlacklist productUtil
            , JBServerProductBlacklist productError
            , LocalDateTime dateTime
            , Set<JBServerComputer> jbServerComputerListExist) {
        String hostKey = name.toUpperCase();
        try {
            if (!inMemoryComputerList.containsKey(hostKey)) {

                jbServerComputerListNew.add(new JBServerComputer(hostKey, getSetBl(productUtil), getSetBl(productError)));
            } else {
                JBServerComputer comp = inMemoryComputerList.get(hostKey);
                if (dateTime == null) {
                    comp.setLastseenutildate(LocalDateTime.now());
                    jbServerComputerListExist.add(comp);
                } else {
                    comp.setLastseenerrordate(dateTime);
                    comp.getUniqUtilProduct().add(productUtil);
                    comp.getUniqErrorProduct().add(productError);
                    jbServerComputerListExist.add(comp);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    public void checkEmployee(Map<String, JBServerEmployee> inMemoryEmployeeList
            , Set<JBServerEmployee> jbServerEmployeeListNew
            , String name
            , JBServerProductBlacklist productUtil
            , JBServerProductBlacklist productError
            , LocalDateTime dateTime) {
        String employeeKey = name.toUpperCase();

        try {
            if (!inMemoryEmployeeList.containsKey(employeeKey)) {
                jbServerEmployeeListNew.add(new JBServerEmployee(employeeKey, getSetBl(productUtil), getSetBl(productError)));
            } else {
                JBServerEmployee employee = inMemoryEmployeeList.get(employeeKey);
                if (dateTime == null) {
                    employee.setLastseenutildate(LocalDateTime.now());
                } else {
                    employee.setLastseenerrordate(dateTime);
                }

                if (employee.getUniqUtilProduct() == null) {
                    employee.setUniqUtilProduct(new HashSet<>(List.of(productUtil)));
                } else {
                    employee.getUniqUtilProduct().add(productUtil);
                }

                if (employee.getUniqErrorProduct() == null) {
                    Set<JBServerProductBlacklist> hash_Set = new HashSet<>();
                    hash_Set.add(productError);
                    employee.setUniqUtilProduct(hash_Set);
                } else {
                    employee.getUniqErrorProduct().add(productError);
                }

                jbServerEmployeeListNew.add(employee);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private Set<JBServerProductBlacklist> getSetBl(JBServerProductBlacklist jbServerProductBlacklist) {
        if (jbServerProductBlacklist == null) return null;
        Set<JBServerProductBlacklist> blSet = new HashSet<>();
        blSet.add(jbServerProductBlacklist);
        return blSet;
    }
}
