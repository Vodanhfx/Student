package com.lms.studentmanagement.service;

import com.lms.studentmanagement.model.User;
import com.lms.studentmanagement.model.Role;
import com.lms.studentmanagement.repository.CourseRepository;
import com.lms.studentmanagement.repository.UserRepository;
import com.lms.studentmanagement.repository.RoleRepository;
import com.lms.studentmanagement.exception.ResourceNotFoundException;
import com.lms.studentmanagement.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private CourseRepository courseRepository;

    @InjectMocks private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldSaveAndReturnUser() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setName("Test User");
        user.setPassword("pass");
        user.setRoles(Set.of(new Role()));

        when(userRepository.save(any(User.class))).thenReturn(user);

        User saved = userService.createUser(user);

        assertThat(saved.getEmail()).isEqualTo("test@mail.com");
        verify(userRepository).save(user);
    }

    @Test
    void getUserById_ShouldReturnUser_IfExists() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User found = userService.getUserById(1L);
        assertThat(found.getId()).isEqualTo(1L);
    }

    @Test
    void getUserById_ShouldThrow_IfNotExists() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getUserById(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void disableUser_ShouldSetActiveFalse() {
        User user = new User();
        user.setId(1L);
        user.setActive(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.disableUser(1L);

        assertThat(user.isActive()).isFalse();
        verify(userRepository).save(user);
    }

    @Test
    void activateUser_ShouldSetActiveTrue() {
        User user = new User();
        user.setId(1L);
        user.setActive(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.activateUser(1L);

        assertThat(user.isActive()).isTrue();
        verify(userRepository).save(user);
    }
}