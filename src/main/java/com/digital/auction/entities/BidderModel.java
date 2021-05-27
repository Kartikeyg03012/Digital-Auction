package com.digital.auction.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "bidding_data")
public class BidderModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int productId;
	private Long actualAmount;
	private Long biddingAmount;
	private String bidTime;
	private String selltime;
	private String status;

	@ManyToOne
	@JsonIgnore
	private User user;

	public BidderModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public Long getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(Long actualAmount) {
		this.actualAmount = actualAmount;
	}

	public Long getBiddingAmount() {
		return biddingAmount;
	}

	public void setBiddingAmount(Long biddingAmount) {
		this.biddingAmount = biddingAmount;
	}

	public String getBidTime() {
		return bidTime;
	}

	public void setBidTime(String bidTime) {
		this.bidTime = bidTime;
	}

	public String getSelltime() {
		return selltime;
	}

	public void setSelltime(String selltime) {
		this.selltime = selltime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BidderModel(int id, int productId, Long actualAmount, Long biddingAmount, String bidTime, String selltime,
			String status, User user) {
		super();
		this.id = id;
		this.productId = productId;
		this.actualAmount = actualAmount;
		this.biddingAmount = biddingAmount;
		this.bidTime = bidTime;
		this.selltime = selltime;
		this.status = status;
		this.user = user;
	}

	@Override
	public String toString() {
		return "BidderModel [id=" + id + ", productId=" + productId + ", actualAmount=" + actualAmount
				+ ", biddingAmount=" + biddingAmount + ", bidTime=" + bidTime + ", selltime=" + selltime + ", status="
				+ status + ", user=" + user + "]";
	}

}
