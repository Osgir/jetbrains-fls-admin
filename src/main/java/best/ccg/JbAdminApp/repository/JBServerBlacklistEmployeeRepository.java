package best.ccg.JbAdminApp.repository;

import best.ccg.JbAdminApp.entity.JBServerBlacklistEmployee;
import best.ccg.JbAdminApp.entity.TransientJbServerEmployeeGroupProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JBServerBlacklistEmployeeRepository extends JpaRepository<JBServerBlacklistEmployee, Long> {

    @Modifying
    @Query("delete from JBServerBlacklistEmployee e where e.jbServerEmployee.id=:emp_id and e.jbServerProductBlacklist.id =:prod_id")
    void deleteNew(@Param("emp_id") Long jbServerEmployee,
                   @Param("prod_id") Long jbServerProductBlacklist);


    @Query("select new best.ccg.JbAdminApp.entity.TransientJbServerEmployeeGroupProduct(j.jbServerEmployee.id,j.jbServerProductBlacklist) " +
            "from JBServerBlacklistEmployee j  " +
            "where j.jbServerEmployee.id IN (:emp) " +
            "group by j.jbServerEmployee,j.jbServerProductBlacklist")
    List<TransientJbServerEmployeeGroupProduct> getGroupByEmployeeProduct(@Param("emp") List<Long> emp);

    @Query("select e.name " +
            "from JBServerBlacklistEmployee j " +
            "inner join JBServerEmployee e on j.jbServerEmployee.id = e.id " +
            "where j.jbServerProductBlacklist.id = (:id) ")
    List<String> getEmployeeNameByProductid(Long id);

}
