package com.moneymanager.api.services.UserEntityService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.moneymanager.api.exceptions.AlreadyExistsException;
import com.moneymanager.api.exceptions.PermissionsException;
import com.moneymanager.api.models.Role;
import com.moneymanager.api.models.UserEntity;
import com.moneymanager.api.repositories.RoleRepository;
import com.moneymanager.api.repositories.UserEntityRepository;
import com.moneymanager.api.requests.RegisterRequest;

@Service
@RequiredArgsConstructor
public class UserEntityServiceImpl implements UserEntityService {
    private final UserEntityRepository userEntityRepository;
    private final RoleRepository roleRepository;

    @Override
    public void createUser(RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        List<UserEntity> userEntities = userEntityRepository.findByUsername(username);
        if (!userEntities.isEmpty())
            throw new AlreadyExistsException(AlreadyExistsException.DATE_AMOUNT_MESSAGE);
        List<Role> roles = roleRepository.findByName("USER");
        Role userRole;
        if (roles.isEmpty()) {
            Role role = new Role("USER");
            userRole = roleRepository.save(role);
        } else {
            userRole = roles.getFirst();
        }
        Set<Role> singletonUserRole = Collections.singleton(userRole);
        UserEntity userEntity = new UserEntity(
                username,
                password,
                singletonUserRole
        );
        userEntityRepository.save(userEntity);
    }

    @Override
    public Optional<UserEntity> getAuthenticatedUser() {
        List<UserEntity> userEntities = userEntityRepository.findAll();
        if (userEntities.isEmpty())
            return Optional.empty();
        else
            return Optional.of(userEntities.getFirst());
    }

    @Override
    public UserEntity getAuthenticatedUserOrThrow() {
        Optional<UserEntity> userEntityOptional = getAuthenticatedUser();
        if (userEntityOptional.isEmpty())
            throw new PermissionsException(PermissionsException.NO_USER);
        else
            return userEntityOptional.get();
    }
}
