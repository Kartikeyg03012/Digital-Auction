package com.digital.auction.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.digital.auction.dao.BiddingRepository;
import com.digital.auction.dao.ProductRepository;
import com.digital.auction.entities.BidderModel;
import com.digital.auction.entities.Product;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;

	// save Product
	public Product adduser(Product product) {
		Product save = productRepository.save(product);
		return save;
	}

	// get all Product
	public List<Product> getAllProducts() {
		List<Product> list = productRepository.findAll();
		return list;
	}

	// get product by id and pagination
	public Page<Product> getproductById(int userId, Pageable pageable) {
		Page<Product> list = productRepository.findProductById(userId, pageable);
		return list;
	}

	// get one product detail by id
	public Product getProductDetailById(int id) {
		Optional<Product> optional = productRepository.findById(id);
		Product product = optional.get();
		return product;

	}

	// delete product by id
	public void deleteProductById(Product product) {
		productRepository.delete(product);
	}

	// get wishlist products
	public List<Product> getWishListProducts(Iterable<Integer> ids) {
		List<Product> findAllById = productRepository.findAllById(ids);
		return findAllById;
	}

	// get those product whose archive is false and verified is true
	public List<Product> getAllProductByUnarchiveAndVerify(boolean verify, boolean archive) {
		List<Product> allProductByUnarchiveAndVerify = productRepository.getAllProductByUnarchiveAndVerify(verify,
				archive);
		return allProductByUnarchiveAndVerify;
	}

	// get newest product whose archive is false and verified is true
	public List<Product> getNewestProductByUnarchiveAndVerify(boolean verify, boolean archive) {
		List<Product> newestProductByUnarchiveAndVerify = productRepository.getNewestProductByUnarchiveAndVerify(verify,
				archive);
		return newestProductByUnarchiveAndVerify;
	}

	// get oldest product whose archive is false and verified is true
	public List<Product> getOldestProductByUnarchiveAndVerify(boolean verify, boolean archive) {
		List<Product> list = productRepository.getOldestProductByUnarchiveAndVerify(verify, archive);
		return list;
	}

	// get by category product whose archive is false and verified is true
	public List<Product> getProductByCategoryByUnarchiveAndVerify(boolean verify, boolean archive, String category) {

		List<Product> list = productRepository.getCategoryProductByUnarchiveAndVerify(verify, archive, category);
		return list;
	}

	public Product getBiddingProducts(int id) {
		Optional<Product> findById = productRepository.findById(id);
		return findById.get();
	}
}
