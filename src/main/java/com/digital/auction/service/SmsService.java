package com.digital.auction.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.SplittableRandom;

import javax.net.ssl.HttpsURLConnection;

@Service
public class SmsService {

	// Genrate random OTP....
	public static String genrateOtp(int length) {
		SplittableRandom sr = new SplittableRandom();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(sr.nextInt(0, 10));

		}
		return sb.toString();
	}

	// Sending SMS On mobile
	public static void sendSms(String message, String number) {
//			System.out.println(message);
//			System.out.println(number);
		try {

			String apiKey = "4GSrFblkE8xdjToAnUf29ILqWKm17tNRVp6J3ihQMePOywvaHzsyoerYKvRg0zp6FEW9aLUH8fx5dlq2";
			String sendId = "FSTSMS";
			// important step...
			message = URLEncoder.encode(message, "UTF-8");
			String language = "english";

			String route = "p";

			String myUrl = "https://www.fast2sms.com/dev/bulk?authorization=" + apiKey + "&sender_id=" + sendId
					+ "&message=" + message + "&language=" + language + "&route=" + route + "&numbers=" + number;

			// sending get request using java..

			URL url = new URL(myUrl);

			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

			con.setRequestMethod("GET");

			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("cache-control", "no-cache");
			System.out.println("Wait..............");

			int code = con.getResponseCode();

			System.out.println("Response code : " + code);

			StringBuffer response = new StringBuffer();

			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				response.append(line);
			}

			System.out.println(response);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	
	
	//only for testing...
	/*
	 * public static void main(String[] args) { SmsService.sendSms(
	 * "Hello User, \nWelcome To Digital Auction Services. Your OTP(One Time password) is: "
	 * + SmsService.genrateOtp(6) +
	 * ". Thanks For Connecting With Us, \nRegards- Digital Auction!!!",
	 * "7566437956"); }
	 */

}
