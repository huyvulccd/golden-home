package com.example.timphongtrohanoi.domain.model.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IntrospectResponse {
    private boolean valid;
}
