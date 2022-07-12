package com.julian.codechallenge.regexcalculator;

import java.util.Base64;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.julian.codechallenge.regexcalculator.service.RegexCalculatorService;

@SpringBootTest
public class RegexCalculatorTests {
    
    @Autowired
	private RegexCalculatorService calculatorService;

	@Test
	public void failTests() {
		byte[] encodedBytes = generateBytes("2 + 2.5 + (5.5");
		Assertions.assertEquals(true, calculatorService.calculateEncodedQuery(encodedBytes).getError());

		encodedBytes = generateBytes("2 + 2.5 + (5.5.3");
		Assertions.assertEquals(true, calculatorService.calculateEncodedQuery(encodedBytes).getError());
		
		encodedBytes = generateBytes("+ (2 * 3.2)");
		Assertions.assertEquals(true, calculatorService.calculateEncodedQuery(encodedBytes).getError());

		encodedBytes = generateBytes("(2 * 3 +)");
		Assertions.assertEquals(true, calculatorService.calculateEncodedQuery(encodedBytes).getError());

		encodedBytes = generateBytes("(3 * 4) +");
		Assertions.assertEquals(true, calculatorService.calculateEncodedQuery(encodedBytes).getError());

		encodedBytes = generateBytes("(2 * (4 + 5)");
		Assertions.assertEquals(true, calculatorService.calculateEncodedQuery(encodedBytes).getError());

		encodedBytes = generateBytes("2 * (4 + 5))");
		Assertions.assertEquals(true, calculatorService.calculateEncodedQuery(encodedBytes).getError());
	}

	@Test
	public void successTests() {
		byte[] encodedBytes = generateBytes("(2.5 * 2) + 10 + (2 * (5 + 1) + (-2))");
		Assertions.assertEquals(25d, calculatorService.calculateEncodedQuery(encodedBytes).getResult());

		encodedBytes = generateBytes("2 * (23/(3*3))- 23 * (2*3)");
		Assertions.assertEquals(false, calculatorService.calculateEncodedQuery(encodedBytes).getError());

		encodedBytes = generateBytes("-2 * (23/(3*3))- 23 * (2*3)");
		Assertions.assertEquals(false, calculatorService.calculateEncodedQuery(encodedBytes).getError());

		encodedBytes = generateBytes("2 * (36/(3*3))- 23 * (-2*3)");
		Assertions.assertEquals(146, calculatorService.calculateEncodedQuery(encodedBytes).getResult());

		encodedBytes = generateBytes("2 * (-36/(3*3))- 23 * (2*3)");
		Assertions.assertEquals(-146d, calculatorService.calculateEncodedQuery(encodedBytes).getResult());

		encodedBytes = generateBytes("5 - 2 + 4 * (8 - (5 + 1)) + 9");
		Assertions.assertEquals(20d, calculatorService.calculateEncodedQuery(encodedBytes).getResult());
		
		encodedBytes = generateBytes("(8 - 1 + 3) * 6 - ((3 + 7) * 2)");
		Assertions.assertEquals(40d, calculatorService.calculateEncodedQuery(encodedBytes).getResult());
	}
	
	private static byte[] generateBytes(String query) {
		return Base64.getEncoder().encode(query.getBytes());
	}

}
