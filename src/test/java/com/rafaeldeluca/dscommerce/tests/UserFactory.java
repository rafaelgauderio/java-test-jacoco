package com.rafaeldeluca.dscommerce.tests;

import com.rafaeldeluca.dscommerce.entities.Role;
import com.rafaeldeluca.dscommerce.entities.User;

import java.time.LocalDate;

public class UserFactory {

    public static User createClientUser () {
        User userClient = new User(1L,"Maria Silva", "juliana@gmail.com", "92842525", LocalDate.parse("1995-04-25"),"$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
        userClient.addRole(new Role(1L, "ROLE_CLIENT"));
        return userClient;
    }

    public static User createCustomUserClient (Long id, String username) {
        User userClient = new User(id,"Maria Silva", username, "92842525", LocalDate.parse("1995-04-25"),"$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
        userClient.addRole(new Role(1L, "ROLE_CLIENT"));
        return userClient;
    }

    public static User createAdminUser () {
        User userAdmin = new User(5L,"Rafael de Luca", "deluca1712@mail.com", "99555512", LocalDate.parse("1987-12-17"),"$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
        userAdmin.addRole(new Role(2L, "ROLE_ADMIN"));
        return userAdmin;
    }

    public static User createCustomAdminUser (Long id, String username) {
        User userAdmin = new User(id,"Rafael de Luca", username, "99555512", LocalDate.parse("1987-12-17"),"$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
        userAdmin.addRole(new Role(2L, "ROLE_ADMIN"));
        return userAdmin;
    }
}

