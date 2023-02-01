package best.ccg.JbAdminApp.repository;

import best.ccg.JbAdminApp.entity.JBServerComputer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JBServerComputerRepository extends JpaRepository<JBServerComputer, Long> {

    List<JBServerComputer> getByName(String hostName);

    List<JBServerComputer> findByName(String hostName);

}