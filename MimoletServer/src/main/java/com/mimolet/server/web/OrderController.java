package com.mimolet.server.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import com.mimolet.server.domain.PhotoData;
import com.mimolet.server.service.OrderService;
import com.mimolet.server.service.UserService;
import com.mimolet.server.tools.EmailSender;

@Controller
public class OrderController {
	private Log log = LogFactory.getLog(OrderController.class);
	private static final String SERVICE_EMAIL = "kuratornet@gmail.com";
	private static final String DEFAULT_PASS = "2921e995a926a2f6ee78f7d5405997e8";

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
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
	@ResponseBody
	public String deleteOrder(@PathVariable("orderId") Integer orderId) {
		try {
			log.warn("Delete order where ID = " + orderId);
			orderService.removeOrder(orderId);
			return "true";
		} catch (Exception e) {
			return "false";
		}
	}

	@RequestMapping("/autherror")
	@ResponseBody
	public String sendErrorMessage() {
		log.info("Auth error");
		return "false";
	}
	
	@RequestMapping("/getid")
	@ResponseBody
	public Integer getId(@PathVariable("username") String name) {
		return userService.findUserByUsername(name).getId();
	}

	@RequestMapping(value = "/adduser", method = RequestMethod.POST) 
	public String handleUserAdd(@RequestParam("login") String loginEmail,
								@RequestParam("password") String password,
								@RequestParam("enabled") String enabled) {
		com.mimolet.server.domain.User user = new com.mimolet.server.domain.User();
		user.setUsername(loginEmail);
		user.setPassword(password);
		user.setEnabled(enabled);
		userService.saveUser(user);
		return "done";
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
			@RequestParam("imagesDescription") String imagesDescriptionString,
			@RequestParam("pages") Integer pages, HttpServletRequest request) {
		String result = "false";
		final HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes()).getRequest();
		final int ownerId = getLoggedUserId();
		final Order order = new Order();
		order.setDescription(description);
		order.setBinding(binding);
		order.setBlocksize(blockSize);
		order.setPaper(paper);
		order.setPrint(1);
		order.setCreateData(new Date());
		order.setPages(pages);
		order.setOwnerId(ownerId);
		order.setStatus(0);
		orderService.addOrder(order);
		final String previewLink = "http://"
				+ httpRequest.getLocalAddr() + ":"
				+ httpRequest.getLocalPort()
				+ servletContext.getContextPath() + "/img/"
				+ ownerId + "_" + order.getId() + ".png";
		final String pdfLink = "http://"
				+ httpRequest.getLocalAddr() + ":"
				+ httpRequest.getLocalPort()
				+ servletContext.getContextPath() + "/pdf/"
				+ ownerId + "_" + order.getId() + ".pdf";
		order.setLink(pdfLink);
		order.setImagelink(previewLink);
		orderService.saveOrder(order);
		log.info(imagesDescriptionString);
		try {
			HashMap<Integer, PhotoData> map = new HashMap<Integer, PhotoData>();
			ObjectMapper mapper = new ObjectMapper();
			map = mapper.readValue(imagesDescriptionString,
					new TypeReference<HashMap<Integer, PhotoData>>() {
					});
			File destination = new File(servletContext.getRealPath("imgdesc/" + ownerId + "_" + order.getId() + ".imdc"));
			destination.getParentFile().mkdirs();
			destination.createNewFile();
			FileOutputStream fos = new FileOutputStream(destination);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(map);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (!file.isEmpty()) {
				File destination = new File(servletContext.getRealPath("pdf/" + ownerId + "_" + order.getId() + ".pdf"));
				destination.getParentFile().mkdirs();
				destination.createNewFile();
				file.transferTo(destination);
				destination = new File(servletContext.getRealPath("img/" + ownerId + "_" + order.getId() + ".png"));
				destination.getParentFile().mkdirs();
				destination.createNewFile();
				preview.transferTo(destination);
			}
			result = "true";
		} catch (IOException e) {
			result = "false";
			log.error(file, e);
		}
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
	
	@RequestMapping(value = "/socialauth", method = RequestMethod.POST)
	@ResponseBody
	public String socialAuth(@RequestParam("email") String username,
			@RequestParam("validateid") String validateid,
			@RequestParam("token") String token,
			@RequestParam("source") String source) {
		com.mimolet.server.domain.User user = userService.findUserByUsername(username);
		log.info("Check user for null " + (user == null));
		if (user != null) {
			log.info(user);
			if (source.equals("facebook")) {
				log.info("Source facebook check validatedid " + (user.getFacebookid().equals(validateid)));
				if (user.getFacebookid().equals(validateid)) {
					authTask(user);
					log.info("Send true answer");
					return "true";
				}
			} else if (source.equals("googleplus")) {
				log.info("Source googleplus");
				if (user.getGoogleplusid().equals(validateid)) {
					authTask(user);
					return "true";
				}
			}
		    return "false";
		} else {
			log.info("Creating new db user");
			com.mimolet.server.domain.User savingUser = new com.mimolet.server.domain.User();
			savingUser.setEnabled("true");
			savingUser.setUsername(username);
			savingUser.setPassword(DEFAULT_PASS);
			if (source.equals("facebook")) {
				savingUser.setFacebookid(validateid);
			} else if (source.equals("googleplus")) {
				savingUser.setGoogleplusid(validateid);
			}
			userService.saveUser(savingUser);
			authTask(userService.findUserByUsername(username));
			return "true";
		}
	}
	
	private void authTask(com.mimolet.server.domain.User user) {
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
	    Authentication authentication = authenticationManager.authenticate(authRequest);
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    securityContext.setAuthentication(authentication);
	    HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes()).getRequest();
	    HttpSession session = httpRequest.getSession(true);
	    session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	}
	
	@RequestMapping(value = "/registrationUser", method = RequestMethod.POST)
	@ResponseBody
	public String registrationUser(@RequestParam("email") String username,
			@RequestParam("password") String password) {
		try {
			com.mimolet.server.domain.User user = userService.findUserByUsername(username);
			if (user != null) {
				if (!user.getPassword().equals(DEFAULT_PASS)) {
					return "wronglogin";
				} else {
					user.setPassword(password);
					userService.updatePassword(user);
					authTask(user);
					return "true";
				}
			} else {
				user = new com.mimolet.server.domain.User();
				user.setUsername(username);
				user.setPassword(password);
				user.setEnabled("true");
				userService.saveUser(user);
				authTask(user);
				return "true";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}
	}
	
	@RequestMapping(value = "/restorepass", method = RequestMethod.POST)
	@ResponseBody
	public String restorepass(@RequestParam("email") String email) {
		log.fatal("Password restore was requested by " + email);
		com.mimolet.server.domain.User user = userService.findUserByUsername(email);
		EmailSender sender = new EmailSender();
		if (user != null) {
			if (!user.getPassword().equals(DEFAULT_PASS)) {
				sender.sendEmail(email, "Mimolet password recovery", 
						" Hello, User \n Your password is " + user.getPassword());
				return "true";
			} else {
				return "false";
			}
		} else {
			return "false";
		}
	}
	
	@RequestMapping(value = "/purchaseorder", method = RequestMethod.POST)
	@ResponseBody
	public String purchaseorder(@RequestParam("id") String id,
			@RequestParam("email") String email) {
		int orderid = Integer.valueOf(id); 
		log.fatal("Get request to purchase order with id =  " + orderid + " and email " + email);
		try {
			Order order = orderService.getOrderById(orderid);
			EmailSender sender = new EmailSender();
			String userMessage = "Спасибо за то что заказали у нас альбом! \n" + 
					"Ваш заказ: \n" + order.toString() + 
					"Ожидайте письмо с подробностями оплаты и доставки";
			String messageTitle = "Подробности заказа № " + orderid;
			sender.sendEmail(email, messageTitle, userMessage);
			String serviceMessage = "Новый заказ \n" + order.toString() + 
					"Pdf заказа доступен по этому адресу " + order.getLink();
			File destination = new File(servletContext.getRealPath("imgdesc/" + order.getOwnerId() + "_" + order.getId() + ".imdc"));
			if (destination.exists()) {
				FileInputStream fis = new FileInputStream(destination);
			    ObjectInputStream ois = new ObjectInputStream(fis);
			    @SuppressWarnings("unchecked")
				HashMap<Integer,PhotoData> workMap = (HashMap<Integer,PhotoData>) ois.readObject();
			    for (Map.Entry<Integer, PhotoData> entry : workMap.entrySet()) {
			        serviceMessage += "\n Данные по фото № " + (entry.getValue().getPageId() + 1) + "\n" + entry.getValue().toString();
			    }
			    ois.close();
			} else {
				return "false";
			}
			sender.sendEmail(SERVICE_EMAIL, messageTitle, serviceMessage);
			return "true";
		} catch (Exception e) {
			log.fatal("Something really goes wrong!");
			e.printStackTrace();
			return "false";
		}
	}
	
}
