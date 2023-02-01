package best.ccg.JbAdminApp.repository;

import best.ccg.JbAdminApp.entity.JBServerLogUtilization;
import best.ccg.JbAdminApp.entity.TransientJbServerEmployeeGroupLogDate;
import best.ccg.JbAdminApp.entity.TransientJbServerEmployeeGroupProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JBServerLogUtilizationRepository extends JpaRepository<JBServerLogUtilization, Long> {

    @Query("select new best.ccg.JbAdminApp.entity.TransientJbServerEmployeeGroupLogDate(j.jbServerEmployee.id,max(j.dateTime)) " +
            "from JBServerLogUtilization j " +
            "group by j.jbServerEmployee.id")
    List<TransientJbServerEmployeeGroupLogDate> getMaxDatetimeGroupByEmployeeId();

    @Query("select new best.ccg.JbAdminApp.entity.TransientJbServerEmployeeGroupProduct(j.jbServerEmployee.id,pu.jbServerProductBlacklist) " +
            "from JBServerLogUtilization j inner join JBServerProductUtilization pu on j.jbServerProductUtilization.id = pu.id " +
            "where j.jbServerEmployee.id IN (:emp) " +
            "group by j.jbServerEmployee, pu.jbServerProductBlacklist")
    List<TransientJbServerEmployeeGroupProduct> getByJbServerEmployeeByGroupByEmployeeId(@Param("emp") List<Long> emp);
}