package com.digital.auction.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.digital.auction.dao.UserRepository;
import com.digital.auction.entities.MailModel;
import com.digital.auction.entities.User;
import com.digital.auction.service.EncryptDecrypt;
import com.digital.auction.service.MailService;
import com.digital.auction.service.SmsService;
import com.digital.auction.service.UserService;
import com.digital.auction.service.UserType;

@Controller
public class UserLoginController {

	@Autowired
	private UserService userService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private MailService mailService;

	// Register User
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String doRegister(@ModelAttribute User user, @RequestParam("image") MultipartFile file1,
			@RequestParam("password") String password, @RequestParam("retype_pwd") String retype_pwd, Model model,
			HttpSession session, BindingResult bindResult) {
		model.addAttribute("title", "Register");
		try {
			if (!(password.equals(retype_pwd))) {
				throw new Exception("Password Not Match");
			}

			if (bindResult.hasErrors()) {
				return "sign-up";
			}
			// uploading File and Save
			String img_name = file1.getOriginalFilename();
			user.setImgUrl(img_name);
			File savefile = new ClassPathResource("static/img").getFile();
			Path path = Paths.get(savefile.getAbsolutePath() + File.separator + img_name);
			Files.copy(file1.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Image Uploaded SuccessFully");

			// set encrypted password
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRole("ROLE_BUYER");
			user.setCreated_date(new Date().toGMTString());
			user.setArchive(false);
			user.setVerifiedStatus(false);

			// Save the User Details....
			User result = userService.adduser(user);

			// sending mail for verification.....
			String token = mailService.genrateRandomString(20);
			String URL = "http://localhost:9876/Digital-Auction/user-verification/" + token;
			System.out.println(URL);

			String subject = "Verification Link | Digital Auction";
			String message = "Dear "+result.getName()+", \n Thanks for reaching out. Glad to be in your network. Look forward to staying in touch.\n"
					+ "Your Verification Link is given below: \n\n" + URL;
			MailModel mailModel = new MailModel(result.getEmail(), subject, message);

			// call service for sending mail
			mailService.sendingmail(mailModel);
			System.out.println("Mail Sent.....");
			// set the token in database for future .....
			result.setToken(token);
			// save final result
			User finalResult = userService.adduser(result);

			model.addAttribute("data", finalResult);
			session.setAttribute("type", "alert-success");
			session.setAttribute("msg", "Success Fully Register!!!! Kindly check your Email for verification");
		} catch (Exception e) {
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", "Email is Already Registerd!!!");
		}

		return "sign-up";
	}

}
