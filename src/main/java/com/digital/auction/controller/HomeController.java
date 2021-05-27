package com.digital.auction.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.digital.auction.entities.BidderModel;
import com.digital.auction.entities.ContactUsModel;
import com.digital.auction.entities.MailModel;
import com.digital.auction.entities.Product;
import com.digital.auction.entities.User;
import com.digital.auction.service.AdminService;
import com.digital.auction.service.BiddingService;
import com.digital.auction.service.ContactUsService;
import com.digital.auction.service.EncryptDecrypt;
import com.digital.auction.service.MailService;
import com.digital.auction.service.ProductService;
import com.digital.auction.service.SmsService;
import com.digital.auction.service.UserService;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private SmsService smsService;

	@Autowired
	private BiddingService biddingService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private MailService mailService;

	@Autowired
	private EncryptDecrypt encryptDecrypt;

	@Autowired
	private ContactUsService contactUsService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private ProductService productService;

	// handler for default index page
	// Show the page --> Show All Products
	@GetMapping("/")
	public String showMyProduct(Model model) {
		Pageable pageable = PageRequest.of(0, 3);
		Pageable pageable2 = PageRequest.of(2, 3);

		List<Product> allProducts = productService.getAllProducts();
		Date d1 = new Date();
		for (Product product : allProducts) {
			long difference = product.getMaxDateOfProduct().getTime() - d1.getTime();
			int daysBetween = (int) (difference / (1000 * 60 * 60 * 24));
			if (daysBetween >= 0) {
				product.setTotalLeftDays(daysBetween);
				product.setArchive(false);
				productService.adduser(product);
			} else {
				product.setArchive(true);
				productService.adduser(product);
			}
		}
		Page<Product> myProducts = adminService.getProductsRandomly(true, false, pageable);
		Page<Product> myProducts2 = adminService.getProductsRandomly(true, false, pageable2);

		model.addAttribute("list2", myProducts2);
		model.addAttribute("list", myProducts);
		return "index";
	}

	// handler for default Home page
	@GetMapping("/home")
	public String homePage(Model model, HttpSession session, Principal p) {
		User userData = userService.getUserDataByEmail(p.getName());
		// System.out.println("********************************************** " +
		// userData.getEmail());
		Pageable pageable = PageRequest.of(0, 3);
		Pageable pageable2 = PageRequest.of(1, 3);
		Page<Product> myProducts = adminService.getProductsRandomly(true, false, pageable);
		Page<Product> myProducts2 = adminService.getProductsRandomly(true, false, pageable2);
		model.addAttribute("list2", myProducts2);
		model.addAttribute("list", myProducts);
		model.addAttribute("data", userData);
		return "home";
	}

	// Full product details For index and Home page
	@GetMapping("/productDetailsById/{id}")
	public String productDetailsByIdForIndex(@PathVariable("id") int id, Model model, Principal p) {
//		String name = p.getName();
//		User user = userService.getUserDataByEmail(name);
		Product productDetailById = productService.getProductDetailById(id);

		model.addAttribute("product", productDetailById);
		// model.addAttribute("data", user);
		return "product_full_detail";
	}

	// handler for custom sign page
	@GetMapping("/custom-login-form")
	public String loginForm() {
		return "login-page";
	}

	// Error Page
	@RequestMapping("/error-page")
	public String errorPage() {
		return "error_page";
	}

	// Show Sign Up Page
	@RequestMapping(value = "/sign-up", method = RequestMethod.GET)
	public String signUp(Model model) {
		model.addAttribute("title", "Register");
		return "sign-up";
	}

	// Show Forgot Password Page
	@GetMapping("/forgot")
	public String forgotPage() {
		return "show-forgot";
	}

	// Verify The User And Sending OTP On mobile Number....
	@PostMapping("/verify-user")
	public String verifyUser(@RequestParam("email") String email, Model model, HttpSession session) {
		try {
			User dataByEmail = userService.getUserDataByEmail(email);
			String actual_OTP = smsService.genrateOtp(6);
			String smsMessage = "Hello User, \nWelcome To Digital Auction Services. Your OTP(One Time password) is: "
					+ actual_OTP + ". Thanks For Connecting With Us, \nRegards- Digital Auction!!!";
			System.out.println(actual_OTP);
			if (email.equalsIgnoreCase(dataByEmail.getEmail())) {
				// System.out.println(dataByEmail.getNumber());
				// Sending OTP via SMS
				smsService.sendSms(smsMessage, dataByEmail.getNumber());
				session.setAttribute("otp", actual_OTP);
				session.setAttribute("email", email);
				session.setAttribute("number", dataByEmail.getNumber());
				return "verify-otp";
			} else {
				throw new UsernameNotFoundException("Something Went Wrong!!!!");
			}

		} catch (Exception e) {
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", "Email Is Not Registered Or Invalid");
			return "sign-up";

		}

	}

	// Verify The OTP Send By the User
	@PostMapping("/verify-otp")
	public String verifyOTP(@RequestParam("email") String email, @RequestParam("myotp") int myotp, Model model,
			HttpSession session) {
		System.out.println((String) (session.getAttribute("otp")));
		try {
			int id = Integer.parseInt((String) (session.getAttribute("otp")));
			if (myotp == id) {
				session.setAttribute("email", email);
				return "change-password";
			} else {
				throw new Exception("Invalid OTP");
			}

		} catch (Exception e) {
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", "Invalid OTP");
			return "sign-up";
		}
	}

	// Change My Password After Authenticate
	@PostMapping("/change-my-password")
	public String changeMypassword(@RequestParam("email") String email, @RequestParam("password") String password,
			@RequestParam("retype_pwd") String retype_pwd, Model model, HttpSession session) {

		try {
			User getData = userService.getUserDataByEmail(email);
			if (!(password.equals(retype_pwd))) {
				throw new UsernameNotFoundException("Something Went Wrong! Please Try Again...");
			} else {
				getData.setPassword(passwordEncoder.encode(password));
				User result = userService.adduser(getData);

				String subject = "Digital Auction || Password Changed";
				String message = "<h1>Welcome To Digital Auction</h1><br/>Dear " + getData.getName()
						+ ", <br/>Your Password Has Been Changed Successfully. If it was not you please contact to our technical team. <br/>"
						+ "Thanks For Connecting With Us<br/>Regards<br/>Digital Auction";
				MailModel mail = new MailModel(getData.getEmail(), subject, message);
				mailService.sendingmail(mail);

				session.setAttribute("type", "alert-success");
				session.setAttribute("msg", "Password Change Successfully");
				return "login-page";
			}
		} catch (Exception e) {
			System.out.println(password + "            " + retype_pwd);
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", "Something Went Wrong! Please Try Again...");
			return "login-page";
		}
	}

	// Sending OTP Using Email
	@GetMapping("/send-otp-email/{email}")
	public String sendOtpOnEmail(@PathVariable("email") String email, Model model, HttpSession session) {
		try {
			User dataByEmail = userService.getUserDataByEmail(email);
			String actual_OTP = smsService.genrateOtp(6);
			String emailMessage = "Hello User, \nWelcome To Digital Auction Services. Your OTP(One Time password) is: "
					+ actual_OTP;
			String subject = "OTP - Forgot Password | Digital Auction";
			System.out.println(actual_OTP);
			if (email.equalsIgnoreCase(dataByEmail.getEmail())) {
				System.out.println(dataByEmail.getEmail());
				// Sending OTP On Email
				MailModel mailModel = new MailModel(dataByEmail.getEmail(), subject, emailMessage);
				mailService.sendingmail(mailModel);
				System.out.println("Sending Mail.....");
				session.setAttribute("otp", actual_OTP);
				session.setAttribute("email", email);
				session.setAttribute("number", dataByEmail.getEmail());
				return "verify-otp";
			} else {
				throw new Exception("Something Went Wrong! Please Try Again...");
			}

		} catch (Exception e) {
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", "Email Is Not Registered Or Invalid");
			return "sign-up";
		}

	}

	// Email Verification Of User
	@GetMapping("/user-verification/{token}")
	public String verification(@PathVariable("token") String token, HttpSession session) {

		try {
			User dataByEmail = userService.getUserDataByToken(token);
			if (dataByEmail.equals(null)) {
				throw new UsernameNotFoundException("Invalid User!!!!");
			} else {
				dataByEmail.setVerifiedStatus(true);
				dataByEmail.setToken(null);

				User adduser = userService.adduser(dataByEmail);

				String subject = "Digital Auction || Verification Successfull";
				String message = "<h1>Welcome To Digital Auction</h1><br/>Dear " + adduser.getName()
						+ ", <br/>We are happy to inform you that your Request Has Been Approved Successfully. Now, you can login on our plateform.<br/>"
						+ "Thanks For Connecting With us<br/>Regards<br/>Digital Auction";
				MailModel mail = new MailModel(adduser.getEmail(), subject, message);
				mailService.sendingmail(mail);

				// System.out.println("User Added SuccessFully..." + adduser);
				session.setAttribute("type", "alert-success");
				session.setAttribute("msg", "Successfully Verified, Please Login To Continue");
				return "redirect:/custom-login-form";
			}
		} catch (Exception e) {
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", "Invalid Request...");
			return "redirect:/custom-login-form";
		}

	}

	// Contact us Controller For Queries......
	@PostMapping("/contact-us")
	public String contactUs(@ModelAttribute ContactUsModel contactUsModel, Model model, HttpSession session) {
		try {
			// Save Query In Database
			contactUsModel.setStatus("Process");
			contactUsModel.setCreate_date(new Date().toLocaleString());
			contactUsService.addQuery(contactUsModel);

			String subject = "Digital Auction || Query Genrated";
			String message = "<h1>Welcome To Digital Auction</h1><br/>Dear " + contactUsModel.getName()
					+ ", <br/>We Recive Your Query, and Our team will connect with you sortly!!<br/>"
					+ "Thanks For Connecting With us<br/>Regards<br/>Digital Auction";
			MailModel mail = new MailModel(contactUsModel.getEmail(), subject, message);
			mailService.sendingmail(mail);

			session.setAttribute("type", "alert-success");
			session.setAttribute("msg", "We Get Your Request,Our Team Will Reach You Soon!!");
		} catch (Exception e) {
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", "Invalid Request...");
		}
		return "redirect:/#footer";
	}

	// Adding Bid For Product
	@PostMapping("/add-bid")
	public String addBid(@RequestParam("uid") String uid, @RequestParam("pid") int pid,
			@RequestParam("addBid") Long addBid, Model model, HttpSession session) {
		User dataByEmail = userService.getUserDataByEmail(uid);
		Pageable pageable = PageRequest.of(0, 3);
		Pageable pageable2 = PageRequest.of(1, 3);
		Page<Product> myProducts = adminService.getProductsRandomly(true, false, pageable);
		Page<Product> myProducts2 = adminService.getProductsRandomly(true, false, pageable2);
		model.addAttribute("list2", myProducts2);
		model.addAttribute("list", myProducts);
		model.addAttribute("data", dataByEmail);

		Product productDetailById = productService.getProductDetailById(pid);

		try {
			Date d1 = new Date();
			if (productDetailById.getMaxDateOfProduct().before(d1)) {
				session.setAttribute("type", "alert-danger");
				session.setAttribute("msg", "Sorry!! Product Is No Longer Avaliable");
				productDetailById.setArchive(true);
				productService.adduser(productDetailById);
				model.addAttribute("data", dataByEmail);
				return "redirect:/home";
			} else if (addBid > productDetailById.getBid_amount()) {

				productDetailById.setBid_amount(addBid);

				BidderModel bidderModel = new BidderModel();
				bidderModel.setUser(dataByEmail);
				bidderModel.setProductId(productDetailById.getId());
				bidderModel.setActualAmount(productDetailById.getAmount());
				bidderModel.setBiddingAmount(addBid);
				bidderModel.setBidTime(new Date().toLocaleString());
				bidderModel.setStatus("Pending");
				bidderModel.setSelltime(null);

				//send mail to buyer
				String subject = "Digital Auction || Add Bid Successfully";
				String message = "<h1>Welcome To Digital Auction</h1><br/>Dear " + dataByEmail.getName()
						+ ", <br/>Your bid on product <b>" + productDetailById.getName()
						+ "</b> is added successfully, if your bid id the highest one, We will Notify you!!!  <br/>"
						+ "Thanks For Connecting With us<br/>Regards<br/>Digital Auction";
				MailModel mail = new MailModel(dataByEmail.getEmail(), subject, message);
				mailService.sendingmail(mail);
				
				
				//send mail to seller
				String subject1 = "Digital Auction || Add Bid Successfully";
				String message1 = "<h1>Welcome To Digital Auction</h1><br/>Dear " + productDetailById.getUser().getName()
						+ ", <br/>A Buyer bid on Your product <b>" + productDetailById.getName()
						+ "</b> is added successfully, if you want to sell this then done the Deal with the user.<br/>"
						+ "Thanks For Connecting With us<br/>Regards<br/>Digital Auction";
				MailModel mail1 = new MailModel(productDetailById.getUser().getEmail(), subject1, message1);
				mailService.sendingmail(mail1);

				productService.adduser(productDetailById);
				biddingService.addBid(bidderModel);
				session.setAttribute("type", "alert-success");
				session.setAttribute("msg", "BID Add Successfully");

			} else {
				session.setAttribute("type", "alert-danger");
				session.setAttribute("msg", "BID amount is must be greater then  current amount");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("data", dataByEmail);
		return "redirect:/home";
	}

	// Add To wishlist
	@GetMapping("/add-to-wishlist/{id}")
	public String addToWishlist(@PathVariable("id") int id, Model model, HttpSession session, Principal p) {
		User userDataByEmail = userService.getUserDataByEmail(p.getName());
		try {

			userDataByEmail.getWishListProduct().add(id);
			User adduser = userService.adduser(userDataByEmail);
			session.setAttribute("type", "alert-success");
			session.setAttribute("msg", "Product Added In Wishlist");
		} catch (Exception e) {
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", "Something Went Wrong!!!");
		}
		model.addAttribute("data", userDataByEmail);
		return "redirect:/home";
	}

	// Show All Products by archive and verify status with Sorting
	@GetMapping("/show-all-products/{sort}")
	public String showAllProducts(@PathVariable("sort") String sort, Model model, HttpSession session, Principal p) {
		User userDataByEmail = userService.getUserDataByEmail(p.getName());
		List<Product> allProducts = null;
		try {
			if (sort.equalsIgnoreCase("newest")) {
				allProducts = productService.getNewestProductByUnarchiveAndVerify(true, false);
			} else if (sort.equalsIgnoreCase("oldest")) {
				allProducts = productService.getOldestProductByUnarchiveAndVerify(true, false);
			} else {
				allProducts = productService.getAllProductByUnarchiveAndVerify(true, false);
			}
			model.addAttribute("list", allProducts);
		} catch (Exception e) {
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", "Something Went Wrong!!!");
		}
		model.addAttribute("data", userDataByEmail);
		return "show-all-product";
	}

	// Show All Products by archive and verify status with Sorting
	@GetMapping("/show-all-products")
	public String showAllProductsByCategory(@RequestParam("category") String category, Model model, HttpSession session,
			Principal p) {
		User userDataByEmail = userService.getUserDataByEmail(p.getName());
		List<Product> allProducts = null;
		try {
			//System.out.println("**********--------------------************************");
			allProducts = productService.getProductByCategoryByUnarchiveAndVerify(true, false, category);
			//System.out.println("**********--------------------************************" + allProducts);
			//System.out.println("**********--------------------************************" + category);
			model.addAttribute("list", allProducts);
		} catch (Exception e) {
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", "Something Went Wrong!!!");
		}
		model.addAttribute("data", userDataByEmail);
		return "show-all-product";
	}

}
