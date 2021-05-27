package com.digital.auction.entities;

public class MailModel {
	private String to;
	private String subject;
	private String message;

	public MailModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MailModel(String to, String subject, String message) {
		super();
		this.to = to;
		this.subject = subject;
		this.message = message;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MailModel [to=" + to + ", subject=" + subject + ", message=" + message + "]";
	}

}
