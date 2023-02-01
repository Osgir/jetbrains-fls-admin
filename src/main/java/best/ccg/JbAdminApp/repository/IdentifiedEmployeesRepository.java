package best.ccg.JbAdminApp.repository;

import best.ccg.JbAdminApp.entity.IdentifiedEmployees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentifiedEmployeesRepository extends JpaRepository<IdentifiedEmployees, Long>  {


}
