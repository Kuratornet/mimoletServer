package com.mimolet.server.web;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mimolet.server.domain.Order;
import com.mimolet.server.domain.User;
import com.mimolet.server.service.OrderService;
import com.mimolet.server.service.UserService;

@Controller
public class OrderController {
	private Log log = LogFactory.getLog(OrderController.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@RequestMapping("/index")
	public String listOrder(Map<String, Object> map) {

		map.put("order", new Order());
		map.put("orderList", orderService.listOrder());

		return "order";
	}

	@RequestMapping("/ping")
	@ResponseBody
	public String ping() {
		return "pong";
	}

	@RequestMapping("/")
	public String home() {
		return "redirect:/index";
	}

	@RequestMapping(value = "/jsessionid", method = RequestMethod.GET)
	@ResponseBody
	public String responseBody(@CookieValue("JSESSIONID") String jsessionid,
			Principal principal, HttpServletRequest request) {
		final User loggedUser = userService.findUserByUsername(principal
				.getName());
//		System.out.println(request);
//		System.out.println(request.getSession(true));
//		System.out.println(loggedUser);
//		request.getSession(true).setAttribute("userid", loggedUser.getId());
		return jsessionid;
	}

	@RequestMapping(value = "/getById", method = RequestMethod.POST)
	public List<Order> getAllOrdersById(@RequestParam("ownerID") Integer ownerId) {
		return orderService.listOrderByOwnerId(ownerId);
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

	// @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
	// public String upladFile(@RequestParam("byteArray") byte[] fileBytes){
	// FileOutputStream fos;
	// try {
	// fos = new FileOutputStream("C:\\MyDir\\");
	// fos.write(fileBytes);
	// fos.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return null;
	// }

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String handleFormUpload(
			@CookieValue("JSESSIONID") String jsessionid,
			@RequestParam("file") MultipartFile file,
			@RequestParam("description") String description,
			@RequestParam("binding") Integer binding,
			@RequestParam("paper") Integer paper,
			@RequestParam("print") Integer print,
			@RequestParam("blockSize") Integer blockSize,
			@RequestParam("pages") Integer pages, HttpServletRequest request) {
		final Integer ownerId = (Integer) request.getSession().getAttribute(
				"userid");
		String result = "false";
		String filePath = null;
		try {
			if (!file.isEmpty()) {
				final File destination = new File("owner" + ownerId + ".pdf");
				filePath = destination.getAbsolutePath();
				file.transferTo(destination);
			}
			result = "true";
		} catch (IOException e) {
			result = "false";
			log.error(file, e);
		}
		final Order order = new Order();
		order.setDescription(description);
		order.setBinding(binding);
		order.setBlocksize(blockSize);
		order.setPaper(paper);
		order.setPages(pages);
		order.setLink(filePath);
		order.setOwnerId(ownerId);
		order.setStatus(0);
//		orderService.addOrder(order);
		return result;
	}

}
