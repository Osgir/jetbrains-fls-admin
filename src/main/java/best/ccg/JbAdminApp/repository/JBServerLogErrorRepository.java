package best.ccg.JbAdminApp.repository;

import best.ccg.JbAdminApp.entity.JBServerLogError;
import best.ccg.JbAdminApp.entity.TransientJbServerEmployeeGroupLogDate;
import best.ccg.JbAdminApp.entity.TransientJbServerEmployeeGroupProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface JBServerLogErrorRepository extends JpaRepository<JBServerLogError, Long> {

    @Query("select s.dateTime from JBServerLogError s")
    Set<LocalDateTime> getJBServerLogErrorDateTime();

    @Query("select new best.ccg.JbAdminApp.entity.TransientJbServerEmployeeGroupLogDate(j.jbServerEmployee.id,max(j.dateTime) ) " + "from JBServerLogError j " + "group by j.jbServerEmployee.id")
    List<TransientJbServerEmployeeGroupLogDate> getMaxDatetimeGroupByEmployeeId();

    @Query("select new best.ccg.JbAdminApp.entity.TransientJbServerEmployeeGroupProduct(j.jbServerEmployee.id,pe.jbServerProductBlacklist) " + "from JBServerLogError j inner join JBServerProductError pe on j.jbServerProductError.id = pe.id " + "where j.jbServerEmployee.id IN (:emp) " + "group by j.jbServerEmployee,pe.jbServerProductBlacklist")
    List<TransientJbServerEmployeeGroupProduct> getGroupByEmployeeProduct(@Param("emp") List<Long> emp);

    @Query("select max(s.dateTime) from JBServerLogError s")
    LocalDateTime getMaxDatetime();
}
