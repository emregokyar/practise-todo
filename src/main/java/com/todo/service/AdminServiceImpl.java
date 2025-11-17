package com.todo.service;

import com.todo.entity.Authority;
import com.todo.entity.User;
import com.todo.repository.UserRepository;
import com.todo.response_dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        // Stream support converts iterable into stream. In this case, Stream<User>.
        // spliterator() function creates spliterator over all users - spliterator lets us create multiple smaller iterators
        // a false option lets us create sequential stream instead of parallel
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(this::convertToUserResponse).toList();
    }

    @Override
    @Transactional
    public UserResponse promoteToAdmin(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty() || user.get().getAuthorities()
                .stream()
                .anyMatch(grantedAuthority ->
                        "ROLE_ADMIN".equals(grantedAuthority.getAuthority()))
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist or already an admin");
        }

        List<Authority> authorities = new ArrayList<>();
        authorities.add(new Authority("ROLE_EMPLOYEE"));
        authorities.add(new Authority("ROLE_ADMIN"));
        user.get().setAuthorities(authorities);

        User savedUser = userRepository.save(user.get());
        return convertToUserResponse(savedUser);
    }

    @Override
    @Transactional
    public void deleteNonAdminUser(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty() || user.get().getAuthorities()
                .stream()
                .anyMatch(grantedAuthority ->
                        "ROLE_ADMIN".equals(grantedAuthority.getAuthority()))
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist or already an admin so can not delete user");
        }
        userRepository.delete(user.get());
    }

    private UserResponse convertToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                (user.getFirstName() + " " + user.getLastName()),
                user.getEmail(),
                user.getAuthorities().stream().map(auth -> (Authority) auth).toList()
        );
    }
}
