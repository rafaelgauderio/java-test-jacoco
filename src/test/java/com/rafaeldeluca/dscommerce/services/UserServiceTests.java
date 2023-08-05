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
    private User userClient;
    private User userAdmin;
    private String existingUserNameClient, existingUserNameAdmin, nonExistingUsername;
    private List<UserDetailsProjection> userDetailsProjectionList, userDetailsProjectionListAdmin;

    @BeforeEach
    public void setUp () throws Exception {

        existingUserNameClient = "juliana@gmail.com";
        existingUserNameAdmin = "deluca1712@gmail.com";
        nonExistingUsername = "rafaeldeluca@gmail.com";

        userClient = UserFactory.createCustomUserClient(1L, existingUserNameClient);
        userAdmin = UserFactory.createCustomAdminUser(5L,existingUserNameAdmin);

        userDetailsProjectionList = UserDetailsFactory.createCustomClientUserList(existingUserNameClient);
        userDetailsProjectionListAdmin = UserDetailsFactory.createCustomClientUserList(existingUserNameAdmin);

        Mockito.when(userRepository.searchUserAndRolesByEmail(existingUserNameClient)).thenReturn(userDetailsProjectionList);
        Mockito.when(userRepository.searchUserAndRolesByEmail(existingUserNameAdmin)).thenReturn(userDetailsProjectionListAdmin);
        Mockito.when(userRepository.searchUserAndRolesByEmail(nonExistingUsername)).thenReturn(Collections.EMPTY_LIST);

    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsProjectionListWhenUserClientExists () {
        UserDetails serviceResult = userService.loadUserByUsername(existingUserNameClient);

        Assertions.assertNotNull(serviceResult);
        Assertions.assertEquals(serviceResult.getUsername(), existingUserNameClient);
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsProjectionListWhenUserAdminExists () {
        UserDetails serviceResult = userService.loadUserByUsername(existingUserNameAdmin);

        Assertions.assertNotNull(serviceResult);
        Assertions.assertEquals(serviceResult.getUsername(), existingUserNameAdmin);
    }

    @Test
    public void loadUserByUserNameShouldThrowUserNameNotFoundExceptionWHenUserDoesNotExists () {

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(nonExistingUsername);
        });
    }



}
