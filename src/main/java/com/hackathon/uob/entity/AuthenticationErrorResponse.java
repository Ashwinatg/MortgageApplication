package com.hackathon.uob.entity;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public class AuthenticationErrorResponse implements ErrorResponse {
    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatusCode.valueOf(401);
    }

    @Override
    public ProblemDetail getBody() {
        return null;
    }
}
