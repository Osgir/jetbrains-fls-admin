package best.ccg.JbAdminApp.service;

import best.ccg.JbAdminApp.entity.Role;
import best.ccg.JbAdminApp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public void save(Role theUser) {
        roleRepository.save(theUser);
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();

    }

    public Role getById(long theId) {
        return roleRepository.getById(theId);
    }

    public void deleteById(long theId) {
        roleRepository.deleteById(theId);
    }
}
