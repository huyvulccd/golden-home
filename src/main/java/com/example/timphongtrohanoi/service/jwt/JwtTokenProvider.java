package com.example.timphongtrohanoi.service.jwt;

import static com.example.timphongtrohanoi.common.DateTimeUtil.quantityMillisecondInHour;

public class JwtTokenProvider {
    private final String secretKey = "yourSecretKey";

    private final long timeValid = quantityMillisecondInHour;
}
