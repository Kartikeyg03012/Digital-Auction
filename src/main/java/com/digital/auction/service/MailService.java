package com.digital.auction.service;

import org.springframework.stereotype.Service;

import com.digital.auction.entities.MailModel;

import java.io.IOException;
import java.util.Properties;
import java.util.SplittableRandom;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {

//		public static String genrateOtp(int length) {
//			SplittableRandom sr = new SplittableRandom();
//			StringBuffer sb = new StringBuffer();
//			for (int i = 0; i < length; i++) {
//				sb.append(sr.nextInt(0, 10));
	//
//			}
//			return sb.toString();
//		}

	// Genrate Random String
	public String genrateRandomString(int n) {

		// chose a Character random from this String
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

		// create StringBuffer size of AlphaNumericString
		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {

			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index = (int) (AlphaNumericString.length() * Math.random());

			// add Character one by one in end of sb
			sb.append(AlphaNumericString.charAt(index));
		}

		return sb.toString();
	}

	public void sendingmail(MailModel mailData) throws IOException {
		String to = mailData.getTo(); // to address. It can be any like gmail, yahoo etc.
		final String from = "digitalauction.info@gmail.com"; // from address. As this is using Gmail SMTP your from
																// address
																// should be
		// gmail
		final String password = "Digital#2021"; // password for from gmail address that you have used in above line.

		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "465");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setHeader("Content-Type", "text/html");
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(mailData.getSubject());
			String htmlData = mailData.getMessage();
			message.setContent(htmlData, "text/html");

			MimeBodyPart image = new MimeBodyPart();
			image.attachFile("img/fav.jpg");

			Transport.send(message);
			// System.out.println("Mail Sent.....");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	// Just For Test
//	public static void main(String[] args) throws IOException {
//		MailService mail = new MailService();
//		MailModel model = new MailModel("kartikeyg77@gmail.com", "Test Mail",
//				"Hello This is Only For Testing\n line Break");
//		mail.sendingmail(model);
//	}

}
