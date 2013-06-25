package com.mimolet.server.service;

import com.mimolet.server.dao.UserDAO;

public class UserServiceImpl implements UserService {

	private UserDAO userdao;
	
	@Override
	public int getOwnerIdByName(String name) {
		return userdao.getOwnerIdByName(name);
	}

}
