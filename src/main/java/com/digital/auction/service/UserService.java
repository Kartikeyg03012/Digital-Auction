package com.digital.auction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digital.auction.dao.UserRepository;
import com.digital.auction.entities.User;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;

	// SAVE Or UPDATE User
	public User adduser(User user) {
		User save = userRepo.save(user);
		return save;
	}

	// Get User Data By Email
	public User getUserDataByEmail(String email) {
		User usernameByEmail = userRepo.getUsernameByEmail(email);
		return usernameByEmail;

	}

	// Get User Data By Token
	public User getUserDataByToken(String token) {
		User usernameByEmail = userRepo.getUsernameByToken(token);
		return usernameByEmail;

	}

	// Get User Data By Id
	public User getUserById(int id) {
		User user = userRepo.getUsernameById(id);
		return user;
	}

}
