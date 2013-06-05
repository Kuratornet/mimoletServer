package com.mimolet.server.web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mimolet.server.domain.Order;
import com.mimolet.server.service.OrderService;

@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/index")
    public String listOrder(Map<String, Object> map) {

        map.put("order", new Order());
        map.put("orderList", orderService.listOrder());

        return "order";
    }
	
	@RequestMapping("/")
    public String home() {
        return "redirect:/index";
    }
	
	@RequestMapping(value="/jsessionid", method = RequestMethod.GET)
	@ResponseBody
	public String responseBody(@CookieValue("JSESSIONID") String jsessionid) {
		return jsessionid;
	}
	
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addOrder(@ModelAttribute("order") Order order,
            BindingResult result) {

        orderService.addOrder(order);

        return "redirect:/index";
    }

    @RequestMapping("/delete/{orderId}")
    public String deleteOrder(@PathVariable("orderId") Integer orderId) {

        orderService.removeOrder(orderId);

        return "redirect:/index";
    }
    
    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    public String upladFile(@RequestParam("byteArray") byte[] fileBytes){
    	FileOutputStream fos;
		try {
			fos = new FileOutputStream("C:\\MyDir\\");
    	fos.write(fileBytes);
    	fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return null;
    }

}
