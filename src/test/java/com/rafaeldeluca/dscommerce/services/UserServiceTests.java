package com.rafaeldeluca.dscommerce.services;

import com.rafaeldeluca.dscommerce.entities.User;
import com.rafaeldeluca.dscommerce.projections.UserDetailsProjection;
import com.rafaeldeluca.dscommerce.repositories.UserRepository;
import com.rafaeldeluca.dscommerce.tests.UserDetailsFactory;
import com.rafaeldeluca.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    //inserindindo as dependências
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    private User user;
    private String existingUserName, nonExistingUsername;
    private List<UserDetailsProjection> userDetailsProjectionList;

    @BeforeEach
    public void setUp () throws Exception {

        existingUserName = "juliana@gmail.com";
        nonExistingUsername = "rafaeldeluca@gmail.com";

        user = UserFactory.createCustomUserClient(1L, existingUserName);
        userDetailsProjectionList = UserDetailsFactory.createCustomAdminUserList(existingUserName);

        Mockito.when(userRepository.searchUserAndRolesByEmail(existingUserName)).thenReturn(userDetailsProjectionList);
        Mockito.when(userRepository.searchUserAndRolesByEmail(nonExistingUsername)).thenReturn(Collections.EMPTY_LIST);

    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsProjectionListWhenUserExists () {
        UserDetails serviceResult = userService.loadUserByUsername(existingUserName);

        Assertions.assertNotNull(serviceResult);
        Assertions.assertEquals(serviceResult.getUsername(), existingUserName);
    }

    @Test
    public void loadUserByUserNameShouldThrowUserNameNotFoundExceptionWHenUserDoesNotExists () {

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(nonExistingUsername);
        });
    }



}
