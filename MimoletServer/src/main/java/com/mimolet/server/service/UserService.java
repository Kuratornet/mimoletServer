package com.mimolet.server.service;

import com.mimolet.server.domain.User;

public interface UserService {
	public User findUserByUsername(String username);
	
	public void saveUser(User user);
	
	public void updatePassword(User user);
}