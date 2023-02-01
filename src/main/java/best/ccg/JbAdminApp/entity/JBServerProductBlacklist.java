package best.ccg.JbAdminApp.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
public class JBServerProductBlacklist {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @Getter
    @NotNull
    @Column(unique = true)
    private String productName;

    @Setter
    @Getter
    @NotNull
    @Column(unique = true)
    private String valueP;

    @Getter
    @Setter

    @Column(name = "creationdate")
    private LocalDateTime creationdate;


    @OneToMany(mappedBy = "jbServerProductBlacklist")
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<JBServerProductUtilization> jbServerProductUtilizationList;


    @OneToMany(mappedBy = "jbServerProductBlacklist")
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<JBServerProductError> jbServerProductErrorList;


    @OneToMany(mappedBy = "jbServerProductBlacklist")
    @Fetch(value = FetchMode.SUBSELECT)
    Set<JBServerBlacklistEmployee> jbServerBlacklistEmployeeSet;


    @OneToMany(mappedBy = "jbServerProductBlacklist")
    @Fetch(value = FetchMode.SUBSELECT)
    Set<JBServerBlacklistEmployee> jbServerBlacklistComputerSet;


    public JBServerProductBlacklist() {

    }

    public JBServerProductBlacklist(Long id, String valueP) {
        this.id = id;
        this.valueP = valueP;
    }

    public JBServerProductBlacklist(String valueP, String productName) {
        this.valueP = valueP;
        this.productName = productName;
        this.creationdate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JBServerProductBlacklist that = (JBServerProductBlacklist) o;
        return valueP.equals(that.valueP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueP);
    }

    @Override
    public String toString() {
        return "JBServerProductBlacklist{" +
                "id=" + id +
                ", name='" + productName + '\'' +
                ", valuep='" + valueP + '\'' +
                ", creationdate=" + creationdate +
                ", jbServerProductUtilizationList=" + jbServerProductUtilizationList +
                ", jbServerProductErrorList=" + jbServerProductErrorList +
                ", jbServerBlacklistEmployeeSet=" + jbServerBlacklistEmployeeSet +
                ", jbServerBlacklistComputerSet=" + jbServerBlacklistComputerSet +
                '}';
    }
}
