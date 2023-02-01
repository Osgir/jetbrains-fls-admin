package best.ccg.JbAdminApp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class JBServerBlacklistComputer {
    public JBServerBlacklistComputer() {
    }

    public JBServerBlacklistComputer(JBServerComputer jbServerComputer, JBServerProductBlacklist jbServerProductBlacklist) {
        this.creationdate = LocalDateTime.now();
        this.jbServerComputer = jbServerComputer;
        this.jbServerProductBlacklist = jbServerProductBlacklist;
    }

    @EmbeddedId
    private JBServerBlacklistComputerKey id = new JBServerBlacklistComputerKey();
    @Getter
    @Setter
    private LocalDateTime creationdate;
    @Getter
    @Setter
    @ManyToOne
    @MapsId("computerId")
    @JoinColumn(name = "computer_id")
    private JBServerComputer jbServerComputer;
    @Getter
    @Setter
    @ManyToOne
    @MapsId("productBlacklistId")
    @JoinColumn(name = "productblacklist_id")
    private JBServerProductBlacklist jbServerProductBlacklist;
}