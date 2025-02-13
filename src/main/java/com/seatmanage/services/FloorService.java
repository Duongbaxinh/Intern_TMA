package com.seatmanage.services;

import com.seatmanage.dto.request.FloorCreationRequest;
import com.seatmanage.entities.Floor;
import com.seatmanage.mappers.FloorMapper;
import com.seatmanage.repositories.FloorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloorService {

    @Autowired
    FloorRepository floorRepository;
    @Autowired
    FloorMapper floorMapper;

    public Floor createFloor(FloorCreationRequest floor) {
        Floor floorExisted = getFloorByName(floor.getName());
        if (floorExisted != null) throw new RuntimeException("Floor already exist");
        Floor newFloor = floorMapper.toFloor(floor);
        return floorRepository.save(newFloor);
    }

    List<Floor> getAll(){
        return floorRepository.findAll();
    }

    Floor getFloorByName(String floorName) {
       return floorRepository.getFloorByFloorName(floor.getName()).orElse(null);
    }

    Floor getFloorById(String floorId) {
        return floorRepository.findById(floorId).orElse(null);
    }

    Floor updateFloor(String floorId){
        Floor floor = getFloorById(floorId);
        if(floor == null) throw new RuntimeException("Floor Not Found");

    }

}
