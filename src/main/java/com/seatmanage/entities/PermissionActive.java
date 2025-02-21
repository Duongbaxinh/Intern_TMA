package com.seatmanage.entities;

import com.seatmanage.config.SecurityUtil;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionActive {
    @Id
    @Column(unique = true, nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    SecurityUtil.PermissionAuth name;
    String description;
}
