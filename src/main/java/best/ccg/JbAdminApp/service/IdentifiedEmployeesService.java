package best.ccg.JbAdminApp.service;

import best.ccg.JbAdminApp.entity.IdentifiedEmployees;
import best.ccg.JbAdminApp.entity.JBServerEmployee;
import best.ccg.JbAdminApp.repository.IdentifiedEmployeesRepository;
import best.ccg.JbAdminApp.repository.JBServerEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class IdentifiedEmployeesService {

    private IdentifiedEmployeesRepository identifiedEmployeesRepository;
    JBServerEmployeeRepository jbServerEmployeeRepository;

    @Autowired
    public void setJbServerEmployeeRepository(JBServerEmployeeRepository jbServerEmployeeRepository) {
        this.jbServerEmployeeRepository = jbServerEmployeeRepository;
    }

    @Autowired
    public void setIdentifiedEmployeesRepository(IdentifiedEmployeesRepository identifiedEmployeesRepository) {
        this.identifiedEmployeesRepository = identifiedEmployeesRepository;
    }

    public void saveAll(Set<IdentifiedEmployees> identifiedEmployees) {
        identifiedEmployeesRepository.deleteAll();
        identifiedEmployeesRepository.saveAll(identifiedEmployees);
        updateJBServerComputersidentified();
    }

    public void updateJBServerComputersidentified() {
        List<JBServerEmployee> employee = jbServerEmployeeRepository.findAll();
        List<IdentifiedEmployees> identifiedEmployees = findAll();
        List<JBServerEmployee> iemployee = new ArrayList<>();

        identifiedEmployees.forEach(i -> iemployee.add(new JBServerEmployee(i.getName())));
        employee.forEach(c -> c.setIsIdentified(false));
        employee.retainAll(iemployee);
        employee.forEach(c -> c.setIsIdentified(true));
        jbServerEmployeeRepository.saveAll(employee);
    }


    public List<IdentifiedEmployees> findAll() {
        return identifiedEmployeesRepository.findAll();
    }
}
