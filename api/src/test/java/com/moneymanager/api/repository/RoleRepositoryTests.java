package com.moneymanager.api.repository;

import com.moneymanager.api.models.Role;
import com.moneymanager.api.repositories.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RoleRepositoryTests {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void RoleRepository_Save() {
        Role role = new Role("USER");

        Role savedRole = roleRepository.save(role);

        Assertions.assertEquals("USER", savedRole.getName());
        Assertions.assertTrue(savedRole.getId() > 0);
    }

    @Test
    public void RoleRepository_FindByName() {
        Role userRole = new Role("USER");
        Role adminRole = new Role("ADMIN");
        roleRepository.save(userRole);
        roleRepository.save(adminRole);

        List<Role> foundAdminRoleList = roleRepository.findByName("ADMIN");

        Assertions.assertNotNull(foundAdminRoleList);
        Assertions.assertEquals(1, foundAdminRoleList.size());
        Assertions.assertEquals("ADMIN", foundAdminRoleList.getFirst().getName());
    }
}
