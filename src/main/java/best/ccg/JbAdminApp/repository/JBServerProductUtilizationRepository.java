package best.ccg.JbAdminApp.repository;

import best.ccg.JbAdminApp.entity.JBServerProductUtilization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JBServerProductUtilizationRepository extends JpaRepository<JBServerProductUtilization, Long> {
    JBServerProductUtilization saveAndFlush(JBServerProductUtilization jbServerProductUtilization);

    List<JBServerProductUtilization> findAll();
}
