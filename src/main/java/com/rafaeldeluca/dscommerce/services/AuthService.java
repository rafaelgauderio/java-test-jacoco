package com.rafaeldeluca.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rafaeldeluca.dscommerce.entities.User;
import com.rafaeldeluca.dscommerce.services.exceptions.ForbiddenException;

@Service
public class AuthService {

	@Autowired
	private UserService userService;
	
	public void validateSelfOrAdmin(long userId) {
		User me = userService.authenticated();
		/*
		if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
			throw new ForbiddenException("Access denied");
		}
		*/
		if(me.hasRole("ROLE_ADMIN")) {
			// se tiver a role admin pode acessar os pedidos do outros usuaários
			return;
		} if (me.getId().equals(userId)==false) {
			throw new ForbiddenException("Acess denied!. Only user admin can access order from another clients.");
		}

	}
}
