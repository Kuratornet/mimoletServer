package com.mimolet.server.tools;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

public class EmailSender {
	private static final String SERVICE_EMAIL = "kuratornet@gmail.com";
	private static final String SERVICE_LOGIN = "ServiceBot";
	private static final String SERVICE_PASSWORD = "lookingForMyPassword?";
	private static final String SERVICE_HOST = "smtp.googlemail.com";
	
	public boolean sendEmail(String to, String subject, String message) {
		try {
			Email email = new SimpleEmail();
			email.setHostName(SERVICE_HOST);
			email.setSmtpPort(465);
			email.setAuthenticator(new DefaultAuthenticator(SERVICE_EMAIL, SERVICE_PASSWORD));
			email.setSSL(true);
			email.addTo(to, to);
			email.setFrom(SERVICE_EMAIL, SERVICE_LOGIN);
			email.setSubject(subject);
			email.setMsg(message);
			email.send();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
