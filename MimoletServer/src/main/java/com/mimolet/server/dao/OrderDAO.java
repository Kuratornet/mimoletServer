package com.mimolet.server.dao;

import java.util.List;

import com.mimolet.server.domain.Order;

public interface OrderDAO {
	public void addOrder(Order order);

	public List<Order> listOrder();

	public List<Order> listOrderByOwnerId(Integer ownerId);

	public void removeOrder(Integer id);

	public void saveOrder(Order order);
	
	public Order getOrderById(Integer id);
}
