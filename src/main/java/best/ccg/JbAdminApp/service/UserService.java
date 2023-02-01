package best.ccg.JbAdminApp.service;


import best.ccg.JbAdminApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import best.ccg.JbAdminApp.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Objects;

@Service

public class UserService implements UserDetailsService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user == null) {

            throw new
                    UsernameNotFoundException("User not exist with name :" + username);
        }
        return user;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void save(User theUser) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String s = encoder.encode(theUser.getPassword());

        if (theUser.getId() == null) {
            theUser.setPassword(s);
        } else {
            User oldUser = userRepository.getById(theUser.getId());

            if (!Objects.equals(s, oldUser.getPassword())) {
                theUser.setPassword(s);
            }

        }
        userRepository.save(theUser);
    }


    public User getById(long theId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root).where(cb.equal(root.get("id"), theId));
        return entityManager.createQuery(cq)
                .getSingleResult();
    }

    public void deleteById(long theId) {
        userRepository.deleteById(theId);
    }
}