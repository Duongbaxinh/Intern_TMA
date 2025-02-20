package com.seatmanage.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserPrivateDTO {
    String username;
    List<String> permissions;
}
