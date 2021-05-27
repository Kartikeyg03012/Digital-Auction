package com.digital.auction.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.digital.auction.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	// get data by email
	@Query("select u from User u WHERE u.email=:email")
	public User getUsernameByEmail(@Param("email") String email);

	// get data by id
	@Query("select u from User u WHERE u.id=:id")
	public User getUsernameById(@Param("id") int id);

	// get data ny token
	@Query("select u from User u WHERE u.token=:token ")
	public User getUsernameByToken(@Param("token") String token);

}
