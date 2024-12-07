package com.hackathon.uob.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiReponse {

    private Object data;
    private Object metadata;
    private HttpStatusCode code;
    private String message;
}
