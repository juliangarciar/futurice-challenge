package com.julian.codechallenge.calculator.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.julian.codechallenge.commons.UtilsBase64;
import com.julian.codechallenge.commons.UtilsRegex;
import com.julian.codechallenge.commons.dto.Response;

@Service
public class CalculatorServiceImpl implements CalculatorService {

	private static final String ENCODED_ERROR = "Calculator - The query is not properly encoded in base64.";
	private static final String INVALID_QUERY_ERROR = "Calculator - The query is not valid.";
	private static final String VALID_QUERY_INFO = "Calculator - The query is valid.";
	
    @Override
    public Response calculateEncodedQuery(String encodedQuery) {
        Response response = new Response();

		Optional<String> decodedQuery = UtilsBase64.decodeQuery(encodedQuery);
		if (!decodedQuery.isPresent()) {
			response.setError(true);
            response.setMessage(ENCODED_ERROR);
			return response;
		}

		Optional<String> result = UtilsRegex.resolveQueryExpression(decodedQuery.get());
		if (result.isPresent()) {
			response.setError(false);
			response.setMessage(VALID_QUERY_INFO);
			response.setResult(Double.parseDouble(result.get()));
		} else {
			response.setError(true);
			response.setMessage(INVALID_QUERY_ERROR);
		}
		
        return response;
    }

}
