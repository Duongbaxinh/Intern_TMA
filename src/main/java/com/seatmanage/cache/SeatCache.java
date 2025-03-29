package com.seatmanage.cache;

import com.cloudinary.api.exceptions.BadRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seatmanage.dto.request.DiagramDraft;
import com.seatmanage.dto.request.SaveDiagram;
import com.seatmanage.services.CloudinaryService;
import com.seatmanage.services.RedisService;
import com.seatmanage.services.RoomService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/diagram")
public class SeatCache {
    private final RedisService redisService;
    private final RoomService roomService;

    public SeatCache(RedisService redisService,RoomService roomService) {
        this.redisService = redisService;
        this.roomService = roomService;
    }

    @GetMapping("/{id}")
    public Object getDiagram( @PathVariable String id) {
        System.out.println("id: " + id);
        return  redisService.getValueByKey(id);
    }
    @GetMapping("/all")
    public Object getAllDiagram( ) {
        return  redisService.getAllDiagrams();
    }

    @DeleteMapping("/{room}")
    public void deleteDiagram(@PathVariable String room ) {
         redisService.deleteValueByKey(room);
    }
}
