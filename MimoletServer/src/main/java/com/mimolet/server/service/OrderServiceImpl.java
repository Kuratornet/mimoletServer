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
		orderDAO.addOrder(order);
	}
	
	@Transactional
	@Override
	public List<Order> listOrder() {
		return orderDAO.listOrder();
	}

	@Transactional
	@Override
	public void removeOrder(Integer id) {
		orderDAO.removeOrder(id);
	}

	@Override
	public List<Order> listOrderByOwnerId(Integer ownerId) {
		return orderDAO.listOrderByOwnerId(ownerId);
	}

}
