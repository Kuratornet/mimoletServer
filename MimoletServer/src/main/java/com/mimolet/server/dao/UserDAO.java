package com.mimolet.server.dao;

import com.mimolet.server.domain.User;

public interface UserDAO {
	public User findUserByUsername(String username);
}
