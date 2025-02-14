package com.seatmanage.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
public class UserRequest {
    String firstName;
    String lastName;
    @Size(min = 8, message = "UNVALIDATED_PASS")
    String password;
}
