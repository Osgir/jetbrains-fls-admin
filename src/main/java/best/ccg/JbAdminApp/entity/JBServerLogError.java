package best.ccg.JbAdminApp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "JBServerLogError", uniqueConstraints = @UniqueConstraint(columnNames = {"dateTime",
        "jbServerProductError_id",
        "jbServerComputer_id",
        "jbServerEmployee_id"}))
public class JBServerLogError {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false, columnDefinition = "TIMESTAMP (3)")
    private LocalDateTime dateTime;

    @Setter
    @Getter
    @Column(length = 2000)
    private String errorString;

    @Setter
    @Getter
    @ManyToOne
    private JBServerProductError jbServerProductError;

    @Setter
    @Getter
    @ManyToOne
    JBServerEmployee jbServerEmployee;

    @Setter
    @Getter
    @ManyToOne
    JBServerComputer jbServerComputer;

    public JBServerLogError() {
    }

    public JBServerLogError(LocalDateTime dateTime, String errorString, JBServerProductError jbServerProductError, JBServerEmployee jbServerEmployee, JBServerComputer jbServerComputer) {
        this.dateTime = dateTime;
        this.errorString = errorString;
        this.jbServerProductError = jbServerProductError;
        this.jbServerEmployee = jbServerEmployee;
        this.jbServerComputer = jbServerComputer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JBServerLogError that = (JBServerLogError) o;
        return dateTime.equals(that.dateTime) &&
                jbServerProductError.equals(that.jbServerProductError) &&
                jbServerEmployee.equals(that.jbServerEmployee) &&
                jbServerComputer.equals(that.jbServerComputer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, jbServerProductError, jbServerEmployee, jbServerComputer);
    }
}