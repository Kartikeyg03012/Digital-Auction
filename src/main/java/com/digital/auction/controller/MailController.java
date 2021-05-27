package com.digital.auction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.digital.auction.entities.MailModel;
import com.digital.auction.service.MailService;

@RestController
@CrossOrigin
public class MailController {

	@Autowired
	private MailService mailService;

	@PostMapping("/send-mail")
	public ResponseEntity<?> sendEmail(@RequestBody MailModel mailModel, Model model) {
		try {
			mailService.sendingmail(mailModel);
			System.out.println("Mail Send SuccessFully");
			return ResponseEntity.ok(new MailModel(mailModel.getTo(), mailModel.getSubject(), mailModel.getMessage()));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
