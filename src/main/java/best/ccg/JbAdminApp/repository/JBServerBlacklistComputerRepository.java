package best.ccg.JbAdminApp.repository;

import best.ccg.JbAdminApp.entity.JBServerBlacklistComputer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JBServerBlacklistComputerRepository extends JpaRepository<JBServerBlacklistComputer, Long> {
    @Modifying
    @Query("delete from JBServerBlacklistComputer e where e.jbServerComputer.id=:comp_id and e.jbServerProductBlacklist.id =:prod_id")
    int deleteNew(@Param("comp_id") Long jbServerComputer,
                  @Param("prod_id") Long jbServerProductBlacklist);

    @Query("select e.name " +
            "from JBServerBlacklistComputer j " +
            "inner join JBServerComputer e on j.jbServerComputer.id = e.id " +
            "where j.jbServerProductBlacklist.id = (:id) ")
    List<String> getComputerNameByProductid(Long id);
}