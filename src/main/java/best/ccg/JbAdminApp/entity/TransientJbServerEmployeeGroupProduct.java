package best.ccg.JbAdminApp.entity;

import lombok.Getter;
import lombok.Setter;

public class TransientJbServerEmployeeGroupProduct {
    @Getter
    @Setter
    Long id;
    @Getter
    @Setter
    JBServerProductBlacklist productBlacklist;


    public TransientJbServerEmployeeGroupProduct(Long id, JBServerProductBlacklist productBlacklist) {
        this.id = id;
        this.productBlacklist = productBlacklist;
    }

    @Override
    public String toString() {
        return "ReportJbServerEmployeeGroupProduct{" +
                "eployee=" + id +
                ", productid=" + productBlacklist +
                '}';
    }
}
