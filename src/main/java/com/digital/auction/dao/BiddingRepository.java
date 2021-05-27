package com.digital.auction.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.digital.auction.entities.BidderModel;

public interface BiddingRepository extends JpaRepository<BidderModel, Integer> {

	@Query("from BidderModel as b where b.user.id=:userId ORDER by id DESC")
	public List<BidderModel> listOfBiddingData(@Param("userId") int userId);

}
