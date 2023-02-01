package best.ccg.JbAdminApp.repository;

import best.ccg.JbAdminApp.entity.JBServerProductBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JBServerProductBlacklistRepository extends JpaRepository<JBServerProductBlacklist, Long> {

    @Query("select new JBServerProductBlacklist (j.id,j.valueP) from JBServerProductBlacklist j " +
            "group by j.id,j.valueP ")
    List<JBServerProductBlacklist> findIdValue();
}