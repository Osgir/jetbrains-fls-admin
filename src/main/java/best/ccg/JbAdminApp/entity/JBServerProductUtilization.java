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
public class JBServerProductUtilization {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @Getter
    private String license;

    @Setter
    @Getter
    private String productstring;

    @Setter
    @Getter
    private String version;

    @Setter
    @Getter
    private Boolean isActual;

    @Getter
    @Setter
    @Column(name = "creationdate")
    private LocalDateTime creationdate;

    @Getter
    @Setter
    @ManyToOne
    @NotNull
    JBServerProductBlacklist jbServerProductBlacklist;

    @Setter
    @Getter
    @OneToMany(mappedBy = "jbServerProductUtilization")
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<JBServerLogUtilization> jbServerLogUtilization;

    public JBServerProductUtilization() {
        this.creationdate = LocalDateTime.now();
    }

    @Deprecated
    public JBServerProductUtilization(String license, JBServerProductBlacklist jbServerProductBlacklist, String version) {
        this.license = license;
        this.creationdate = LocalDateTime.now();
        this.jbServerProductBlacklist = jbServerProductBlacklist;
        this.version = version;

    }

    public JBServerProductUtilization(String license,
                                      JBServerProductBlacklist jbServerProductBlacklist,
                                      String version,
                                      String productstring) {
        this.license = license;
        this.creationdate = LocalDateTime.now();
        this.jbServerProductBlacklist = jbServerProductBlacklist;
        this.version = version;
        this.productstring = productstring;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JBServerProductUtilization that = (JBServerProductUtilization) o;
        return license.equals(that.license) && Objects.equals(version, that.version) && jbServerProductBlacklist.equals(that.jbServerProductBlacklist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(license, version, jbServerProductBlacklist);
    }

    @Override
    public String toString() {
        return "JBServerProductUtilization{" +
                "id=" + id +
                ", license='" + license + '\'' +
                ", productstring='" + productstring + '\'' +
                ", version='" + version + '\'' +
                ", creationdate=" + creationdate +
                ", jbServerProductBlacklist=" + jbServerProductBlacklist +
                ", jbServerLogUtilization=" + jbServerLogUtilization +
                '}';
    }
}
