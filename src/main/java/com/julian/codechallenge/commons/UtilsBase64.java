package com.julian.codechallenge.commons;

import java.util.Base64;
import java.util.Optional;

public class UtilsBase64 {

    private UtilsBase64() {}
    
    public static String encodeQuery(String query) {
        return Base64.getEncoder().encodeToString(query.getBytes());
    }

	public static Optional<String> decodeQuery(String encodedQuery) {
		try {
			byte[] decodedBytes = Base64.getDecoder().decode(encodedQuery);
			String decodedQuery = new String(decodedBytes);
			return Optional.of(decodedQuery);
		} catch (IllegalArgumentException exception) {
			return Optional.empty();
		}
	}

}
