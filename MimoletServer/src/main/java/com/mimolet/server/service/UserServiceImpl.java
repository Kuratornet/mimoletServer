package com.mimolet.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mimolet.server.dao.AuthorityDAO;
import com.mimolet.server.dao.UserDAO;
import com.mimolet.server.domain.Authority;
import com.mimolet.server.domain.User;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private AuthorityDAO authDAO;

	@Override
	public User findUserByUsername(String username) {
		return userDAO.findUserByUsername(username);
	}
	
	@Transactional
	@Override
	public void saveUser(User user) {
		userDAO.saveUser(user);
		Authority authority = new Authority();
		authority.setAuthority("ROLE_USER");
		authority.setUsername(user.getUsername());
		authDAO.addAuthority(authority);
	}
	
	@Transactional
	@Override
	public void updatePassword(User user) {
		userDAO.updatePassword(user);
	}
}