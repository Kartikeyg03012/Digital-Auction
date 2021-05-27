package com.digital.auction.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.digital.auction.dao.UserRepository;
import com.digital.auction.entities.BidderModel;
import com.digital.auction.entities.MailModel;
import com.digital.auction.entities.Product;
import com.digital.auction.entities.User;
import com.digital.auction.service.BiddingService;
import com.digital.auction.service.MailService;
import com.digital.auction.service.ProductService;
import com.digital.auction.service.UserService;

@Controller
@RequestMapping("/user")
public class UserForBuyersController {

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@Autowired
	private BiddingService biddingService;

	@Autowired
	private MailService mailService;

	@GetMapping("/dashboard")
	public String userDashboard(Model model, Principal p) {
		String name = p.getName();
		User user = userService.getUserDataByEmail(name);
		model.addAttribute("data", user);
		return "user-pages/user-dashboard";
	}

	@GetMapping("/show-profile")
	public String ShowProfile(Model model, Principal p) {
		String name = p.getName();
		User user = userService.getUserDataByEmail(name);
		model.addAttribute("data", user);
		return "user-pages/user-profile-detail";
	}

	@PostMapping("/update-user-data")
	public String updateUserData() {

		return "user-pages/user-profile-detail";
	}

	// Request User BUYER TO SELLER.....
	@GetMapping("/request-seller-page")
	public String requestPage(Model model, HttpSession session, Principal p) {
		User user = userService.getUserDataByEmail(p.getName());
		model.addAttribute("data", user);
		return "user-pages/request-page";
	}

	// Process Request User BUYER TO SELLER.....
	@PostMapping("/process-request")
	public String processRequest(@RequestParam("name_on_account") String name,
			@RequestParam("bank_name") String bank_name, @RequestParam("ifsc_code") String ifsc_code,
			@RequestParam("account_no") String account_no, Model model, HttpSession session, Principal p)
			throws IOException {
		User user = userService.getUserDataByEmail(p.getName());
		System.out.println("Inside Process Request");

		user.setName_on_account(name);
		user.setBank_name(bank_name);
		user.setIfsc_code(ifsc_code);
		user.setAccount_no(account_no);
		user.setStatus("REQUEST FOR SELLER ACCOUNT");

		String subject = "Digital Auction || Request For Seller Account";
		String message = "<h1>Welcome To Digital Auction</h1><br/>Dear " + user.getName()
				+ ", <br/>Your Request For Seller Account Has Been Recived by us, if your details will be verified successfully,<br/>"
				+ "we will notify you. it will take upto 24 hours.<br/>"
				+ "Thanks For Connecting With us<br/>Regards<br/>Digital Auction";
		MailModel mail = new MailModel(user.getEmail(), subject, message);
		mailService.sendingmail(mail);

		User adduser = userService.adduser(user);
		System.out.println("Save Data Succesfully: " + adduser);
		model.addAttribute("data", user);
		return "redirect:https://pages.razorpay.com/pl_GzGf1oulLE1aCC/view";
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
		return "user-pages/my-order";
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
				return "user-pages/my-wishlist";
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", "No Products Found!!!");
			model.addAttribute("data", user);
			return "user-pages/user-dashboard";
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
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("type", "alert-danger");
			session.setAttribute("msg", "Something Went Wrong!!!");
		}
		model.addAttribute("data", temp);
		return "redirect:/user/my-wishlist";
	}

}
