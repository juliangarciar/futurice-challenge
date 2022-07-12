package com.julian.codechallenge.regexcalculator.service;

import com.julian.codechallenge.commons.dto.Response;

public interface RegexCalculatorService {

    public Response calculateEncodedQuery(byte[] encodedQuery);

    public byte[] encodeQuery(String query);

}
