package com.digital.auction.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.digital.auction.dao.ContactRepository;
import com.digital.auction.entities.ContactUsModel;
import com.digital.auction.entities.Product;
import com.digital.auction.entities.User;

@Service
public class ContactUsService {

	@Autowired
	private ContactRepository contactRepository;

	// save details
	public ContactUsModel addQuery(ContactUsModel contactUsModel) {
		return contactRepository.save(contactUsModel);
	}

	// get All queries...
	public Page<ContactUsModel> getAllQueriesOfCustomer(Pageable pageable) {
		Page<ContactUsModel> list = contactRepository.findAllQuery(pageable);
		return list;
	}

	// get Query detail by id
	public ContactUsModel getQueryDetailById(int id) {
		Optional<ContactUsModel> optional = contactRepository.findById(id);
		ContactUsModel contactUsModel = optional.get();
		return contactUsModel;
	}

	// Sort Query detail by status
	public Page<ContactUsModel> sortQueryByStatus(String status,Pageable pageable) {
	
		return contactRepository.getContactUsModelByStatus(status, pageable);
	}

}
