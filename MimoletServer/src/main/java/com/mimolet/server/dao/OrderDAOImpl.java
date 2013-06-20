package com.mimolet.server.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mimolet.server.domain.Order;

@Repository
public class OrderDAOImpl implements OrderDAO {

	private static final Log LOGGER = LogFactory.getLog(OrderDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void addOrder(Order order) {
		try {
			sessionFactory.getCurrentSession().save(order);
			LOGGER.info("Successfully saved order " + order);
		} catch (Exception ex) {
			LOGGER.error("Could not save order " + order, ex);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order> listOrder() {
		return sessionFactory.getCurrentSession().createQuery("from Order")
				.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Order> listOrderByOwnerId(Integer ownerId) {
		return sessionFactory.getCurrentSession()
				.createQuery("from Order where ownerId=" + ownerId).list();
	}

	@Override
	public void removeOrder(Integer id) {
		Order order = (Order) sessionFactory.getCurrentSession().load(
				Order.class, id);
		if (null != order) {
			sessionFactory.getCurrentSession().delete(order);
		}

	}

}
