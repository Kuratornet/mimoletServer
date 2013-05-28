package com.mimolet.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mimolet.server.dao.OrderDAO;
import com.mimolet.server.domain.Order;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDAO orderDAO;
	
	@Transactional
	@Override
	public void addOrder(Order order) {
		// TODO Auto-generated method stub
		orderDAO.addOrder(order);
	}
	
	@Transactional
	@Override
	public List<Order> listOrder() {
		// TODO Auto-generated method stub
		return orderDAO.listOrder();
	}

	@Transactional
	@Override
	public void removeOrder(Integer id) {
		// TODO Auto-generated method stub
		orderDAO.removeOrder(id);
	}

}
