package com.seatmanage.services;

import com.seatmanage.dto.response.FloorDTO;
import com.seatmanage.dto.request.FloorRequest;
import com.seatmanage.entities.Floor;
import com.seatmanage.mappers.FloorMapper;
import com.seatmanage.repositories.FloorRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PUBLIC)
public class FloorService {

    @Autowired
    FloorRepository floorRepository;
    @Autowired
    FloorMapper floorMapper;

    public Floor createFloor(FloorRequest floor) {
        Floor floorExisted = getFloorByName(floor.getName());
        if (floorExisted != null) throw new RuntimeException("Floor already exist");
        Floor newFloor = floorMapper.toFloor(floor);
        return floorRepository.save(newFloor);
    }

    public List<FloorDTO> getAll() {
        List<Floor> floors = floorRepository.findAll();
        return floors.stream()
                .map(floor -> floorMapper.toFloorDTO(floor))
                .collect(Collectors.toList());
    }

    public Floor getFloorByName(String floorName) {
       return floorRepository.getFloorByFloorName(floorName).orElse(null);
    }

    public FloorDTO getFloorById(String floorId) {
        Floor floor =  floorRepository.findById(floorId).orElseThrow(()->new RuntimeException("Floor not Found !"));
        return floorMapper.toFloorDTO(floor);
    }

    public Floor getFloorByIdDefault(String floorId) {
        return  floorRepository.findById(floorId).orElse(null);
    }

    public FloorDTO updateFloor(String floorId, FloorRequest floorUpdate) {
        Floor floor = getFloorByIdDefault(floorId);
        Optional.ofNullable(floorUpdate.getName()).ifPresent(floor::setName);
        Optional.ofNullable(floorUpdate.getDescription()).ifPresent(floor::setDescription);
        return  floorMapper.toFloorDTO(floorRepository.save(floor));
    }

    public FloorDTO deleteFloor(String floorId){
        FloorDTO floor = getFloorById(floorId);
        floorRepository.deleteById(floorId);
        return floor;
    }

}
