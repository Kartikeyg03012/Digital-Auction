package com.digital.auction.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.digital.auction.entities.ContactUsModel;
import com.digital.auction.entities.User;

public interface ContactRepository extends JpaRepository<ContactUsModel, Integer> {

	// GET ALL QUERIES From Contact Table
	// implimention pagination
	// current page value
	// how much value in current page - like 5 values in one page
	@Query("from ContactUsModel as c ORDER by id DESC")
	public Page<ContactUsModel> findAllQuery(Pageable pageAble);

	// Sort Users on the basis of Archive status with pagination
	@Query("select u from ContactUsModel u WHERE u.status=:status ORDER by id DESC")
	public Page<ContactUsModel> getContactUsModelByStatus(@Param("status") String status, Pageable pageAble);
}
