package com.digital.auction.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.digital.auction.dao.BiddingRepository;

@Controller
public class TestController {

	@GetMapping("/load-form")
	public String showForm() {
		return "testfileupload";
	}

	@Autowired
	private BiddingRepository biddingRepository;

	@PostMapping("/testing-addimage")
	public String addImage(@RequestParam("image") MultipartFile file) {

		try {
			File savefile = new ClassPathResource("static/img").getFile();

			Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Image Uploaded SuccessFully");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "login";
	}

	@GetMapping("/test-bidding-data")
	@ResponseBody
	public String ok() {
	
		return "okok";
	}

}
