package com.seatmanage.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
public class UserRequest {
    String firstName;
    String lastName;
    @NotNull(message = "USERNAME_NOT_NULL")
    String username;
    String roleName;
    @Size(min = 8, message = "UNVALIDATED_PASS")
    @NotNull(message = "PASSWORD_NOT_NULL")
    String password;
}
