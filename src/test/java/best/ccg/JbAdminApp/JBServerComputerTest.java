package best.ccg.JbAdminApp;

import best.ccg.JbAdminApp.entity.JBServerComputer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JBServerComputerTest {

    @Test
    void getCompNameTest1() {
        String name = JBServerComputer.getCompName("127001@compname1.test.tt.ru");
        assertThat(name.equals("COMPNAME1.TEST.TT.RU")).isTrue();

        name = JBServerComputer.getCompName("127001@test.tt.ru@compname2.test.tt.ru");
        assertThat(name.equals("COMPNAME2.TEST.TT.RU")).isTrue();

        name = JBServerComputer.getCompName("login@127.0.0.1");
        assertThat(name.equals("127.0.0.1")).isTrue();

        name = JBServerComputer.getCompName("user@internets-MacBook-Pro.local");
        assertThat(name.equals("INTERNETS-MACBOOK-PRO.LOCAL")).isTrue();

    }

    @Test
    void getComputerIdentificationName() {
        String name = JBServerComputer.getComputerIdentificationName("compname1.test.tt.ru");
        System.out.println(name);
        assertThat(name.equals("COMPNAME1")).isTrue();

        name = JBServerComputer.getComputerIdentificationName("compname2");
        System.out.println(name);
        assertThat(name.equals("COMPNAME2")).isTrue();

        name = JBServerComputer.getComputerIdentificationName("tut.net.cc");
        System.out.println(name);
        assertThat(name.equals("TUT")).isTrue();

        name = JBServerComputer.getComputerIdentificationName("127.0.0.1");
        System.out.println(name);
        assertThat(name.equals("127.0.0.1")).isTrue();

        name = JBServerComputer.getComputerIdentificationName("internets-MacBook-Pro.local");
        System.out.println(name);
        assertThat(name.equals("INTERNETS-MACBOOK-PRO")).isTrue();
    }
}
