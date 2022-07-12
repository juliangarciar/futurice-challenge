package com.julian.codechallenge.regexcalculator.controller;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.julian.codechallenge.commons.dto.Response;
import com.julian.codechallenge.regexcalculator.service.RegexCalculatorService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

@RestController
@RequestMapping("/regex-calculator")
public class RegexCalculatorController {

    @Autowired
    private RegexCalculatorService regexCalculatorService;

    private final Bucket bucket;

    public RegexCalculatorController() {
        Bandwidth limit = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder()
            .addLimit(limit)
            .build();
    }

    @GetMapping("/hello")
    public ResponseEntity<String> calculateEncodedQuery() {
        return ResponseEntity.ok("Hello World!");
    }
    
    @GetMapping("/calculus")
    public ResponseEntity<Response> calculateEncodedQuery(@RequestParam("query") byte[] encodedQuery) {
        if (bucket.tryConsume(1)) {
            return ResponseEntity.ok(regexCalculatorService.calculateEncodedQuery(encodedQuery));
        }
    
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @GetMapping("/encode")
    public ResponseEntity<byte[]> encodeQuery(@RequestParam("query") String query) {
        if (bucket.tryConsume(1)) {
            return ResponseEntity.ok(regexCalculatorService.encodeQuery(query));
        }
    
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

}
