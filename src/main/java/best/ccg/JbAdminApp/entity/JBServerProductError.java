package best.ccg.JbAdminApp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class JBServerProductError {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @Getter
    @NotNull
    @Column(unique = true)
    private String valueP;

    @Getter
    @Setter
    @Column(name = "creationdate")
    private LocalDateTime creationdate;

    @Getter
    @Setter
    @NotNull
    @ManyToOne
    @JoinColumn(name = "jbServerProductBlacklist_id")
    private JBServerProductBlacklist jbServerProductBlacklist;

    public JBServerProductError() {
    }

    public JBServerProductError(String valueP, JBServerProductBlacklist jbServerProductBlacklist) {
        this.valueP = valueP;
        this.creationdate = LocalDateTime.now();
        this.jbServerProductBlacklist = jbServerProductBlacklist;
    }

    public JBServerProductError(String valueP) {
        this.valueP = valueP;
        this.creationdate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JBServerProductError that = (JBServerProductError) o;
        return valueP.equals(that.valueP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueP);
    }
}
