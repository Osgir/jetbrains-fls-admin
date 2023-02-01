package best.ccg.JbAdminApp.service;

import best.ccg.JbAdminApp.entity.IdentifiedComputers;
import best.ccg.JbAdminApp.entity.JBServerComputer;
import best.ccg.JbAdminApp.repository.IdentifiedComputersRepository;
import best.ccg.JbAdminApp.repository.JBServerComputerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class IdentifiedComputersService {

    private IdentifiedComputersRepository identifiedComputersRepository;
    private JBServerComputerRepository jbServerComputerRepository;

    @Autowired
    public void setJbServerComputerRepository(JBServerComputerRepository jbServerComputerRepository) {
        this.jbServerComputerRepository = jbServerComputerRepository;
    }

    @Autowired
    public void setIdentifiedComputersRepository(IdentifiedComputersRepository identifiedComputersRepository) {
        this.identifiedComputersRepository = identifiedComputersRepository;
    }

    public void saveAll(Set<IdentifiedComputers> identifiedComputers) {
        identifiedComputersRepository.deleteAll();
        identifiedComputersRepository.saveAll(identifiedComputers);
        updateJBServerComputersidentified();
    }

    public void updateJBServerComputersidentified() {
        List<JBServerComputer> computers = jbServerComputerRepository.findAll();
        List<IdentifiedComputers> identifiedComputers = findAll();
        List<JBServerComputer> icomputers = new ArrayList<>();
        identifiedComputers.forEach(i -> icomputers.add(new JBServerComputer(i.getName())));
        computers.forEach(c -> c.setIdentified(false));
        computers.retainAll(icomputers);
        computers.forEach(c -> c.setIdentified(true));
        jbServerComputerRepository.saveAll(computers);
    }

    public List<IdentifiedComputers> findAll() {
        return identifiedComputersRepository.findAll();
    }
}
