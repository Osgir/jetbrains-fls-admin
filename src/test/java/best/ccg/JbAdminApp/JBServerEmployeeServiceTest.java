package best.ccg.JbAdminApp;

import best.ccg.JbAdminApp.entity.JBServerComputer;
import best.ccg.JbAdminApp.entity.JBServerEmployee;
import best.ccg.JbAdminApp.service.JBServerEmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JBServerEmployeeServiceTest {

    @Autowired
    JBServerEmployeeService jbServerEmployeeService;

    @Test
    void checkEmployeeComputerRelationsTest() {
        Set<JBServerEmployee> employeeComputerRelations = new HashSet<>();
        JBServerComputer jbServerComputer = new JBServerComputer("testcomputer1");
        JBServerEmployee jbServerEmployee = new JBServerEmployee("testuser1");
        jbServerEmployeeService.checkEmployeeComputerRelations(employeeComputerRelations, jbServerComputer, jbServerEmployee);
        assertThat(employeeComputerRelations.size() == 2).isFalse();
        assertThat(employeeComputerRelations.size() == 1).isTrue();
    }

}