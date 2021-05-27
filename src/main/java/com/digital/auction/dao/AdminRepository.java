package com.digital.auction.dao;

import java.util.List;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.digital.auction.entities.Product;
import com.digital.auction.entities.User;

public interface AdminRepository extends CrudRepository<User, Integer> {

	// GET ALL User
	// implimention pagination
	// current page value
	// how much value in current page - like 5 values in one page
	@Query("from User as u ORDER by id DESC")
	public Page<User> findAllUsers(Pageable pageAble);

	// GET ALL Product
	// implimention pagination
	// current page value
	// how much value in current page - like 5 values in one page
	@Query("from Product as p ORDER by id DESC")
	public Page<Product> findAllProducts(Pageable pageAble);

	// Sort Users on the basis of verification status with pagination
	@Query("select u from User u WHERE u.verifiedStatus=:status ORDER by id DESC")
	public Page<User> getUserByVerification(@Param("status") Boolean status, Pageable pageAble);

	// Sort Users on the basis of request status with pagination
	@Query("select u from User u WHERE u.status=:status ORDER by id DESC")
	public Page<User> getUserByRequestForSeller(@Param("status") String status, Pageable pageAble);

	// Sort Users on the basis of ROLE with pagination
	@Query("select u from User u WHERE u.role=:role ORDER by id DESC")
	public Page<User> getUserByRole(@Param("role") String role, Pageable pageAble);

	// Sort Users on the basis of Archive status with pagination
	@Query("select u from User u WHERE u.archive=:archive ORDER by id DESC")
	public Page<User> getUserByArchive(@Param("archive") Boolean archive, Pageable pageAble);

	// get products randomly...
	@Query("SELECT p FROM Product p WHERE p.verified=:verify AND p.archive=:archive ORDER BY RAND()")
	public Page<Product> findAllProductsRandomly(@Param("verify") Boolean verify, @Param("archive") Boolean archive,
			Pageable pageAble);

	// Sort Product on the basis of Archive status, verify Status and process Status
	// with pagination
	@Query("select p from Product p WHERE p.archive=:archive ORDER by id DESC")
	public Page<Product> getProductByArchiveSorting(@Param("archive") Boolean archive, Pageable pageAble);

	@Query("select p from Product p WHERE p.verified=:verified ORDER by id DESC")
	public Page<Product> getProductByVerifySorting(@Param("verified") Boolean verified, Pageable pageAble);

	@Query("select p from Product p WHERE p.status=:status ORDER by id DESC")
	public Page<Product> getProductByStatusSorting(@Param("status") String status, Pageable pageAble);

}
