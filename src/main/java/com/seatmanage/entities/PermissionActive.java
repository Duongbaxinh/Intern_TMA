package com.seatmanage.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionActive {
    @Id
    @Column(unique = true, nullable = false)
    String name;
    String description;
}
