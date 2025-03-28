package com.seatmanage.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@FieldDefaults(level = AccessLevel.PUBLIC)
public class SaveDiagram {
    String roomId;
    MultipartFile image;
    String seats;
    String object;
}
