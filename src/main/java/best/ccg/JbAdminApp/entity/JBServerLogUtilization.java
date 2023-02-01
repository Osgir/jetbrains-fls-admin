package best.ccg.JbAdminApp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "JBServerLogUtilization"
        , uniqueConstraints = @UniqueConstraint(columnNames = {"dateTime", "jbServerProductUtilization_id", "jbServerComputer_id", "jbServerEmployee_id"})
        , indexes = {@Index(name = "uniqueIndexEmployee", columnList = "dateTime DESC,jbServerEmployee_id, jbServerProductUtilization_id", unique = true),
        @Index(name = "uniqueIndexComputer", columnList = "dateTime DESC,jbServerComputer_id, jbServerProductUtilization_id", unique = true)})
public class JBServerLogUtilization {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false, columnDefinition = "TIMESTAMP (3)")
    private LocalDateTime dateTime;

    @Getter
    @Setter
    @ManyToOne
    @NotNull
    private JBServerComputer jbServerComputer;

    @Getter
    @Setter
    @ManyToOne
    @NotNull
    private JBServerEmployee jbServerEmployee;

    @Getter
    @Setter
    @ManyToOne
    @NotNull
    private JBServerProductUtilization jbServerProductUtilization;

    public JBServerLogUtilization() {
        this.dateTime = LocalDateTime.now();
    }

    public JBServerLogUtilization(LocalDateTime dateTime, JBServerComputer jbServerComputer, JBServerEmployee jbServerEmployee, JBServerProductUtilization jbServerProductUtilization) {
        this.dateTime = dateTime;
        this.jbServerComputer = jbServerComputer;
        this.jbServerEmployee = jbServerEmployee;
        this.jbServerProductUtilization = jbServerProductUtilization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JBServerLogUtilization that = (JBServerLogUtilization) o;
        return dateTime.equals(that.dateTime)
                && jbServerComputer.equals(that.jbServerComputer)
                && jbServerEmployee.equals(that.jbServerEmployee)
                && jbServerProductUtilization.equals(that.jbServerProductUtilization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, jbServerComputer, jbServerEmployee, jbServerProductUtilization);
    }
}
