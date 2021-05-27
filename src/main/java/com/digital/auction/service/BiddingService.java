package com.digital.auction.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digital.auction.dao.BiddingRepository;
import com.digital.auction.entities.BidderModel;

@Service
public class BiddingService {
	@Autowired
	private BiddingRepository biddingRepository;

	// add bidding on product
	public BidderModel addBid(BidderModel bidderModel) {
		return biddingRepository.save(bidderModel);
	}

	public List<BidderModel> getListOfBiddingData(int userId) {
		return biddingRepository.listOfBiddingData(userId);
	}
}

