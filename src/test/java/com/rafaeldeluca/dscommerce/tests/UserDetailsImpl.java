package com.rafaeldeluca.dscommerce.tests;

import com.rafaeldeluca.dscommerce.projections.UserDetailsProjection;

// implementando a interface UserDetailsProjection
public class UserDetailsImpl implements UserDetailsProjection  {

    private String username;
    private String password;
    private Long roleId;
    private String authority;

    public UserDetailsImpl () {

    }

    public UserDetailsImpl(String username, String password, Long roleId, String authority) {
        this.username = username;
        this.password = password;
        this.roleId = roleId;
        this.authority = authority;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Long getRoleId() {
        return roleId;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
