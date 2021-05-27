package com.digital.auction.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "digital_auction_products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	@Column(length = 4000)
	private String description;
	@Column(length = 4000)
	private String specifications;
	private Long amount;
	private Long bid_amount;
	private Long doneAt;
	private String category;
	private Date maxDateOfProduct;
	private Long maxAmountOfProduct;
	private int totalLeftDays;
	private String image_url;
	private String status;
	private boolean verified;
	private boolean archive;

	@ManyToOne
	@JsonIgnore
	private User user;

	public Long getDoneAt() {
		return doneAt;
	}

	public void setDoneAt(Long doneAt) {
		this.doneAt = doneAt;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getBid_amount() {
		return bid_amount;
	}

	public void setBid_amount(Long bid_amount) {
		this.bid_amount = bid_amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public Date getMaxDateOfProduct() {
		return maxDateOfProduct;
	}

	public void setMaxDateOfProduct(Date maxDateOfProduct) {
		this.maxDateOfProduct = maxDateOfProduct;
	}

	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getMaxAmountOfProduct() {
		return maxAmountOfProduct;
	}

	public void setMaxAmountOfProduct(Long maxAmountOfProduct) {
		this.maxAmountOfProduct = maxAmountOfProduct;
	}

	public int getTotalLeftDays() {
		return totalLeftDays;
	}

	public void setTotalLeftDays(int totalLeftDays) {
		this.totalLeftDays = totalLeftDays;
	}

	public Product(int id, String name, String description, String specifications, Long amount, Long bid_amount,
			Long doneAt, String category, Date maxDateOfProduct, Long maxAmountOfProduct, int totalLeftDays,
			String image_url, String status, boolean verified, boolean archive,
			User user) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.specifications = specifications;
		this.amount = amount;
		this.bid_amount = bid_amount;
		this.doneAt = doneAt;
		this.category = category;
		this.maxDateOfProduct = maxDateOfProduct;
		this.maxAmountOfProduct = maxAmountOfProduct;
		this.totalLeftDays = totalLeftDays;
		this.image_url = image_url;
		this.status = status;
		this.verified = verified;
		this.archive = archive;
		this.user = user;
	}

}
