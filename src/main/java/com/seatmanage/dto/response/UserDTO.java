package com.seatmanage.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seatmanage.entities.Team;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PUBLIC)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    String id;
    String username;
    String firstName;
    String lastName;
    String role;
    Team team;
}
