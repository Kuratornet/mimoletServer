package com.mimolet.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mimolet.server.dao.UserDAO;
import com.mimolet.server.domain.User;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDAO userDAO;

	@Override
	public User findUserByUsername(String username) {
		return userDAO.findUserByUsername(username);
	}
}