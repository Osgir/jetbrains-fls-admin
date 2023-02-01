package best.ccg.JbAdminApp.config;

import best.ccg.JbAdminApp.entity.*;
import best.ccg.JbAdminApp.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class DBInit {

    private UserService userService;
    private RoleService roleService;

    private JBServerProductBlacklistService jbServerProductBlacklistService;
    private JBServerProductErrorService jbServerProductErrorService;
    private JBServerProductUtilizationService jbServerProductUtilizationService;

    private static final Logger logger = LoggerFactory.getLogger(DBInit.class);

    @Autowired
    public void setJbServerProductUtilizationService(JBServerProductUtilizationService jbServerProductUtilizationService) {
        this.jbServerProductUtilizationService = jbServerProductUtilizationService;
    }

    @Autowired
    public void setJbServerProductErrorService(JBServerProductErrorService jbServerProductErrorService) {
        this.jbServerProductErrorService = jbServerProductErrorService;
    }

    @Autowired
    public void setJbServerProductBlacklistService(JBServerProductBlacklistService jbServerProductBlacklistService) {
        this.jbServerProductBlacklistService = jbServerProductBlacklistService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }


    @PostConstruct
    private void postConstruct() {
        if (userService.getUsers().size() < 1)
            if (roleService.getRoles().size() < 1) {
                String pass = PasswordGenerator.generateStrongPassword();
                logger.error("admin password: " + pass);
                roleService.save(new Role(1L, "ROLE_ADMIN"));
                roleService.save(new Role(2L, "ROLE_USER"));
                roleService.save(new Role(3L, "ROLE_JBSERVER"));

                Role role = roleService.getById(1L);
                List<Role> roleSet = roleService.getRoles();
                userService.save(new User("admin", pass, roleSet));
            }
        if (jbServerProductBlacklistService.findAll().size() < 1) {
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("ALL", "All Products Pack"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("AC", "AppCode"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("CL", "CLion"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("DB", "DataGrip"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("DS", "DataSpell"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("DC", "dotCover"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("DM", "dotMemory"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("DPN", "dotTrace"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("DUL", "dotUltimate"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("GO", "GoLand"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("II", "IntelliJ IDEA"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("PS", "PhpStorm"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("PC", "PyCharm"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("RSC", "ReSharper"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("RSU", "ReSharper Ultimate"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("RC", "ReSharper C++"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("RRR", "ReSharper Ultimate + Rider"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("RD", "Rider"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("RM", "RubyMine"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("WS", "WebStorm"));
            jbServerProductBlacklistService.save(new JBServerProductBlacklist("PCWMP", "CodeWithMe"));
        }
        if (jbServerProductUtilizationService.findAll().size() < 1) {
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("AppCode Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("AC"), "2021.3","AC:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("CLion Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("CL"), "2021.3","CL:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("DataGrip Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("DB"), "2021.3","DB:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("DataSpell Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("DS"), "2021.3","DS:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("dotCover Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("DC"), "2021.3","DC:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("dotUltimate Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("DM"), "2021.3","DM:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("dotUltimate Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("DPN"), "2021.3","DPN:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("dotUltimate Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("DUL"), "2021.3","DUL:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("GoLand Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("GO"), "2021.3","GO:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("IntelliJ IDEA Ultimate Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("II"), "2021.3","II:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("PhpStorm Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("PS"), "2021.3","PS:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("PyCharm Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("PC"), "2021.3","PC:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("ReSharper Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("RSC"), "2021.3","RSC:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("dotUltimate Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("RD"), "2021.3","RD:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("ReSharper C++ Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("RC"), "2021.3","RC:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("ReSharper Ultimate + Rider Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("RRR"), "2021.3","RRR:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("Rider Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("RD"), "2021.3","RD:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("RubyMine Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("RM"), "2021.3","RM:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("WebStorm Toolbox", jbServerProductBlacklistService.findByValueAddifNotExist("WS"), "2021.3","WS:2021.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("CLion 2020.1", jbServerProductBlacklistService.findByValueAddifNotExist("CL"), "2020.1","CL:2020.1"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("ReSharper 2019.3", jbServerProductBlacklistService.findByValueAddifNotExist("RC"), "2019.3","RC:2019.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("PyCharm 2019.3", jbServerProductBlacklistService.findByValueAddifNotExist("PC"), "2019.3","PC:2019.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("IntelliJ IDEA Ultimate 2020.3", jbServerProductBlacklistService.findByValueAddifNotExist("II"), "2020.3","II:2020.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("CLion 2020.3", jbServerProductBlacklistService.findByValueAddifNotExist("CL"), "2020.3","CL:2020.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("PyCharm 2020.3", jbServerProductBlacklistService.findByValueAddifNotExist("PC"), "2020.3","PC:2020.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("DataGrip 2020.3", jbServerProductBlacklistService.findByValueAddifNotExist("DB"), "2020.3","DB:2020.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("DataGrip 2019.3", jbServerProductBlacklistService.findByValueAddifNotExist("DB"), "2019.3","DB:2019.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("WebStorm 2019.3", jbServerProductBlacklistService.findByValueAddifNotExist("WS"), "2019.3","WS:2019.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("IntelliJ IDEA Ultimate 2019.3", jbServerProductBlacklistService.findByValueAddifNotExist("II"), "2019.3","II:2019.3"));
            jbServerProductUtilizationService.saveAndFlush(new JBServerProductUtilization("WebStorm 2020.3", jbServerProductBlacklistService.findByValueAddifNotExist("WS"), "2020.3","WS:2020.3"));
        }
        if (jbServerProductErrorService.findAll().size() < 1) {
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("cfc7082d-ae43-4978-a2a2-46feb1679405".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("CL")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("94ed896e-599e-4e2c-8724-204935e593ff".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("DB")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("6ca374ac-f547-4984-be94-adb3e47b580c".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("GO")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("49c202d4-ac56-452b-bb84-735056242fb3".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("II")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("0d85f2cc-b84f-44c7-b319-93997d080ac9".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("PS")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("e8d15448-eecd-440e-bbe9-1e5f754d781b".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("PC")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("5931f436-2506-415e-a0a9-27f50d7f62bf".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("RSC")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("c9e1fa2c-9f19-4ad7-935c-481ca0c2d23c".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("RD")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("342e66b2-956c-4384-81da-f50365b990e9".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("WS")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("c7188e33-d57e-4f8d-ac0d-daf28d5b2baa".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("All")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("31cfcac2-ba37-44a3-810d-96b11b222d56".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("All")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("709978b2-feae-4109-94dc-5dd574d51dad".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("All")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("95c62f78-12a5-42de-8fb9-aa5e158eaf46".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("All")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("930a52c3-bc8f-411e-9064-94a1164aa92f".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("All")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("a8c85415-c168-4e9d-9b93-53018a0b6cc6".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("All")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("pcwmp".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("All")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("59BB7CF0-D203-4E54-9A5F-04FBB1AEBCD4".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("All")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("39365442-7F02-4765-AB93-770C04F400B7".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("All")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("FDF9F05F-D8FE-44B1-9721-4455E35EA49F".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("All")));
            jbServerProductErrorService.saveAndFlush(new JBServerProductError("DD8D40C7-866B-4204-9D56-9E620CD76A4D".toUpperCase(), jbServerProductBlacklistService.findByValueAddifNotExist("All")));

        }
    }
}
