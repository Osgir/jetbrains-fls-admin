package best.ccg.JbAdminApp.service;

import best.ccg.JbAdminApp.entity.JBServerLogError;
import best.ccg.JbAdminApp.entity.TransientJbServerEmployeeGroupProduct;
import best.ccg.JbAdminApp.repository.JBServerLogErrorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class JBServerLogErrorService {

    private JBServerLogErrorRepository jbServerLogErrorRepository;

    @Autowired
    public void setJbServerErrorLogRepository(JBServerLogErrorRepository jbServerLogErrorRepository) {
        this.jbServerLogErrorRepository = jbServerLogErrorRepository;
    }

    public void saveAll(Set<JBServerLogError> jbServerLogErrorList) {
        jbServerLogErrorRepository.saveAll(jbServerLogErrorList);
    }

    public List<TransientJbServerEmployeeGroupProduct> getGroupByEmployeeProduct(List<Long> emp) {
        return jbServerLogErrorRepository.getGroupByEmployeeProduct(emp);
    }

    public LocalDateTime getMaxDatetime() {
        return jbServerLogErrorRepository.getMaxDatetime();
    }
}
