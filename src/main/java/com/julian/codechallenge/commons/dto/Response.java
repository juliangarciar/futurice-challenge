package com.julian.codechallenge.commons.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Response {
    
    private Boolean error;
    private String message;
    private Double result;

}
