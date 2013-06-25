package com.mimolet.server.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mimolet.server.domain.Order;

@Repository
public class OrderDAOImpl implements OrderDAO {

	@Autowired
    private SessionFactory sessionFactory;

	@Override
	public void addOrder(Order order) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(order);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Order> listOrder() {
		// TODO Auto-generated method stub
		return sessionFactory.getCurrentSession().createQuery("from Order")
	            .list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Order> listOrderByOwnerId(Integer ownerId) {
		// TODO Auto-generated method stub
		return sessionFactory.getCurrentSession().createQuery("from Order where ownerId=" + ownerId)
	            .list();
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
