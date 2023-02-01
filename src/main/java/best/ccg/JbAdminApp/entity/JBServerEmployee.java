package best.ccg.JbAdminApp.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "JBServerEmployee", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class JBServerEmployee {
    @Id
    @Getter
    @GeneratedValue
    private Long id;

    @Getter
    @Setter
    @NotNull
    private String name;

    @Getter
    @Setter
    private Boolean isExclusioned = false;

    @Getter
    @Setter
    private LocalDateTime creationdate;

    @Getter
    @Setter
    private LocalDateTime lastseenutildate;

    @Getter
    @Setter
    private LocalDateTime lastseenerrordate;

    @Getter
    @Setter
    private String identificationname;

    @Getter
    @Setter
    private Boolean isIdentified = false;

    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<JBServerComputer> jbServerComputers;

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "jbServerEmployee", cascade = CascadeType.ALL)
    private Set<JBServerBlacklistEmployee> jbServerBlacklistProductSet = new LinkedHashSet<>();

    @Getter
    @Setter
    @OneToMany(mappedBy = "jbServerEmployee")
    private Set<JBServerLogUtilization> jbServerLogUtilizationSet;

    @Getter
    @Setter
    @OneToMany(mappedBy = "jbServerEmployee")
    private Set<JBServerLogError> jbServerLogErrors;

    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "JBServerEmployee_uniqUtilProduct",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    Set<JBServerProductBlacklist> uniqUtilProduct;

    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "JBServerEmployee_uniqErrorProduct",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    Set<JBServerProductBlacklist> uniqErrorProduct;


    public JBServerEmployee() {
    }

    public JBServerEmployee(String name, Set<JBServerProductBlacklist> uniqUtilProduct
            , Set<JBServerProductBlacklist> uniqErrorProduct
    ) {
        this.name = name.toUpperCase();
        this.creationdate = LocalDateTime.now();
        this.lastseenutildate = LocalDateTime.now();
        this.identificationname = getEmployeeIdentificationName(this.name);
        this.uniqUtilProduct = uniqUtilProduct;
        this.uniqErrorProduct = uniqErrorProduct;
    }

    public JBServerEmployee(String name) {
        this.name = name.toUpperCase();
        this.creationdate = LocalDateTime.now();
        this.lastseenutildate = LocalDateTime.now();
        this.identificationname = getEmployeeIdentificationName(this.name);
    }

    public static String getEmployeeName(String str) {
        if (StringUtils.countMatches(str, "@") > 1) {
            int index = StringUtils.ordinalIndexOf(str, "@", 2);
            return str.substring(0, index).toUpperCase();
        } else {
            return str.substring(0, str.indexOf("@")).toUpperCase();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JBServerEmployee that = (JBServerEmployee) o;
        return name.equals(that.name.toUpperCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toUpperCase());
    }


    public static String getEmployeeIdentificationName(String name) {

        String iname;
        if (StringUtils.countMatches(name, "@") > 0) {
            iname = name.substring(0, name.indexOf("@"));
        } else {

            int dotcount = StringUtils.countMatches(name, ".");
            if (dotcount == 0) {
                if (Character.isLetter(name.charAt(0)) && StringUtils.isNumeric(name.substring(1)))
                    iname = name.substring(1);
                else
                    iname = name;
            } else {

                iname = name.substring(0, name.indexOf("."));
            }
        }
        if (iname.endsWith("_"))
            iname = iname.substring(0, iname.length() - 1);
        return iname.toUpperCase();
    }

    @Override
    public String toString() {
        return "JBServerEmployee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isExclusioned=" + isExclusioned +
                ", creationdate=" + creationdate +
                ", lastseenutildate=" + lastseenutildate +
                ", lastseenerrordate=" + lastseenerrordate +
                ", identificationname='" + identificationname + '\'' +
                ", isIdentified=" + isIdentified +
                ", jbServerComputers=" + jbServerComputers +
                ", jbServerBlacklistProductSet=" + jbServerBlacklistProductSet +
                ", jbServerLogUtilizationSet=" + jbServerLogUtilizationSet +
                ", jbServerLogErrors=" + jbServerLogErrors +
                ", uniqUtilProduct=" + uniqUtilProduct +
                ", uniqErrorProduct=" + uniqErrorProduct +
                '}';
    }
}
