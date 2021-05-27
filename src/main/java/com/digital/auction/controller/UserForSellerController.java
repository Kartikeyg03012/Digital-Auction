package com.digital.auction.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.digital.auction.entities.BidderModel;
import com.digital.auction.entities.MailModel;
import com.digital.auction.entities.Product;
import com.digital.auction.entities.User;
import com.digital.auction.service.BiddingService;
import com.digital.auction.service.MailService;
import com.digital.auction.service.ProductService;
import com.digital.auction.service.UserService;

@Controller
@RequestMapping("/seller")
public class UserForSellerController {

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private BiddingService biddingService;

	@Autowired
	private MailService mailService;

	// Show the page --> Seller Dashboard
	@GetMapping("/dashboard")
	public String userDashboard(Model model, Principal p) {
		String name = p.getName();
		User user = userService.getUserDataByEmail(name);
		model.addAttribute("data", user);
		return "seller-pages/seller-dashboard";
	}

	// Show the page --> Seller Profile
	@GetMapping("/show-profile")
	public String ShowProfile(Model model, Principal p) {
		String name = p.getName();
		User user = userService.getUserDataByEmail(name);
		model.addAttribute("data", user);
		return "seller-pages/seller-profile-detail";
	}

	// Show the page --> Add product
	@GetMapping("/show-add-product")
	public String showAddProduct(Model model, Principal p) {
		String name = p.getName();
		User user = userService.getUserDataByEmail(name);
		model.addAttribute("data", user);
		return "seller-pages/add-product";
	}

	// Adding The Products in DataBase
	@RequestMapping(value = "/add-products", method = RequestMethod.POST)
	public String addProduct(@ModelAttribute Product product, @RequestParam("image") MultipartFile file1, Model model,
			Principal principal, HttpSession session, BindingResult bindResult) {

		model.addAttribute("title", "Register");

		String name = principal.getName();
		User user = userService.getUserDataByEmail(name);

		try {
			if (bindResult.hasErrors()) {
				return "seller-pages/add-product";
			}

			// uploading File and Save
			String img_name = file1.getOriginalFilename();
			product.setImage_url(img_name);
			File savefile = new ClassPathResource("static/img").getFile();
			Path path = Paths.get(savefile.getAbsolutePath() + File.separator + img_name);
			Files.copy(file1.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			// System.out.println("Image Uploaded SuccessFully");

			product.setUser(user);
			// System.out.println(product.getAmount() + " This is The
			// Amount--------------------------------------------");
			product.setBid_amount(product.getAmount());
			product.setStatus("Pending");
			user.getProduct().add(product);
			User result = userService.adduser(user);
			// System.out.println(result);

			String subject = "Digital Auction || Add Product";
			String message = "<h1>Welcome To Digital Auction</h1><br/>Dear " + user.getName() + ", <br/>Your Product "
					+ product.getName()
					+ " Has Been Add Successfully. once it will verify by team it will take some time.<br/> "
					+ "We will send you a mail if it is verifed successfully<br/>"
					+ "Thanks For Connecting With Us<br/>Regards<br/>Digital Auction";
			MailModel mail = new MailModel(user.getEmail(), subject, message);
			mailService.sendingmail(mail);

			model.addAttribute("data", result);
			session.setAttribute("type", "alert-success");
			session.setAttribute("msg", "Success Fully Add Your Product");

		} catch (Exception e) {
			model.addAttribute("data", user);
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", e.getMessage());
			return "seller-pages/add-product";
		}

		return "seller-pages/add-product";
	}

	// Show the page --> Show All Products With Pagination
	@GetMapping("/show-my-products/{page}")
	public String showMyProduct(@PathVariable("page") Integer page, Model model, Principal p) {
		String name = p.getName();
		User user = userService.getUserDataByEmail(name);

		// impliment pagination
		// current page value-1
		// per page value - 5
		Pageable pageable = PageRequest.of(page, 3);
		Page<Product> myProducts = productService.getproductById(user.getId(), pageable);

		model.addAttribute("data", user);
		model.addAttribute("list", myProducts);
		model.addAttribute("currentpage", page);
		model.addAttribute("totalpages", myProducts.getTotalPages());
		return "seller-pages/show-my-product";
	}

	// Full product details
	@GetMapping("/productDetailsById/{id}")
	public String productDetailsById(@PathVariable("id") int id, Model model, Principal p) {
		String name = p.getName();
		User user = userService.getUserDataByEmail(name);
		Product productDetailById = productService.getProductDetailById(id);
		if (user.getId() == productDetailById.getUser().getId()) {
			model.addAttribute("product", productDetailById);
		}
		model.addAttribute("data", user);
		return "seller-pages/product_full_detail";
	}

	// delete product by id
	@GetMapping("/delete-product/{id}")
	public String deleteProductById(@PathVariable("id") int id, Model model, Principal p, HttpSession session)
			throws IOException {
		String name = p.getName();
		User user = userService.getUserDataByEmail(name);
		Product product = productService.getProductDetailById(id);

		if (user.getId() == product.getUser().getId()) {
			product.setUser(null);
			productService.deleteProductById(product);

			String subject = "Digital Auction || Product Deleted";
			String message = "<h1>Welcome To Digital Auction</h1><br/>Dear " + user.getName() + ", <br/>Your Product "
					+ product.getName() + " Has Been Deleted Successfully. <br/>"
					+ "Thanks For Connecting With Us<br/>Regards<br/>Digital Auction";
			MailModel mail = new MailModel(user.getEmail(), subject, message);
			mailService.sendingmail(mail);

			session.setAttribute("type", "alert-success");
			session.setAttribute("msg", "SuccessFully Deleted");
			model.addAttribute("data", user);

		} else {
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", " You Not Authorise To Delete This Product");
			model.addAttribute("data", user);

		}
		return "redirect:/seller/show-my-products/0";
	}

	// Show Update Page For Product
	@PostMapping("/update-product/{id}")
	public String searchProductForUpdate(@PathVariable("id") int id, Model model, Principal p) {
		User user = userService.getUserDataByEmail(p.getName());
		Product product = productService.getProductDetailById(id);
		model.addAttribute("data", user);
		model.addAttribute("product", product);
		return "seller-pages/update-product";
	}

	// Update Product By Id
	@RequestMapping(value = "/update-my-product", method = RequestMethod.POST)
	public String updateProduct(@ModelAttribute Product product, @RequestParam("image") MultipartFile file1,
			Model model, Principal principal, HttpSession session, BindingResult bindResult) {

		model.addAttribute("title", "Register");

		User user = userService.getUserDataByEmail(principal.getName());

		Product oldProductDetails = productService.getProductDetailById(product.getId());

		try {
			// uploading only when if new file chooses
			if (!file1.isEmpty()) {

				// delete File
				if (oldProductDetails.getImage_url() != null) {
					File oldimage = new ClassPathResource("static/img").getFile();
					File deleteImage = new File(oldimage, oldProductDetails.getImage_url());
					deleteImage.delete();
				}

				// Uploading New File
				// SimpleDateFormat dateFormat = new SimpleDateFormat();
				System.out.println("This is My image File Name :" + file1.getOriginalFilename());
				product.setImage_url(file1.getOriginalFilename());
				File savefile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file1.getOriginalFilename());
				Files.copy(file1.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			} else {
				product.setImage_url(oldProductDetails.getImage_url());
			}

			product.setUser(user);

			// update the user
			Product result = productService.adduser(product);

			model.addAttribute("data", result);
			session.setAttribute("type", "alert-success");
			session.setAttribute("msg", "Success Fully Update Your Product");

		} catch (Exception e) {
			model.addAttribute("data", user);
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", e.getMessage());
			return "seller-pages/seller-dashboard";
		}

		return "redirect:productDetailsById/" + product.getId();
	}

	// Show Update Page For User Profile
	@PostMapping("/update-profile/{id}")
	public String searchUserProfileForUpdate(@PathVariable("id") int id, Model model, Principal p) {
		User user = userService.getUserDataByEmail(p.getName());

		model.addAttribute("data", user);

		return "seller-pages/seller-profile-update";
	}

	// Update User Profile By Id
	@RequestMapping(value = "/update-my-profile", method = RequestMethod.POST)
	public String updateUserProfile(@ModelAttribute User user, Model model, Principal principal, HttpSession session,
			BindingResult bindResult) {

		model.addAttribute("title", "Register");

		User oldUserData = userService.getUserDataByEmail(principal.getName());

		try {
			// This Comment Code for uploading user image
			/*
			 * // uploading only when if new file chooses if (!file1.isEmpty()) {
			 * 
			 * //delete File if(oldProductDetails.getImage_url()!=null) { File oldimage =
			 * new ClassPathResource("static/img").getFile(); File deleteImage=new
			 * File(oldimage,oldProductDetails.getImage_url()); deleteImage.delete(); }
			 * 
			 * // Uploading New File // SimpleDateFormat dateFormat = new
			 * SimpleDateFormat(); System.out.println("This is My image File Name :" +
			 * file1.getOriginalFilename());
			 * product.setImage_url(file1.getOriginalFilename()); File savefile = new
			 * ClassPathResource("static/img").getFile(); Path path =
			 * Paths.get(savefile.getAbsolutePath() + File.separator +
			 * file1.getOriginalFilename()); Files.copy(file1.getInputStream(), path,
			 * StandardCopyOption.REPLACE_EXISTING); } else {
			 * 
			 * }
			 */

			System.out.println("This is Old User Data" + oldUserData);

			user.setId(oldUserData.getId());
			user.setPassword(oldUserData.getPassword());
			user.setArchive(oldUserData.isArchive());
			user.setVerifiedStatus(oldUserData.isVerifiedStatus());
			user.setRole(oldUserData.getRole());
			user.setCreated_date(oldUserData.getCreated_date());

			User result = userService.adduser(user);

			model.addAttribute("data", result);
			session.setAttribute("type", "alert-success");
			session.setAttribute("msg", "Success Fully Update Your Profile");

		} catch (Exception e) {
			model.addAttribute("data", oldUserData);
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", e.getMessage());
			return "seller-pages/seller-dashboard";
		}

		return "seller-pages/seller-dashboard";
	}

	// Seller Change Password By Old Password
	@GetMapping("/change-password/{id}")
	public String changePassword(@PathVariable("id") int id, Model model, Principal p, HttpSession session) {

		User user = userService.getUserDataByEmail(p.getName());
		if (id == user.getId()) {
			model.addAttribute("data", user);
			return "seller-pages/change-password-page";
		}
		model.addAttribute("data", user);
		session.setAttribute("type", "alert-danger");
		session.setAttribute("msg", "Something Went Wrong! Please Try Again...");
		return "seller-pages/seller-dashboard";

	}

	// Change My Password After Authenticate
	@PostMapping("/change-my-password")
	public String changeMypassword(@RequestParam("password") String password,
			@RequestParam("retype_pwd") String retype_pwd, Model model, HttpSession session, Principal p)
			throws IOException {
		System.out.println("Principle value.........." + p.getName());
		User getData = userService.getUserDataByEmail(p.getName());
		if (!(password.equals(retype_pwd))) {
			// System.out.println(password + " " + retype_pwd);
			model.addAttribute("data", getData);
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", "Password didn't match, Please try again!!");
		} else {
			getData.setPassword(passwordEncoder.encode(password));
			userService.adduser(getData);
			// System.out.println("Password Change Successfully");

			String subject = "Digital Auction || Product Deleted";
			String message = "<h1>Welcome To Digital Auction</h1><br/>Dear " + getData.getName()
					+ ", <br/>Your Password Has Been Changed Successfully. If it was not you, please contact to our team immediately. <br/>"
					+ "Thanks For Connecting With Us<br/>Regards<br/>Digital Auction";
			MailModel mail = new MailModel(getData.getEmail(), subject, message);
			mailService.sendingmail(mail);

			model.addAttribute("data", getData);
			session.setAttribute("type", "alert-success");
			session.setAttribute("msg", "Password Change Successfully");
		}
		return "seller-pages/seller-dashboard";
	}

	@GetMapping("/done-product/{id}")
	public String doneProduct(@PathVariable("id") int id, Model model, Principal p, HttpSession session)
			throws IOException {
		User user = userService.getUserDataByEmail(p.getName());
		Product product = productService.getProductDetailById(id);

		product.setDoneAt(product.getBid_amount());
		product.setStatus("Processing");
		product.setArchive(true);

		productService.adduser(product);

		// send to admin
		String subject = "Digital Auction || Product Done Request";
		String message = "<h1>Welcome To Digital Auction</h1><br/>Dear Admin, " + "<br/>A New Request of product "
				+ product.getName() + " and ID " + product.getId() + " is generated. <br/>"
				+ "Thanks For Connecting With Us<br/>Regards<br/>Digital Auction";
		MailModel mail = new MailModel("digitalauction.info@gmail.com", subject, message);
		mailService.sendingmail(mail);

		// Remaining PART....
		// send to buyer
		
		
//		String subject1 = "Digital Auction || Product Done Request";
//		String message1 = "<h1>Welcome To Digital Auction</h1><br/>Dear Admin, " + "<br/>A New Request of product "
//				+ product.getName() + " and ID " + product.getId() + " is generated. <br/>"
//				+ "Thanks For Connecting With Us<br/>Regards<br/>Digital Auction";
//		MailModel mail1 = new MailModel("digitalauction.info@gmail.com", subject1, message1);
//		mailService.sendingmail(mail1);

		model.addAttribute("data", user);
		session.setAttribute("type", "alert-success");
		session.setAttribute("msg", "Your Product Has Been Sold");
		return "seller-pages/seller-dashboard";
	}

	@GetMapping("/my-orders")
	public String myOrders(Model model, HttpSession session, Principal p) {
		User user = userService.getUserDataByEmail(p.getName());

		List<BidderModel> listOfBiddingData = biddingService.getListOfBiddingData(user.getId());
		List<Product> listOfBiddingProducts = new ArrayList<Product>();
		for (BidderModel bidderModel : listOfBiddingData) {
			listOfBiddingProducts.add(productService.getBiddingProducts(bidderModel.getProductId()));
		}

		model.addAttribute("list", listOfBiddingProducts);
		model.addAttribute("amountList", listOfBiddingData);
		model.addAttribute("data", user);
		return "seller-pages/my-order";
	}

	@GetMapping("/my-wishlist")
	public String myWishList(Model model, HttpSession session, Principal p) {
		User user = userService.getUserDataByEmail(p.getName());

		try {
			Iterable<Integer> wishlist = user.getWishListProduct();
			if (wishlist != null) {
				List<Product> wishListProducts = productService.getWishListProducts(wishlist);
				for (Product product : wishListProducts) {
					if (!product.isArchive()) {
						model.addAttribute("list", wishListProducts);
					}
				}
				model.addAttribute("data", user);
				return "seller-pages/my-wishlist";
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", "No Products Found!!!");
			model.addAttribute("data", user);
			return "seller-pages/seller-dashboard";
		}

	}

	@GetMapping("/remove-from-wishlist/{id}")
	public String removeProductWishList(@PathVariable("id") int id, Model model, HttpSession session, Principal p) {
		User user = userService.getUserDataByEmail(p.getName());
		User temp = null;
		try {
			List<Integer> wishListProduct = user.getWishListProduct();
			for (int i = 0; i < wishListProduct.size(); i++) {
				if (wishListProduct.get(i) == id) {
					System.out.println("Insiden If");
					wishListProduct.remove(i);
					user.setWishListProduct(wishListProduct);
					temp = userService.adduser(user);
				}
			}
			Iterable<Integer> wishlist = temp.getWishListProduct();
			List<Product> wishListProducts = productService.getWishListProducts(wishlist);
			model.addAttribute("list", wishListProducts);

			session.setAttribute("type", "alert-success");
			session.setAttribute("msg", "Product Removed From Wishlist");
		} catch (

		Exception e) {
			e.printStackTrace();
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", "Something Went Wrong!!!");
		}
		model.addAttribute("data", temp);
		return "redirect:/seller/my-wishlist";
	}

}
