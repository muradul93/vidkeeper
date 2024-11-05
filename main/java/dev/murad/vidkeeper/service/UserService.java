package dev.murad.vidkeeper.service;

import dev.murad.vidkeeper.controller.dto.SignupRequest;
import dev.murad.vidkeeper.domain.Role;
import dev.murad.vidkeeper.domain.User;
import dev.murad.vidkeeper.exceptions.DuplicateException;
import dev.murad.vidkeeper.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void signup(SignupRequest request) {
        String username = request.username();
        Optional<User> existingUser = repository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new DuplicateException(String.format("User with the username  '%s' already exists.", username));
        }

        String hashedPassword = passwordEncoder.encode(request.password());
        User user = User.builder()
                .username(username)
                .password(hashedPassword)
                .role(Role.valueOf(request.role()))
                .build();
        repository.save(user);
    }


    public User findByUserName(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
