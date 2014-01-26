package com.mimolet.server.tools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.SimpleEmail;

public class EmailSender {
	private static Log log = LogFactory.getLog(EmailSender.class);
	private static final String SERVICE_EMAIL = "kuret@ukr.net";
	private static final String SERVICE_LOGIN = "ServiceBot";
	private static final String SERVICE_PASSWORD = "111111";
	private static final String SERVICE_HOST = "smtp.ukr.net";
	
	public static boolean sendEmail(String to, String subject, String message) {
		try {
			SimpleEmail email = new SimpleEmail();
			email.setHostName(SERVICE_HOST);
			email.setSmtpPort(465);
			email.setAuthenticator(new DefaultAuthenticator(SERVICE_EMAIL, SERVICE_PASSWORD));
			email.setSSL(true);
			email.addTo(to, to);
			email.setFrom(SERVICE_EMAIL, SERVICE_LOGIN);
			email.setSubject(subject);
			email.setMsg(message);
			log.info(email.send());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
