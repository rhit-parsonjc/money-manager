package com.moneymanager.api.services.UserEntityService;

import com.moneymanager.api.exceptions.AlreadyExistsException;
import com.moneymanager.api.exceptions.PermissionsException;
import com.moneymanager.api.models.Role;
import com.moneymanager.api.models.UserEntity;
import com.moneymanager.api.repositories.RoleRepository;
import com.moneymanager.api.repositories.UserEntityRepository;
import com.moneymanager.api.requests.RegisterRequest;

import com.moneymanager.api.requests.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserEntityServiceImpl implements UserEntityService {
    private final UserEntityRepository userEntityRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String firstName = registerRequest.getFirstName();
        String lastName = registerRequest.getLastName();
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        List<UserEntity> userEntitiesByUsername = userEntityRepository.findByUsername(username);
        if (!userEntitiesByUsername.isEmpty())
            throw new AlreadyExistsException(AlreadyExistsException.USERNAME_MESSAGE);
        List<UserEntity> userEntitiesByEmail = userEntityRepository.findByEmail(email);
        if (!userEntitiesByEmail.isEmpty())
            throw new AlreadyExistsException(AlreadyExistsException.EMAIL_MESSAGE);
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
                email,
                firstName,
                lastName,
                username,
                passwordEncoder.encode(password),
                singletonUserRole
        );
        userEntityRepository.save(userEntity);
    }

    @Override
    public Optional<UserEntity> getAuthenticatedUser() {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        List<UserEntity> userEntities = userEntityRepository.findByUsername(username);
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

    @Override
    public UserEntity updateUser(UserUpdateRequest userUpdateRequest) {
        UserEntity userEntity = this.getAuthenticatedUserOrThrow();
        String password = userUpdateRequest.getPassword();
        if (password != null) {
            String encodedPassword = passwordEncoder.encode(password);
            userEntity.update(userUpdateRequest, encodedPassword);
        } else {
            userEntity.update(userUpdateRequest);
        }
        return userEntityRepository.save(userEntity);
    }

    @Override
    public void deleteUser() {
        UserEntity userEntity = this.getAuthenticatedUserOrThrow();
        Long userEntityId = userEntity.getId();
        userEntityRepository.deleteById(userEntityId);
    }
}
