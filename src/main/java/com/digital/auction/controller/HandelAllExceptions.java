package com.digital.auction.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class HandelAllExceptions {
	
	//for any exception occur in our project it will be handel

	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Exception.class)
	public String anyException(Model model) {
		model.addAttribute("msg", "Exception Occours.....");
		return "error_page";
	}
}
