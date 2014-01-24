package com.mimolet.server.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mimolet.server.domain.User;

@Repository
public class UserDAOImpl implements UserDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public User findUserByUsername(String username) {
		final Session session = sessionFactory.openSession();
		try {
			final Criteria criteria = session.createCriteria(User.class);
			criteria.add(Restrictions.eq("username", username));
			return (User) criteria.uniqueResult();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void saveUser(User user) {
		sessionFactory.getCurrentSession().save(user);
	}

	@Override
	public void updatePassword(User user) {
		sessionFactory.getCurrentSession().update(user);
	}
	
}
