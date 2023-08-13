package com.rafaeldeluca.dscommerce.services;

import com.rafaeldeluca.dscommerce.entities.User;
import com.rafaeldeluca.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {

    @InjectMocks
    private AuthService authService;
    @Mock
    private UserService userService;
    private User admin, selClient, differentClient;

    @BeforeEach
    void setUp () throws Exception {
        admin = UserFactory.createAdminUser();
        selClient = UserFactory.createCustomUserClient(1L,"Claúdia");
        differentClient = UserFactory.createCustomUserClient(2L,"Ana Carolina");
    }

    @Test
    public void validateSelfOrAdminShouldNotThrowExceptionWhenUserLoggedAsAdmin () {
        Mockito.when(userService.authenticated()).thenReturn(admin);

        Long userId = admin.getId();

        Assertions.assertDoesNotThrow( () -> {
            authService.validateSelfOrAdmin(userId);
        });
    }
}
;