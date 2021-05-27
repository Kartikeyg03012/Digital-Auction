package com.digital.auction.controller;

import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.digital.auction.dao.AdminRepository;
import com.digital.auction.entities.BidderModel;
import com.digital.auction.entities.ContactUsModel;
import com.digital.auction.entities.MailModel;
import com.digital.auction.entities.Product;
import com.digital.auction.entities.User;
import com.digital.auction.service.AdminService;
import com.digital.auction.service.BiddingService;
import com.digital.auction.service.ContactUsService;
import com.digital.auction.service.MailService;
import com.digital.auction.service.ProductService;
import com.digital.auction.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	// Note:- ***Keep in mind Admin Also An User So All the things we can also do
	// with user servie....

	@Autowired
	private AdminService adminService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ContactUsService contactUsService;

	@Autowired
	private MailService mailService;

	@Autowired
	private BiddingService biddingService;

	// Show Admin Dashboard
	@GetMapping("/dashboard")
	public String adminDashboard(Model model, Principal p) {
		User user = userService.getUserDataByEmail(p.getName());
		model.addAttribute("data", user);
		model.addAttribute("title", "Admin Login");
		return "admin-pages/admindashboard";
	}

	// Show All Users With Pagination
	@GetMapping("/show-user-profile/{page}")
	public String showUserProfile(@PathVariable("page") Integer page, Model model, Principal p) {
		User user = userService.getUserDataByEmail(p.getName());

		// System.out.println("user .........." + user);

		// impliment pagination
		// current page value-1
		// per page value - 5
		Pageable pageable = PageRequest.of(page, 2);
		Page<User> myUsers = adminService.getAllUsers(pageable);

		model.addAttribute("data", user);
		model.addAttribute("list", myUsers);
		model.addAttribute("currentpage", page);
		model.addAttribute("totalpages", myUsers.getTotalPages());
		// System.out.println("my users 2: " + myUsers);

		return "admin-pages/user-profile";
	}

	// Show the page --> Show All Products With Pagination
	@GetMapping("/show-all-products/{page}")
	public String showMyProduct(@PathVariable("page") Integer page, Model model, Principal p) {
		String name = p.getName();
		User user = userService.getUserDataByEmail(name);

		// impliment pagination
		// current page value-1
		// per page value - 5
		Pageable pageable = PageRequest.of(page, 3);
		Page<Product> myProducts = adminService.getAllProducts(pageable);

		model.addAttribute("data", user);
		model.addAttribute("list", myProducts);
		model.addAttribute("currentpage", page);
		model.addAttribute("totalpages", myProducts.getTotalPages());
		return "admin-pages/product-detail";
	}

	// Full product details on admin page
	@GetMapping("/productDetailsById/{id}")
	public String productDetailsById(@PathVariable("id") int id, Model model, Principal p) {
		String name = p.getName();
		User user = userService.getUserDataByEmail(name);
		Product productDetailById = productService.getProductDetailById(id);

		model.addAttribute("product", productDetailById);
		model.addAttribute("data", user);
		return "admin-pages/product_full_detail";
	}

	// Show All Products Of a particular User With Pagination
	@GetMapping("/show-my-products/{id}/{page}")
	public String showUserProduct(@PathVariable("id") int id, @PathVariable("page") Integer page, Model model,
			Principal p) {

		User user = userService.getUserDataByEmail(p.getName());

		// impliment pagination
		// current page value-1
		// per page value - 5
		Pageable pageable = PageRequest.of(page, 3);
		Page<Product> myProducts = productService.getproductById(id, pageable);

		model.addAttribute("data", user);
		model.addAttribute("id", id);
		model.addAttribute("list", myProducts);
		model.addAttribute("currentpage", page);
		model.addAttribute("totalpages", myProducts.getTotalPages());
		return "admin-pages/product-detail";
	}

	// Update Buyer To Seller When verifiaction is Done
	@GetMapping("/update-buyer-to-seller/{id}")
	public String updateToSeller(@PathVariable("id") int id, Model model, Principal p, HttpSession session) {
		User user = userService.getUserDataByEmail(p.getName());
		try {
			User userById = userService.getUserById(id);
			// set role
			userById.setRole("ROLE_SELLER");
			userById.setStatus(null);
			// update data
			User adduser = userService.adduser(userById);
			// sending mail
			String subject = "Digital Auction || Your Request Has Been Approved Successfully";
			String message = "<h1>Welcome To Digital Auction</h1><br/>Dear " + userById.getName()
					+ ", <br/>We are happy to inform you that your Seller Request Has Been Approved Successfully. Now, you can Add your products on our plateform.<br/>"
					+ "Thanks For Connecting With us<br/>Regards<br/>Digital Auction";
			MailModel mail = new MailModel(userById.getEmail(), subject, message);
			mailService.sendingmail(mail);

			model.addAttribute("data", user);
			session.setAttribute("type", "alert-success");
			session.setAttribute("msg", "Now, " + userById.getEmail() + " is a SELLER");

		} catch (Exception e) {
			model.addAttribute("data", user);
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", e.getMessage());

		}
		return "redirect:/admin/show-user-profile/0";
	}

	// Archive User ..
	@GetMapping("/archive-user/{id}")
	public String archiveUser(@PathVariable("id") int id, Model model, Principal p, HttpSession session) {
		User user = userService.getUserDataByEmail(p.getName());
		try {
			User userById = userService.getUserById(id);
			if (userById.isArchive()) {
				userById.setArchive(false);
				userById.setVerifiedStatus(true);

				String subject = "Digital Auction || Your Profile Has Live";
				String message = "<h1>Welcome To Digital Auction</h1><br/>Dear " + userById.getName()
						+ ", <br/>Your Profile Is Now Live. <br/>"
						+ "Thanks For Connecting With Us<br/>Regards<br/>Digital Auction";
				MailModel mail = new MailModel(userById.getEmail(), subject, message);
				mailService.sendingmail(mail);

				session.setAttribute("type", "alert-success");
				session.setAttribute("msg", userById.getEmail() + " is Visible...");
			} else {
				userById.setArchive(true);
				userById.setVerifiedStatus(false);

				String subject = "Digital Auction || Your Profile Has Been Archived!!!";
				String message = "<h1>Welcome To Digital Auction</h1><br/>Dear " + userById.getName()
						+ ", <br/>Your Profile Has Been Archive Due To Spamming. Contact To our Technical support team for more info <br/>"
						+ "Thanks For Connecting With Us<br/>Regards<br/>Digital Auction";
				MailModel mail = new MailModel(userById.getEmail(), subject, message);
				mailService.sendingmail(mail);

				session.setAttribute("type", "alert-success");
				session.setAttribute("msg", userById.getEmail() + " is a Archived...");
			}
			// update data
			User adduser = userService.adduser(userById);

			model.addAttribute("data", user);

		} catch (Exception e) {
			model.addAttribute("data", user);
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", e.getMessage());

		}
		return "redirect:/admin/show-user-profile/0";
	}

	// Update product Status To Verified By Admin
	@GetMapping("/product-visible/{id}")
	public String showProduct(@PathVariable("id") int id, Model model, Principal p, HttpSession session) {
		User user = userService.getUserDataByEmail(p.getName());
		try {
			Product productDetailById = productService.getProductDetailById(id);
			// set role
			productDetailById.setVerified(true);
			// update data
			Product addproduct = productService.adduser(productDetailById);
			String subject = "Digital Auction || Your Product Has Been Approved Successfully";
			String message = "<h1>Welcome To Digital Auction</h1><br/>Congratulations Dear "
					+ productDetailById.getUser().getName() + ", <br/> Your Product Is Now Live For Auction<br/>"
					+ "Thanks For Connecting With Us<br/>Regards<br/>Digital Auction";
			MailModel mail = new MailModel(productDetailById.getUser().getEmail(), subject, message);
			mailService.sendingmail(mail);

			model.addAttribute("data", user);
			session.setAttribute("type", "alert-success");
			session.setAttribute("msg", "Now, " + addproduct.getName() + " is Live");

		} catch (Exception e) {
			model.addAttribute("data", user);
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", e.getMessage());

		}
		return "redirect:/admin/show-all-products/0";
	}

	// Archive Product ..
	@GetMapping("/archive-product/{id}")
	public String archiveProduct(@PathVariable("id") int id, Model model, Principal p, HttpSession session) {
		User user = userService.getUserDataByEmail(p.getName());
		try {
			Product productDetailById = productService.getProductDetailById(id);
			if (productDetailById.isArchive()) {

				productDetailById.setArchive(false);
				productDetailById.setVerified(true);

				String subject = "Digital Auction || Your Product Has Been Live!!";
				String message = "<h1>Welcome To Digital Auction</h1><br/>Congratulations Dear "
						+ productDetailById.getUser().getName() + ", <br/> Your Product Is Now Live For Auction<br/>"
						+ "Thanks For Connecting With Us<br/>Regards<br/>Digital Auction";
				MailModel mail = new MailModel(productDetailById.getUser().getEmail(), subject, message);
				mailService.sendingmail(mail);

				session.setAttribute("type", "alert-success");
				session.setAttribute("msg", productDetailById.getName() + " is Visible...");
			} else {
				productDetailById.setArchive(true);
				productDetailById.setVerified(false);

				String subject = "Digital Auction || Your Product Has Been Archived !!!";
				String message = "<h1>Welcome To Digital Auction</h1><br/>Dear " + productDetailById.getUser().getName()
						+ ", <br/>Your Profile Has Been Archive Due To Spamming. Contact To our Technical support team for more info. <br/>"
						+ "Thanks For Connecting With Us<br/>Regards<br/>Digital Auction";
				MailModel mail = new MailModel(productDetailById.getUser().getEmail(), subject, message);
				mailService.sendingmail(mail);

				session.setAttribute("type", "alert-success");
				session.setAttribute("msg", productDetailById.getName() + " is a Archived...");
			}
			// update product
			Product adduser = productService.adduser(productDetailById);

			model.addAttribute("data", user);

		} catch (Exception e) {
			model.addAttribute("data", user);
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", e.getMessage());

		}
		return "redirect:/admin/show-all-products/0";
	}

	// Sorting Controller For User
	@GetMapping("/sorting-user/{data}/{page}")
	public String sortingUser(@PathVariable("data") String data, @PathVariable("page") Integer page, Model model,
			Principal p, HttpSession session) {
		User user = userService.getUserDataByEmail(p.getName());
		Pageable pageable = PageRequest.of(page, 2);
		Page<User> myUsers = null;
		try {
			// on the basis of verified status
			System.out.println("Value Of Data : " + data);
			if (data.equalsIgnoreCase("true")) {
				myUsers = adminService.getUserByVerification(true, pageable);
			} else if (data.equalsIgnoreCase("false")) {
				myUsers = adminService.getUserByVerification(false, pageable);
			} else if (data.equalsIgnoreCase("ROLE_BUYER") || data.equalsIgnoreCase("ROLE_SELLER")) {
				myUsers = adminService.getUserByRole(data, pageable);
			} else if (data.equalsIgnoreCase("archive")) {
				myUsers = adminService.getUserByArchive(true, pageable);
			} else if (data.equalsIgnoreCase("unarchive")) {
				myUsers = adminService.getUserByArchive(false, pageable);
			} else if (data.equalsIgnoreCase("request")) {
				data = "REQUEST FOR SELLER ACCOUNT";
				myUsers = adminService.getUserByRequestSeller(data, pageable);
			} else {
				myUsers = adminService.getAllUsers(pageable);
			}
			model.addAttribute("data", user);
			model.addAttribute("status", data);
			model.addAttribute("list", myUsers);
			model.addAttribute("currentpage", page);
			model.addAttribute("totalpages", myUsers.getTotalPages());
		} catch (Exception e) {
			model.addAttribute("status", data);
			model.addAttribute("data", user);
			model.addAttribute("list", myUsers);
			model.addAttribute("currentpage", page);
			model.addAttribute("totalpages", myUsers.getTotalPages());
		}

		return "admin-pages/user-sorting-profile";
	}

	// Show All Queries With Pagination
	@GetMapping("/show-all-query/{page}")
	public String showAllQuery(@PathVariable("page") Integer page, Model model, Principal p) {
		User user = userService.getUserDataByEmail(p.getName());

		// System.out.println("user .........." + user);

		// impliment pagination
		// current page value-1
		// per page value - 5
		Pageable pageable = PageRequest.of(page, 2);
		Page<ContactUsModel> myQuery = contactUsService.getAllQueriesOfCustomer(pageable);
		model.addAttribute("data", user);
		model.addAttribute("list", myQuery);
		model.addAttribute("currentpage", page);
		model.addAttribute("totalpages", myQuery.getTotalPages());
		// System.out.println("my users 2: " + myUsers);

		return "admin-pages/customer-query";
	}

	// update Query STATUS By ADMIN
	@GetMapping("/update-query/{data}/{id}")
	public String showQueryStatus(@PathVariable("data") String data, @PathVariable("id") int id, Model model,
			Principal p, HttpSession session) {
		User user = userService.getUserDataByEmail(p.getName());
		try {
			ContactUsModel queryDetailById = contactUsService.getQueryDetailById(id);
			// update Status
			if (data.equalsIgnoreCase("solve")) {
				queryDetailById.setStatus("Complete");
				queryDetailById.setUpdate_date(new Date().toLocaleString());
			} else {
				queryDetailById.setStatus("Un Resolve");
				queryDetailById.setUpdate_date(new Date().toLocaleString());
			}

			// update data
			ContactUsModel contactData = contactUsService.addQuery(queryDetailById);
			model.addAttribute("data", user);
			session.setAttribute("type", "alert-success");
			session.setAttribute("msg", "Status Update Successfully");

		} catch (Exception e) {
			model.addAttribute("data", user);
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", e.getMessage());

		}
		return "redirect:/admin/show-all-query/0";
	}

	// Sorting Controller For Query
	@GetMapping("/sorting-query/{data}/{page}")
	public String sortingQuery(@PathVariable("data") String data, @PathVariable("page") Integer page, Model model,
			Principal p, HttpSession session) {
		User user = userService.getUserDataByEmail(p.getName());
		Pageable pageable = PageRequest.of(page, 2);
		Page<ContactUsModel> contactUsModel = null;
		try {
			// on the basis of verified status
			// System.out.println("Value Of Data : " + data);
			if (data.equalsIgnoreCase("all")) {
				contactUsModel = contactUsService.getAllQueriesOfCustomer(pageable);
			} else {
				contactUsModel = contactUsService.sortQueryByStatus(data, pageable);
			}

			model.addAttribute("data", user);
			model.addAttribute("status", data);
			model.addAttribute("list", contactUsModel);
			model.addAttribute("currentpage", page);
			model.addAttribute("totalpages", contactUsModel.getTotalPages());
		} catch (Exception e) {
			model.addAttribute("status", data);
			model.addAttribute("data", user);
			model.addAttribute("list", contactUsModel);
			model.addAttribute("currentpage", page);
			model.addAttribute("totalpages", contactUsModel.getTotalPages());
		}

		return "admin-pages/query-sorting";
	}

	// Sorting Controller For Product on Admin Page
	@GetMapping("/sorting-product/{data}/{page}")
	public String sortingProductOnAdminPage(@PathVariable("data") String data, @PathVariable("page") Integer page,
			Model model, Principal p, HttpSession session) {
		User user = userService.getUserDataByEmail(p.getName());
		Pageable pageable = PageRequest.of(page, 3);
		Page<Product> productPage = null;
		try {
			// on the basis of verified status
			System.out.println("Value Of Data : " + data);
			if (data.equalsIgnoreCase("all")) {

				productPage = adminService.getAllProducts(pageable);
			} else if (data.equalsIgnoreCase("complete") || data.equalsIgnoreCase("pending")
					|| data.equalsIgnoreCase("Processing")) {

				productPage = adminService.getProductByStatusSorting(data, pageable);
			} else if (data.equalsIgnoreCase("verified")) {

				productPage = adminService.getProductByVerifySorting(true, pageable);
			} else if (data.equalsIgnoreCase("notverified")) {

				productPage = adminService.getProductByVerifySorting(false, pageable);
			} else if (data.equalsIgnoreCase("archive")) {

				productPage = adminService.getProductByArchiveSorting(true, pageable);
			} else if (data.equalsIgnoreCase("unarchive")) {

				productPage = adminService.getProductByArchiveSorting(false, pageable);
			}

			model.addAttribute("data", user);
			model.addAttribute("status", data);
			model.addAttribute("list", productPage);
			model.addAttribute("currentpage", page);
			model.addAttribute("totalpages", productPage.getTotalPages());
		} catch (Exception e) {
			model.addAttribute("status", data);
			model.addAttribute("data", user);
			model.addAttribute("list", productPage);
			model.addAttribute("currentpage", page);
			model.addAttribute("totalpages", productPage.getTotalPages());
		}

		return "admin-pages/product-detail";
	}

	// Update product Status To Complete By Admin
	@GetMapping("/product-done/{id}")
	public String updateProductToComplete(@PathVariable("id") int id, Model model, Principal p, HttpSession session) {
		User user = userService.getUserDataByEmail(p.getName());
		try {
			Product productDetailById = productService.getProductDetailById(id);
			productDetailById.setStatus("Complete");
			Product addproduct = productService.adduser(productDetailById);

			// Remaining PART....
			
			
			
			
			// mail to seller
			String subject = "Digital Auction || Your Product Has Been Sold Successfully";
			String message = "<h1>Welcome To Digital Auction</h1><br/>Dear " + productDetailById.getUser().getName()
					+ ", <br/>Your Product Has Been Sold Successfully. Payment Will be credited in 2-3 working days. <br/>"
					+ "Thanks For Connecting With Us<br/>Regards<br/>Digital Auction";
			MailModel mail = new MailModel(productDetailById.getUser().getEmail(), subject, message);
			mailService.sendingmail(mail);

			model.addAttribute("data", user);
			session.setAttribute("type", "alert-success");
			session.setAttribute("msg", addproduct.getName() + " is Completed");

		} catch (Exception e) {
			model.addAttribute("data", user);
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", e.getMessage());

		}
		return "redirect:/admin/show-all-products/0";
	}

}
