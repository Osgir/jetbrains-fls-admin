package best.ccg.JbAdminApp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class JBServerBlacklistEmployeeKey implements Serializable {
    public JBServerBlacklistEmployeeKey() {
    }

    @Getter
    @Setter
    @Column(name = "productblacklist_id")
    Long productBlacklistId;

    @Getter
    @Setter
    @Column(name = "employee_id")
    Long employeeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JBServerBlacklistEmployeeKey that = (JBServerBlacklistEmployeeKey) o;
        return productBlacklistId.equals(that.productBlacklistId) && employeeId.equals(that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productBlacklistId, employeeId);
    }
}
