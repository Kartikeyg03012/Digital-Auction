package com.digital.auction.security;

import java.nio.file.attribute.UserPrincipalNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.digital.auction.dao.UserRepository;
import com.digital.auction.entities.User;

public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.getUsernameByEmail(username);

		if (user == null || !user.isVerifiedStatus() || user.isArchive()) {
			throw new UsernameNotFoundException("User Not Found");
		}
		UserDetails userDetails = new UserDetailsImpl(user);
		return userDetails;
	}

}
