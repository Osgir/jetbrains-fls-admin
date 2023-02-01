package best.ccg.JbAdminApp.repository;

import best.ccg.JbAdminApp.entity.IdentifiedComputers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentifiedComputersRepository extends JpaRepository<IdentifiedComputers, Long> {

}
