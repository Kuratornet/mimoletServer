package com.mimolet.server.web;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.mimolet.server.domain.Order;
import com.mimolet.server.service.OrderService;
import com.mimolet.server.service.UserService;

@Controller
public class OrderController {
	private Log log = LogFactory.getLog(OrderController.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Autowired
	private ServletContext servletContext;

	@Autowired(required = true)
	private ApplicationContext applicationContext;

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
			HttpServletRequest request) {
		return String.valueOf(getLoggedUserId());
	}

	@RequestMapping(value = "/getById", method = RequestMethod.POST)
	@ResponseBody
	public List<Order> getAllOrdersById() {
		return orderService.listOrderByOwnerId(getLoggedUserId());
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

	@RequestMapping("/getid")
	@ResponseBody
	public Integer getId(@PathVariable("username") String name) {
		return userService.findUserByUsername(name).getId();
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String handleFormUpload(
			@CookieValue("JSESSIONID") String jsessionid,
			@RequestParam("file") MultipartFile file,
			@RequestParam("preview") MultipartFile preview,
			@RequestParam("description") String description,
			@RequestParam("binding") Integer binding,
			@RequestParam("paper") Integer paper,
			@RequestParam("print") Integer print,
			@RequestParam("blockSize") Integer blockSize,
			@RequestParam("pages") Integer pages, HttpServletRequest request) {
		final Integer ownerId = getLoggedUserId();
		String result = "false";
		String previewLink = null;
		try {
			if (!file.isEmpty()) {
				File destination = new File(servletContext.getRealPath("owner" + ownerId + ".pdf"));
				destination.getParentFile().mkdirs();
				destination.createNewFile();
				file.transferTo(destination);
				destination = new File(servletContext.getRealPath("img/owner" + ownerId + ".png"));
				destination.getParentFile().mkdirs();
				destination.createNewFile();
				preview.transferTo(destination);
				final HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder
						.currentRequestAttributes()).getRequest(); 
				previewLink = "http://"
						+ httpRequest.getLocalAddr() + ":"
						+ httpRequest.getLocalPort()
						+ servletContext.getContextPath() + "/img/owner"
						+ ownerId + ".png"; 
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
		order.setPrint(1);
		order.setCreateData("2011-03-18");
		order.setPages(pages);
		order.setLink(previewLink);
		order.setOwnerId(ownerId);
		order.setStatus(0);
		orderService.addOrder(order);
		return result;
	}

	private Integer getLoggedUserId() {
		final SecurityContext securityContext = SecurityContextHolder
				.getContext();
		final Authentication authentication = securityContext
				.getAuthentication();
		if (authentication != null) {
			final User principal = (User) authentication.getPrincipal();
			final com.mimolet.server.domain.User user = userService
					.findUserByUsername(principal.getUsername());
			return user.getId();
		}
		return -1;
	}

}
