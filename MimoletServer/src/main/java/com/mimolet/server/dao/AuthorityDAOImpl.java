package com.mimolet.server.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mimolet.server.domain.Authority;

@Repository
public class AuthorityDAOImpl implements AuthorityDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Authority findAuthorityByUsername(String username) {
		final Session session = sessionFactory.openSession();
		try {
			final Criteria criteria = session.createCriteria(Authority.class);
			criteria.add(Restrictions.eq("username", username));
			return (Authority) criteria.uniqueResult();
		} finally {
			session.close();
		}
	}

	@Override
	public void addAuthority(Authority authority) {
		sessionFactory.getCurrentSession().save(authority);
	}

}
