package com.mimolet.server.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mimolet.server.domain.User;

public class UserDAOImpl implements UserDAO {

	@Autowired
    private SessionFactory sessionFactory;
	
	@Override
	public int getOwnerIdByName(String name) {
		Session session = sessionFactory.openSession();
		User user = (User) session.get(User.class, name);
		return user.getId();
	}

}
