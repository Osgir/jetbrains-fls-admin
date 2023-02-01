package best.ccg.JbAdminApp.service;

import best.ccg.JbAdminApp.entity.JBServerBlacklistComputer;
import best.ccg.JbAdminApp.repository.JBServerBlacklistComputerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class JBServerBlacklistComputerService {


    private static final Logger logger
            = LoggerFactory.getLogger(JBServerBlacklistComputerService.class);


    private JBServerBlacklistComputerRepository jbServerBlacklistComputerRepository;

    @Autowired
    public void setJbServerBlacklistRepository(JBServerBlacklistComputerRepository jbServerBlacklistComputerRepository) {
        this.jbServerBlacklistComputerRepository = jbServerBlacklistComputerRepository;
    }

    public void save(JBServerBlacklistComputer JBServerBlacklistComputer) {
        try {
            jbServerBlacklistComputerRepository.save(JBServerBlacklistComputer);
        } catch (Exception err) {
            logger.error(err.fillInStackTrace().getMessage());
        }
    }

    public List<String> getComputerNameByProductid(Long id) {
        return jbServerBlacklistComputerRepository.getComputerNameByProductid(id);

    }

//    public List<TransientJbServerEmployeeGroupProduct> getGroupByEmployeeProduct(List<Long> ids) {
//        return jbServerBlacklistComputerRepository.getGroupByEmployeeProduct(ids);
//    }

    @Transactional
    public void deleteByComputerIdAndProductIdBlacklist(Long computerId_, Long jbServerProductBlacklist_id_) {
        jbServerBlacklistComputerRepository.deleteNew(computerId_, jbServerProductBlacklist_id_);
    }
}
