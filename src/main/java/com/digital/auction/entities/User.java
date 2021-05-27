package com.digital.auction.entities;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "user_data")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String name;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	private String number;

	private String password;

	private String address;

	private String city;

	private String state;

	private String country;

	private String zipcode;
	private String token;
	private String imgUrl;
	private String created_date;
	private String role;
	private String status;
	private boolean archive;
	private boolean verifiedStatus;

	// Account details For Seller....
	private String name_on_account;
	private String bank_name;
	private String ifsc_code;
	private String account_no;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<Product> product = new ArrayList<>();

	@ElementCollection
	private List<Integer> wishListProduct = new ArrayList<Integer>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<BidderModel> biddingList  = new ArrayList<>();

	public User() {
		super();

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	public boolean isVerifiedStatus() {
		return verifiedStatus;
	}

	public void setVerifiedStatus(boolean verifiedStatus) {
		this.verifiedStatus = verifiedStatus;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIfsc_code() {
		return ifsc_code;
	}

	public void setIfsc_code(String ifsc_code) {
		this.ifsc_code = ifsc_code;
	}

	public String getName_on_account() {
		return name_on_account;
	}

	public void setName_on_account(String name_on_account) {
		this.name_on_account = name_on_account;
	}

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public List<Integer> getWishListProduct() {
		return wishListProduct;
	}

	public void setWishListProduct(List<Integer> wishListProduct) {
		this.wishListProduct = wishListProduct;
	}

	public List<Product> getProduct() {
		return product;
	}

	public void setProduct(List<Product> product) {
		this.product = product;
	}
	
	
	public List<BidderModel> getBiddingList() {
		return biddingList;
	}

	public void setBiddingList(List<BidderModel> biddingList) {
		this.biddingList = biddingList;
	}

	public User(int id, String name, String email, String number, String password, String address, String city,
			String state, String country, String zipcode, String token, String imgUrl, String created_date, String role,
			String status, boolean archive, boolean verifiedStatus, String name_on_account, String bank_name,
			String ifsc_code, String account_no, List<Product> product, List<Integer> wishListProduct,
			List<BidderModel> biddingList) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.number = number;
		this.password = password;
		this.address = address;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zipcode = zipcode;
		this.token = token;
		this.imgUrl = imgUrl;
		this.created_date = created_date;
		this.role = role;
		this.status = status;
		this.archive = archive;
		this.verifiedStatus = verifiedStatus;
		this.name_on_account = name_on_account;
		this.bank_name = bank_name;
		this.ifsc_code = ifsc_code;
		this.account_no = account_no;
		this.product = product;
		this.wishListProduct = wishListProduct;
		this.biddingList = biddingList;
	}
	
	

}
