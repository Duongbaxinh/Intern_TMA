package com.seatmanage.services;

import com.seatmanage.dto.request.HallRequest;
import com.seatmanage.dto.response.HallDTO;
import com.seatmanage.entities.Floor;
import com.seatmanage.entities.Hall;
import com.seatmanage.mappers.HallMapper;
import com.seatmanage.repositories.FloorRepository;
import com.seatmanage.repositories.HallRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PUBLIC)
public class HallService {

    @Autowired
    HallRepository hallRepository;
    @Autowired
    HallMapper hallMapper;
    @Autowired
    FloorService floorService;
    @Autowired
    FloorRepository floorRepository;

    public HallDTO getHallById(String hallId) {
        Hall hall = hallRepository.findById(hallId).orElseThrow(()->new RuntimeException("Hall not found"));
        return hallMapper.toHallDTO(hall);
    }

    public Hall getHallByIdDefault(String hallId) {
        return hallRepository.findById(hallId).orElseThrow(()->new RuntimeException("Hall not found"));
    }

    public HallDTO createHall(HallRequest hallRequest) {

        Floor floorExisted = floorService.getFloorByIdDefault(hallRequest.getFloorId());
        if (floorExisted == null) throw new RuntimeException("Floor Not Found");
        Hall hallExisted =
                hallRepository.findByHallNamedAndFloorId(hallRequest.getName(),floorExisted.getId()).orElse(null);
        if (hallExisted != null) throw new RuntimeException("Hall already exist");
        Hall hall = new Hall();
        Optional.ofNullable(hallRequest.getFloorId())
                .map(floorService::getFloorByIdDefault)
                .ifPresent(hall::setFloor);
        hall.setName(hallRequest.getName());
        hall.setName(hallRequest.getName());
        return hallMapper.toHallDTO(hallRepository.save(hall));
    }

    public List<HallDTO> getAll(){
         List<Hall> halls = hallRepository.findAll();
        return halls.stream()
                .map(hall -> hallMapper.toHallDTO(hall))
                .collect(Collectors.toList());
    }

    public HallDTO updateHall(String hallId, HallRequest hallUpdate) {
        Hall hall = getHallByIdDefault(hallId);
        Floor floorExisted =
                Optional.ofNullable(hallUpdate.getFloorId())
                        .map(floorService::getFloorByIdDefault)
                        .orElseThrow(()-> new RuntimeException("Floor Not Found"));
        Optional.ofNullable(hallUpdate.getName()).ifPresent(hall::setName);
        Optional.ofNullable(hallUpdate.getDescription()).ifPresent(hall::setDescription);
        hall.setFloor(floorExisted);
        return hallMapper.toHallDTO(hallRepository.save(hall));
    }

    public HallDTO deleteHall(String hallId){
        HallDTO hall = getHallById(hallId);
        hallRepository.deleteById(hallId);
        return hall;
    }

}
