package com.company.controller;

import com.company.dto.auth.RegistrationDto;
import com.company.dto.profile.ProfileDTO;
import com.company.dto.profile.ProfileFilterDto;
import com.company.dto.profile.ProfileJwtDTO;
import com.company.enums.ProfileRole;
import com.company.service.ProfileService;
import com.company.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/profile")
@Api(tags = "Profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("/createByAdmin")
    @ApiOperation(value = "create a profile for admin")
    @ApiResponse(code = 200,message = "Successful",response = ProfileDTO.class)
    public ResponseEntity<ProfileDTO> createProfile(@Valid @RequestBody ProfileDTO dto, HttpServletRequest request) {
        ProfileJwtDTO admin=JwtUtil.getProfile(request,ProfileRole.ADMIN_ROLE);
        ProfileDTO response = profileService.createProfileAdmin(dto, admin.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @ApiOperation(value = "get all profile")
    @ApiResponse(code = 200,message = "Successful",response = ProfileDTO.class,responseContainer = "List")
    public ResponseEntity<List<ProfileDTO>> getAll() {
        List<ProfileDTO> dtoList = profileService.getAll();
        return ResponseEntity.ok(dtoList) ;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "get profile method")
    @ApiResponse(code = 200,message = "Successful",response = ProfileDTO.class)
    public ResponseEntity<?> getById(@ApiParam(value = "Id of Profile",required = true) @PathVariable("id") Integer id) {
        ProfileDTO dto = profileService.getById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.badRequest().body("Profile not found");
    }

    @PutMapping("/update")
    @ApiOperation(value = "update for admin mathod")
    @ApiResponse(code = 200,message = "Successful",response = ProfileDTO.class)
    public ResponseEntity<?> update(@Valid @RequestBody ProfileDTO dto,
                                    HttpServletRequest request) {
        ProfileJwtDTO admin=JwtUtil.getProfile(request,ProfileRole.ADMIN_ROLE);
        boolean result = profileService.update(admin.getId(), dto);
        return result ? ResponseEntity.ok(dto) : ResponseEntity.badRequest().body("Not update");
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "delete profile for Admin")
    @ApiResponse(code = 200,message = "Successful",response = String.class,responseContainer = "List")
    public ResponseEntity<String> delete(@ApiParam(value = "Id of profile")@PathVariable("id") Integer id,
                                    HttpServletRequest request) {
        JwtUtil.getProfile(request,ProfileRole.ADMIN_ROLE);
        boolean result = profileService.delete(id);
        return result ? ResponseEntity.ok("deleted") : ResponseEntity.badRequest().body("Not delete");
    }
    @GetMapping("/filter")
    @ApiOperation(value = "filter method")
    @ApiResponse(code = 200,message = "Successful",response = ProfileDTO.class,responseContainer = "PageImpl")
    public ResponseEntity<PageImpl<ProfileDTO>> filter(@RequestBody ProfileFilterDto filterDto,
                                                       @ApiParam(required = true)@RequestParam("page")int page,
                                                       @ApiParam(required = true)@RequestParam("size") int size){

        PageImpl<ProfileDTO> dtoPage=profileService.filter(page,size,filterDto);
        return ResponseEntity.ok(dtoPage);
    }

    // registration
    // authorization

}
