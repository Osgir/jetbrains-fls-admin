package best.ccg.JbAdminApp;

import best.ccg.JbAdminApp.entity.JBServerEmployee;
import best.ccg.JbAdminApp.service.JBServerEmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JBServerEmployeeTest {

    @Autowired
    JBServerEmployeeService jbServerEmployeeService;

    @Test
    void getEmployeeNameTest() {
        String name = JBServerEmployee.getEmployeeName("127001@compname.test.tt.ru");
        assertThat(name.equals("127001")).isTrue();

        name = JBServerEmployee.getEmployeeName("127001@test.tt.ru@compname.test.tt.ru");
        assertThat(name.equals("127001@TEST.TT.RU")).isTrue();


        name = JBServerEmployee.getEmployeeName("user@internets-MacBook-Pro.local");
        assertThat(name.equals("USER")).isTrue();
    }

    @Test
    void getEmployeeIdentificationNameTest() {
        String name = JBServerEmployee.getEmployeeIdentificationName("127001");
        System.out.println(name);
        assertThat(name.equals("127001")).isTrue();

        name = JBServerEmployee.getEmployeeIdentificationName("127001@test.tt.ru");
        System.out.println(name);
        assertThat(name.equals("127001")).isTrue();

        name = JBServerEmployee.getEmployeeIdentificationName("login@test.tt.ru");
        System.out.println(name);
        assertThat(name.equals("LOGIN")).isTrue();

        name = JBServerEmployee.getEmployeeIdentificationName("login_@test.tt.ru");
        System.out.println(name);
        assertThat(name.equals("LOGIN")).isTrue();

        name = JBServerEmployee.getEmployeeIdentificationName("login_");
        System.out.println(name);
        assertThat(name.equals("LOGIN")).isTrue();

        name = JBServerEmployee.getEmployeeIdentificationName("user@internets-MacBook-Pro.local");
        System.out.println(name);
        assertThat(name.equals("USER")).isTrue();
    }

    @Test
    void getStr() {
        int page = 1;
        int size = 5;
        int sort = 1;

        String s = "lastseenutildate";
        if (sort == 1) s = "lastseenerrordate";

        Pageable paging = PageRequest.of(page, size, Sort.by("isIdentified").and(Sort.by(s).descending()));
        JBServerEmployeeService.FindData bb = jbServerEmployeeService.getEmployeeIds("username", paging, 1);
        System.out.println(bb.getListData());
        assertThat(bb.getListData()).isNotNull();

        sort = 0;
        s = "lastseenutildate";
        if (sort == 1) s = "lastseenerrordate";
        paging = PageRequest.of(page, size, Sort.by("isIdentified").and(Sort.by(s).descending()));
        JBServerEmployeeService.FindData bb2 = jbServerEmployeeService.getEmployeeIds("noThisname", paging, 0);
        assertThat(bb2).isNull();

    }
}
