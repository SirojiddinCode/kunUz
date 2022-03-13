package com.company.controller;

import com.company.dto.RegionDto;
import com.company.enums.ProfileRole;
import com.company.service.RegionService;
import com.company.util.JwtUtil;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static org.apache.logging.log4j.util.LambdaUtil.getAll;

@RestController
@RequestMapping("/Region")
@Api(tags = "Region")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @PostMapping("/create")
    @ApiOperation(nickname ="create" ,value ="Region create method")
    @ApiResponse(code = 200,message = "Successful",response = RegionDto.class)
    public ResponseEntity<RegionDto> create(@RequestBody RegionDto dto, HttpServletRequest request) {
        JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        RegionDto result = regionService.create(dto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update/{id}")
    @ApiOperation(nickname = "Update",value = "Region update method")
    @ApiResponse(code = 200,message = "Successful",response = String.class)
    public ResponseEntity<String> update(@RequestBody RegionDto dto,
                                    @ApiParam(name="Id",value = "Id of region",required = true)
                                    @PathVariable("id") Integer id,
                                    HttpServletRequest request) {
        JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        regionService.update(id, dto);
        return ResponseEntity.ok("updated");
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(nickname ="Delete" ,value = "Region delete method")
    @ApiResponse(code = 200,message = "Successful",response = String.class)
    public ResponseEntity<String> delete(@ApiParam(name="Id",value = "Id of region",required = true)
                                        @PathVariable("id") Integer id, HttpServletRequest request) {
        JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        regionService.delete(id);
        return ResponseEntity.ok("deleted");
    }

    @GetMapping("/{id}")
    @ApiOperation(nickname ="getById" ,value = "get region method")
    @ApiResponse(code = 200,message = "Successful",response = RegionDto.class)
    public ResponseEntity<RegionDto> getById(@ApiParam(name="Id",value = "Id of region",required = true)
                                                 @PathVariable("id") Integer id) {
        RegionDto dto = regionService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/all")
    @ApiOperation(value = "get all regions method")
    @ApiResponse(code = 200,message = "Successful",response = RegionDto.class,responseContainer = "List")
    public ResponseEntity<List<RegionDto>> getall() {
        List<RegionDto> result = regionService.getAll();
        return ResponseEntity.ok(result);
    }

}
