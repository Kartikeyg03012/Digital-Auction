package com.digital.auction.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestClass {
	public static void main(String args[]) throws ParseException {
		SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
		String dateBeforeString = "31 01 2015";
		String dateAfterString = "02 06 2014";

		try {
			Date dateBefore = myFormat.parse(dateBeforeString);
			Date dateAfter = myFormat.parse(dateAfterString);
			long difference = dateAfter.getTime() - dateBefore.getTime();
			int daysBetween = (int) (difference / (1000 * 60 * 60 * 24));
			/*
			 * You can also convert the milliseconds to days using this method float
			 * daysBetween = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)
			 */
			System.out.println("Number of Days between dates: " + daysBetween);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
