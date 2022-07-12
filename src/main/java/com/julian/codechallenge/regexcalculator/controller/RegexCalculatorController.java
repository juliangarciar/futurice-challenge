package com.julian.codechallenge.regexcalculator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.julian.codechallenge.commons.dto.Response;
import com.julian.codechallenge.regexcalculator.service.RegexCalculatorService;

@RestController
@RequestMapping("/calculus")
public class RegexCalculatorController {

    @Autowired
    private RegexCalculatorService regexCalculatorService;
    
    @GetMapping
    public ResponseEntity<Response> calculateEncodedQuery(@RequestParam("query") byte[] encodedQuery) {
        Response response = regexCalculatorService.calculateEncodedQuery(encodedQuery);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/encode")
    public ResponseEntity<byte[]> encodeQuery(@RequestParam("query") String query) {
        byte[] response = regexCalculatorService.encodeQuery(query);
        return ResponseEntity.ok().body(response);
    }

}
