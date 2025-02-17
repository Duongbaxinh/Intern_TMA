package com.seatmanage.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthDTO {
    private int id;
    private String username;
    private String accessToken;
}
