package best.ccg.JbAdminApp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class IdentifiedComputers {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Getter
    @Setter
    @NotNull
    private String name;

    public IdentifiedComputers(String name) {
        this.name = name;
    }

    public IdentifiedComputers() {
    }
}
