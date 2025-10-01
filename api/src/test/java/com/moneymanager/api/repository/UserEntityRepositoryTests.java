package com.moneymanager.api.repository;

import com.moneymanager.api.models.Role;
import com.moneymanager.api.models.UserEntity;
import com.moneymanager.api.repositories.RoleRepository;
import com.moneymanager.api.repositories.UserEntityRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserEntityRepositoryTests {
    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Set<Role> roleSet;

    private void verifyUserEntity(UserEntity userEntity, String username, String password) {
        Assertions.assertEquals(username, userEntity.getUsername());
        Assertions.assertEquals(password, userEntity.getPassword());
        Assertions.assertTrue(userEntity.getId() > 0);
    }

    @BeforeEach
    public void setup() {
        Role userRole = new Role("USER");
        Role savedUserRole = roleRepository.save(userRole);
        roleSet = new HashSet<>();
        roleSet.add(savedUserRole);
    }

    @Test
    public void UserEntityRepository_Save() {
        UserEntity userEntity = new UserEntity("spring@spring.io", "First", "Last", "Spring", "pass1234", roleSet);

        UserEntity savedUserEntity = userEntityRepository.save(userEntity);

        verifyUserEntity(savedUserEntity, "Spring", "pass1234");
    }

    @Test
    public void UserEntityRepository_FindByUsername() {
        UserEntity userEntity1 = new UserEntity("spring1@spring.io", "Spring", "Boot", "Spring1", "pass1234", roleSet);
        UserEntity userEntity2 = new UserEntity("spring2@spring.io", "Spring", "Data", "Spring2", "password", roleSet);
        userEntityRepository.save(userEntity1);
        userEntityRepository.save(userEntity2);

        List<UserEntity> foundUserEntityList = userEntityRepository.findByUsername("Spring1");

        Assertions.assertNotNull(foundUserEntityList);
        Assertions.assertEquals(1, foundUserEntityList.size());
        verifyUserEntity(foundUserEntityList.getFirst(), "Spring1", "pass1234");
    }
}
