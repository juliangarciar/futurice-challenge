package com.julian.codechallenge.calculator.controller;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.julian.codechallenge.calculator.service.CalculatorService;
import com.julian.codechallenge.commons.UtilsBase64;
import com.julian.codechallenge.commons.dto.Response;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

@RestController
@RequestMapping("/calculus")
public class CalculatorController {

    @Autowired
    private CalculatorService regexCalculatorService;

    private final Bucket bucket;

    public CalculatorController() {
        Bandwidth limit = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder()
            .addLimit(limit)
            .build();
    }

    @GetMapping
    public ResponseEntity<Response> calculateEncodedQuery(@RequestParam("query") String encodedQuery) {
        if (bucket.tryConsume(1)) {
            return ResponseEntity.ok(regexCalculatorService.calculateEncodedQuery(encodedQuery));
        }
    
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @GetMapping("/encode")
    public ResponseEntity<String> encodeQuery(@RequestParam("query") String query) {
        if (bucket.tryConsume(1)) {
            return ResponseEntity.ok(UtilsBase64.encodeQuery(query));
        }
    
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

}
