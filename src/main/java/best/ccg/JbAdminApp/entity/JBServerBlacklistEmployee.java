package best.ccg.JbAdminApp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class JBServerBlacklistEmployee {
    public JBServerBlacklistEmployee() {
    }

    public JBServerBlacklistEmployee(JBServerEmployee jbServerEmployee, JBServerProductBlacklist jbServerProductBlacklist) {
        this.creationdate = LocalDateTime.now();
        this.jbServerEmployee = jbServerEmployee;
        this.jbServerProductBlacklist = jbServerProductBlacklist;
    }

    @EmbeddedId
    private JBServerBlacklistEmployeeKey id = new JBServerBlacklistEmployeeKey();
    @Getter
    @Setter
    private LocalDateTime creationdate;

    @Getter
    @Setter
    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private JBServerEmployee jbServerEmployee;

    @Getter
    @Setter
    @ManyToOne
    @MapsId("productBlacklistId")
    @JoinColumn(name = "productblacklist_id")
    private JBServerProductBlacklist jbServerProductBlacklist;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}