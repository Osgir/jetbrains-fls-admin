package best.ccg.JbAdminApp.repository;

import best.ccg.JbAdminApp.entity.JBServerComputer;
import best.ccg.JbAdminApp.entity.JBServerEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JBServerEmployeeRepository extends JpaRepository<JBServerEmployee, Long> {
    JBServerEmployee saveAndFlush(JBServerEmployee jbServerEmployee);

    List<JBServerEmployee> findAllByNameContains(String name);

    Page<JBServerEmployee> findAllByNameContains(String name, Pageable pageable);

    @Query("Select copmuters from JBServerEmployee e inner join e.jbServerComputers copmuters where e.id = ?1")
    Page<JBServerComputer> findAllComputersByEmployee_Id(long empid, Pageable paging);

}
