package best.ccg.JbAdminApp.service;

import best.ccg.JbAdminApp.entity.JBServerBlacklistEmployee;
import best.ccg.JbAdminApp.entity.TransientJbServerEmployeeGroupProduct;
import best.ccg.JbAdminApp.repository.JBServerBlacklistEmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class JBServerBlacklistEmployeeService {


    private static final Logger logger
            = LoggerFactory.getLogger(JBServerBlacklistEmployeeService.class);
    JBServerBlacklistEmployeeRepository jbServerBlacklistEmployeeRepository;

    @Autowired
    public void setJbServerBlacklistEmployeeRepository(JBServerBlacklistEmployeeRepository jbServerBlacklistEmployeeRepository) {
        this.jbServerBlacklistEmployeeRepository = jbServerBlacklistEmployeeRepository;
    }

    public void save(JBServerBlacklistEmployee blacklist) {
        jbServerBlacklistEmployeeRepository.save(blacklist);
    }

    @Transactional
    public void deleteByIdEmployeeIdAndIdProductBlacklistId(Long jbServerEmployee, Long jbServerProductBlacklist) {
        jbServerBlacklistEmployeeRepository.deleteNew(jbServerEmployee, jbServerProductBlacklist);
    }

    public List<TransientJbServerEmployeeGroupProduct> getGroupByEmployeeProduct(List<Long> emp) {
        return jbServerBlacklistEmployeeRepository.getGroupByEmployeeProduct(emp);
    }

    public List<String> getEmployeeNameByProductid(Long id) {
        return jbServerBlacklistEmployeeRepository.getEmployeeNameByProductid(id);
    }

    public void delete(JBServerBlacklistEmployee bl) {
        jbServerBlacklistEmployeeRepository.delete(bl);
    }
}
