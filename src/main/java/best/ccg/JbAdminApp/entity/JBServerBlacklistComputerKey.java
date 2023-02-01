package best.ccg.JbAdminApp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class JBServerBlacklistComputerKey implements Serializable {
    public JBServerBlacklistComputerKey() {
    }

    @Getter
    @Setter
    @Column(name = "productblacklist_id")
    Long productBlacklistId;

    @Getter
    @Setter
    @Column(name = "computer_id")
    Long computerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JBServerBlacklistComputerKey that = (JBServerBlacklistComputerKey) o;
        return productBlacklistId.equals(that.productBlacklistId) && computerId.equals(that.computerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productBlacklistId, computerId);
    }
}