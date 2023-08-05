package com.rafaeldeluca.dscommerce.tests;

import com.rafaeldeluca.dscommerce.projections.UserDetailsProjection;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsFactory {

    public static List<UserDetailsProjection> createCustomClientUserList (String username) {

        List<UserDetailsProjection> userDetailsProjectionList = new ArrayList<UserDetailsProjection>();
        userDetailsProjectionList.add(new UserDetailsImpl(username, "123456",1L, "ROLE_CLIENT"));
        return userDetailsProjectionList;
    }

    public static List<UserDetailsProjection> createCustomAdminUserList (String username) {

        List<UserDetailsProjection> userDetailsProjectionList = new ArrayList<UserDetailsProjection>();
        userDetailsProjectionList.add(new UserDetailsImpl(username, "123456",2L, "ROLE_ADMIN"));
        return userDetailsProjectionList;
    }

    public static List<UserDetailsProjection> createCustomAdminAndClientUserList (String username) {

        List<UserDetailsProjection> userDetailsProjectionList = new ArrayList<UserDetailsProjection>();
        userDetailsProjectionList.add(new UserDetailsImpl(username, "123456",1L, "ROLE_CLIENT"));
        userDetailsProjectionList.add(new UserDetailsImpl(username, "123456",2L, "ROLE_ADMIN"));
        return userDetailsProjectionList;
    }


}
