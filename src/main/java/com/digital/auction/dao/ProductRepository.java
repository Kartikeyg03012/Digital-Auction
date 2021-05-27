package com.digital.auction.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.digital.auction.entities.Product;
import com.digital.auction.entities.User;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	// implimention pagination
	// current page value
	// how much value in current page - like 5 values in one page
	@Query("from Product as p where p.user.id=:userId ORDER by id DESC")
	public Page<Product> findProductById(@Param("userId") int userId, Pageable pageAble);

	@Query("SELECT p FROM Product p WHERE p.verified=:verify AND p.archive=:archive ORDER BY RAND()")
	public List<Product> getAllProductByUnarchiveAndVerify(@Param("verify") Boolean verify,
			@Param("archive") Boolean archive);

	@Query("SELECT p FROM Product p WHERE p.verified=:verify AND p.archive=:archive ORDER by id DESC")
	public List<Product> getNewestProductByUnarchiveAndVerify(@Param("verify") Boolean verify,
			@Param("archive") Boolean archive);

	@Query("SELECT p FROM Product p WHERE p.verified=:verify AND p.archive=:archive")
	public List<Product> getOldestProductByUnarchiveAndVerify(@Param("verify") Boolean verify,
			@Param("archive") Boolean archive);

	@Query("SELECT p FROM Product p WHERE p.verified=:verify AND p.archive=:archive AND p.category=:category ORDER by id DESC")
	public List<Product> getCategoryProductByUnarchiveAndVerify(@Param("verify") Boolean verify,
			@Param("archive") Boolean archive, @Param("category") String category);

	

}
