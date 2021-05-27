package com.digital.auction.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.digital.auction.dao.AdminRepository;
import com.digital.auction.dao.ProductRepository;
import com.digital.auction.dao.UserRepository;
import com.digital.auction.entities.Product;
import com.digital.auction.entities.User;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;

	// get users by id and pagination
	public Page<User> getAllUsers(Pageable pageable) {
		Page<User> list = adminRepository.findAllUsers(pageable);
		return list;
	}

	// get product by id and pagination
	public Page<Product> getAllProducts(Pageable pageable) {
		Page<Product> list = adminRepository.findAllProducts(pageable);
		return list;
	}

	// Sort Users on the basis of verification status
	public Page<User> getUserByVerification(Boolean status, Pageable pageable) {
		return adminRepository.getUserByVerification(status, pageable);
	}

	// Sort Users on the basis of request status with pagination
	public Page<User> getUserByRequestSeller(String status, Pageable pageable) {
		return adminRepository.getUserByRequestForSeller(status, pageable);
	}

	// Sort Users on the basis of ROLE with pagination
	public Page<User> getUserByRole(String role, Pageable pageable) {
		return adminRepository.getUserByRole(role, pageable);
	}

	// Sort Users on the basis of Archive Status with pagination
	public Page<User> getUserByArchive(Boolean archive, Pageable pageable) {
		return adminRepository.getUserByArchive(archive, pageable);
	}

	// get products randomly...
	public Page<Product> getProductsRandomly(boolean status, boolean archive, Pageable pageable) {
		return adminRepository.findAllProductsRandomly(status, archive, pageable);
	}

	// sorting on product

	public Page<Product> getProductByArchiveSorting(boolean archive, Pageable pageable) {
		return adminRepository.getProductByArchiveSorting(archive, pageable);
	}

	public Page<Product> getProductByVerifySorting(boolean archive, Pageable pageable) {
		return adminRepository.getProductByVerifySorting(archive, pageable);
	}

	public Page<Product> getProductByStatusSorting(String status, Pageable pageable) {
		return adminRepository.getProductByStatusSorting(status, pageable);
	}
}
