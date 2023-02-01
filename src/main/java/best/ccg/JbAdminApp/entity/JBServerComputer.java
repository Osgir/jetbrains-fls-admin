package best.ccg.JbAdminApp.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "JBServerComputer", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class JBServerComputer {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
    private boolean isIdentified = false;

    @Getter
    @Setter
    @ManyToMany(mappedBy = "jbServerComputers")
    Set<JBServerEmployee> jbServerEmployee;


    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "JBServerComputer_uniqUtilProduct",
            joinColumns = @JoinColumn(name = "copmuter_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    Set<JBServerProductBlacklist> uniqUtilProduct;

    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "JBServerComputer_uniqErrorProduct",
            joinColumns = @JoinColumn(name = "computer_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    Set<JBServerProductBlacklist> uniqErrorProduct;


    @Getter
    @Setter
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "jbServerComputer", cascade = CascadeType.ALL)
    private Set<JBServerBlacklistComputer> jbServerBlacklistProductSet = new LinkedHashSet<>();


    public JBServerComputer() {
    }

    public JBServerComputer(String name) {
        this.name = name.toUpperCase();
        this.creationdate = LocalDateTime.now();
        this.lastseenutildate = LocalDateTime.now();
        this.identificationname = getComputerIdentificationName(this.name);
    }


    public JBServerComputer(String name
            , Set<JBServerProductBlacklist> uniqUtilProduct
            , Set<JBServerProductBlacklist> uniqErrorProduct) {
        this.name = name.toUpperCase();
        this.creationdate = LocalDateTime.now();
        this.lastseenutildate = LocalDateTime.now();
        this.identificationname = getComputerIdentificationName(this.name);
        this.uniqUtilProduct = uniqUtilProduct;
        this.uniqErrorProduct = uniqErrorProduct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JBServerComputer that = (JBServerComputer) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static String getCompName(String str) {

        if (StringUtils.countMatches(str, "@") > 1) {
            int index = StringUtils.ordinalIndexOf(str, "@", 2) + 1;
            return str.substring(index).toUpperCase();
        } else {
            return str.substring(str.indexOf("@") + 1).toUpperCase();
        }

    }

    public static String getComputerIdentificationName(String name) {

        int dotcount = StringUtils.countMatches(name, ".");
        if (dotcount > 0) {
            if (StringUtils.isNumeric(name.substring(0, name.indexOf(".")))) {
                return name.toUpperCase(Locale.ROOT);
            } else {
                return name.substring(0, name.indexOf(".")).toUpperCase(Locale.ROOT);
            }

        }
        return name.toUpperCase(Locale.ROOT);
    }


    @Override
    public String toString() {
        return "JBServerComputer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isExclusioned=" + isExclusioned +
                ", creationdate=" + creationdate +
                ", lastseenutildate=" + lastseenutildate +
                ", lastseenerrordate=" + lastseenerrordate +
                ", identificationname='" + identificationname + '\'' +
                ", isIdentified=" + isIdentified +
                ", jbServerEmployee=" + jbServerEmployee +
                ", uniqUtilProduct=" + uniqUtilProduct +
                ", uniqErrorProduct=" + uniqErrorProduct +
                ", jbServerBlacklistProductSet=" + jbServerBlacklistProductSet +
                '}';
    }
}
