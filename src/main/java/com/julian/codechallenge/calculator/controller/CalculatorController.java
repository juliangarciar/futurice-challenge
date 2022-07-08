package com.julian.codechallenge.calculator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.julian.codechallenge.calculator.dto.Calculator;
import com.julian.codechallenge.calculator.service.CalculatorService;

@RestController
@RequestMapping("/calculus")
public class CalculatorController {

    @Autowired
    private CalculatorService calculatorService;
    
    @GetMapping
    public ResponseEntity<Calculator> calculateQuery(@RequestParam("query") String query) {
        Calculator response = calculatorService.calculateQuery(query);
        return ResponseEntity.ok().body(response);
    }

}
