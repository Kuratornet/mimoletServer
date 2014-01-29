package com.mimolet.server.service;

import java.util.List;

import com.mimolet.server.domain.Order;

public interface OrderService {
	
	public void addOrder(Order order);
	
	public void saveOrder(Order order);
	
	public List<Order> listOrder();
	
	public List<Order> listOrderByOwnerId(Integer ownerId);
	
	public void removeOrder(Integer id);
	
	public Order getOrderById(Integer id);

}
