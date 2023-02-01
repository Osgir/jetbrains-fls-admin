package best.ccg.JbAdminApp.service;

import best.ccg.JbAdminApp.entity.JBServerLogUtilization;
import best.ccg.JbAdminApp.entity.TransientJbServerEmployeeGroupProduct;
import best.ccg.JbAdminApp.repository.JBServerLogUtilizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class JBServerLogUtilizationService {

    private static final Logger logger = LoggerFactory.getLogger(JBServerLogUtilizationService.class);

    JBServerLogUtilizationRepository jbServerLogUtilizationRepository;
    JBServerProductUtilizationService jbServerProductUtilizationService;

    @Autowired
    public void setJbServerProductUtilizationService(JBServerProductUtilizationService jbServerProductUtilizationService) {
        this.jbServerProductUtilizationService = jbServerProductUtilizationService;
    }

    @Autowired
    public void setJbServerLogUtilizationRepository(JBServerLogUtilizationRepository jbServerLogUtilizationRepository) {
        this.jbServerLogUtilizationRepository = jbServerLogUtilizationRepository;
    }

    public void saveAll(Set<JBServerLogUtilization> jbServerLogErrorList) {
        logger.debug("start jbServerLogUtilization jbServerLogErrorList");
        try {
            jbServerLogUtilizationRepository.saveAll(jbServerLogErrorList);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        logger.debug("finish jbServerLogUtilization jbServerLogErrorList");
    }

    public List<TransientJbServerEmployeeGroupProduct> getGroupByEmployeeProduct(List<Long> emp) {
        return jbServerLogUtilizationRepository.getByJbServerEmployeeByGroupByEmployeeId(emp);
    }

}
