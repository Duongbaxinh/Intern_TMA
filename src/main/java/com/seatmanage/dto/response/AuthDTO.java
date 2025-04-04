package com.seatmanage.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthDTO {
    private String id;
    private String username;
    private String role;
    private String room;
    private String accessToken;
}
