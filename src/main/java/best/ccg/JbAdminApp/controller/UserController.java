package best.ccg.JbAdminApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import best.ccg.JbAdminApp.entity.Role;
import best.ccg.JbAdminApp.entity.User;
import best.ccg.JbAdminApp.service.RoleService;
import best.ccg.JbAdminApp.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @GetMapping("/list")
    public String listUsers(Model theModel) {
        List<User> theUsers = userService.getUsers();
        theModel.addAttribute("users", theUsers);
        return "list-users";
    }

    @GetMapping("/new")
    public String showFormForAdd(@ModelAttribute User user, Model theModel) {
        User theUser = new User();
        List<Role> roles = roleService.getRoles();
        theModel.addAttribute("user", theUser);
        theModel.addAttribute("roles", roles);
        return "UserForm";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User theUser) {
        userService.save(theUser);
        return "redirect:/user/list";
    }

    @GetMapping("/edit")
    public String showFormForUpdate(@RequestParam("userId") long theId, Model theModel) {
        User theUser = userService.getById(theId);
        List<Role> roles = roleService.getRoles();

        theModel.addAttribute("user", theUser);
        theModel.addAttribute("roles", roles);
        return "UserForm";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("userId") long theId) {
        userService.deleteById(theId);
        return "redirect:/user/list";
    }

    protected static String getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }


}