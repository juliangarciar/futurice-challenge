package com.julian.codechallenge.calculator.service;

import org.springframework.stereotype.Service;

import com.julian.codechallenge.calculator.dto.Calculator;

@Service
public class CalculatorServiceImpl implements CalculatorService {
    
    @Override
    public Calculator calculateQuery(String query) {
        Calculator calculator = new Calculator();
        calculator.setError("Hello world");
        calculator.setResult(23);

        return calculator;
    }

}
