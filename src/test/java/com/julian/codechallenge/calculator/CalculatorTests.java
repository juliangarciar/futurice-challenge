package com.julian.codechallenge.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.julian.codechallenge.calculator.service.CalculatorService;
import com.julian.codechallenge.commons.UtilsBase64;

@SpringBootTest
class CalculatorTests {
    
    @Autowired
	private CalculatorService calculatorService;

	@Test
	void failTests() {
		String encodedQuery = UtilsBase64.encodeQuery("2 + 2.5 + (5.5");
		Assertions.assertEquals(true, calculatorService.calculateEncodedQuery(encodedQuery).getError());

		encodedQuery = UtilsBase64.encodeQuery("2 + 2.5 + (5.5.3");
		Assertions.assertEquals(true, calculatorService.calculateEncodedQuery(encodedQuery).getError());
		
		encodedQuery = UtilsBase64.encodeQuery("+- (2 * 3.2)");
		Assertions.assertEquals(true, calculatorService.calculateEncodedQuery(encodedQuery).getError());

		encodedQuery = UtilsBase64.encodeQuery("(2 * 3 +)");
		Assertions.assertEquals(true, calculatorService.calculateEncodedQuery(encodedQuery).getError());

		encodedQuery = UtilsBase64.encodeQuery("(3 * 4) +");
		Assertions.assertEquals(true, calculatorService.calculateEncodedQuery(encodedQuery).getError());

		encodedQuery = UtilsBase64.encodeQuery("(2 * (4 + 5)");
		Assertions.assertEquals(true, calculatorService.calculateEncodedQuery(encodedQuery).getError());

		encodedQuery = UtilsBase64.encodeQuery("2 * (4 + 5))");
		Assertions.assertEquals(true, calculatorService.calculateEncodedQuery(encodedQuery).getError());

		encodedQuery = UtilsBase64.encodeQuery("2 ((4 + 5))");
		Assertions.assertEquals(true, calculatorService.calculateEncodedQuery(encodedQuery).getError());

		encodedQuery = UtilsBase64.encodeQuery("2 + (4  5)");
		Assertions.assertEquals(true, calculatorService.calculateEncodedQuery(encodedQuery).getError());
	}

	@Test
	void successTests() {
		String encodedQuery = UtilsBase64.encodeQuery("(2.5 * 2) + 10 + (2 * (5 + 1) + (-2))");
		Assertions.assertEquals(25d, calculatorService.calculateEncodedQuery(encodedQuery).getResult());

		encodedQuery = UtilsBase64.encodeQuery("2 * (23/(3*3))- 23 * (2*3)");
		Assertions.assertEquals(false, calculatorService.calculateEncodedQuery(encodedQuery).getError());

		encodedQuery = UtilsBase64.encodeQuery("-2 * (23/(3*3))- 23 * (2*3)");
		Assertions.assertEquals(false, calculatorService.calculateEncodedQuery(encodedQuery).getError());

		encodedQuery = UtilsBase64.encodeQuery("2 * (36/(3*3))- 23 * (-2*3)");
		Assertions.assertEquals(146, calculatorService.calculateEncodedQuery(encodedQuery).getResult());

		encodedQuery = UtilsBase64.encodeQuery("2 * (-36/(3*3))- 23 * (2*3)");
		Assertions.assertEquals(-146d, calculatorService.calculateEncodedQuery(encodedQuery).getResult());

		encodedQuery = UtilsBase64.encodeQuery("5 - 2 + 4 * (8 - (5 + 1)) + 9");
		Assertions.assertEquals(20d, calculatorService.calculateEncodedQuery(encodedQuery).getResult());
		
		encodedQuery = UtilsBase64.encodeQuery("(8 - 1 + 3) * 6 - ((3 + 7) * 2)");
		Assertions.assertEquals(40d, calculatorService.calculateEncodedQuery(encodedQuery).getResult());

		encodedQuery = UtilsBase64.encodeQuery("-8 -(3-4)");
		Assertions.assertEquals(-7d, calculatorService.calculateEncodedQuery(encodedQuery).getResult());

		encodedQuery = UtilsBase64.encodeQuery("- 8 -( -3*-4 )");
		Assertions.assertEquals(-20d, calculatorService.calculateEncodedQuery(encodedQuery).getResult());
		
		encodedQuery = UtilsBase64.encodeQuery(" -8 - (-3 * -4) ");
		Assertions.assertEquals(-20d, calculatorService.calculateEncodedQuery(encodedQuery).getResult());

		encodedQuery = UtilsBase64.encodeQuery("+1-(-4)");
		Assertions.assertEquals(5d, calculatorService.calculateEncodedQuery(encodedQuery).getResult());

		encodedQuery = UtilsBase64.encodeQuery("(-8 )*(-3 * -4) ");
		Assertions.assertEquals(-96d, calculatorService.calculateEncodedQuery(encodedQuery).getResult());

		encodedQuery = UtilsBase64.encodeQuery("+3-2+4+(-3*(3*-3))-2+5*4/2");
		Assertions.assertEquals(40d, calculatorService.calculateEncodedQuery(encodedQuery).getResult());
	}
	
}
