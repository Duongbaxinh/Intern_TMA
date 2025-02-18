package com.seatmanage.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
public class UserRequest {
    String firstName;
    String lastName;
    String username;
    String roleId;
    @Size(min = 8, message = "UNVALIDATED_PASS")
    String password;
}
