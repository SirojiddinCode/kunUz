package com.company.service;

import com.company.dto.RegionDto;
import com.company.entity.RegionEntity;
import com.company.exceptions.BadRequestException;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;

    public RegionDto create(RegionDto dto) {
        if (dto.getName() == null && dto.getName().isEmpty()) {
            throw new BadRequestException("Region name can not be null");
        }
        RegionEntity entity = new RegionEntity();
        entity.setName(dto.getName());
        regionRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public void update(Integer id, RegionDto dto) {
        if (dto.getName() == null && dto.getName().isEmpty()) {
            throw new BadRequestException("Region name can not be null");
        }
        if (regionRepository.existsById(id)) {
            RegionEntity entity = new RegionEntity();
            entity.setName(dto.getName());
            entity.setId(id);
            regionRepository.save(entity);
        }
        throw new ItemNotFoundException("Region not found");
    }

    public void delete(Integer id) {
        if (regionRepository.existsById(id)) {
            regionRepository.deleteById(id);
        }
        throw new ItemNotFoundException("Region not Found");
    }

    public RegionEntity get(Integer id) {
        Optional<RegionEntity> op = regionRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        }else{
            throw new ItemNotFoundException("Region not found");
        }
    }
    public RegionDto toDto(RegionEntity entity){
        RegionDto dto=new RegionDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
    public RegionDto getById(Integer id){
        return toDto(get(id));
    }
    public List<RegionDto> getAll(){
        List<RegionEntity> entityList=regionRepository.findAll();
        List<RegionDto> dtoList=new LinkedList<>();
        entityList.forEach(r->dtoList.add(toDto(r)));
        return dtoList;
    }

}
