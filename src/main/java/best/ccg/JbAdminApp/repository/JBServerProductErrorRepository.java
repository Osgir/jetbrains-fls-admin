package best.ccg.JbAdminApp.repository;

import best.ccg.JbAdminApp.entity.JBServerProductError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JBServerProductErrorRepository extends JpaRepository<JBServerProductError, Long> {

    JBServerProductError saveAndFlush(JBServerProductError jbServerProductError);

    JBServerProductError getByValueP(String value);

}