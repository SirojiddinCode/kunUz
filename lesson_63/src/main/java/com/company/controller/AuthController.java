package com.company.controller;

import com.company.dto.profile.ProfileDTO;
import com.company.dto.auth.AuthorizationDTO;
import com.company.dto.auth.RegistrationDto;
import com.company.service.AuthService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/auth")
@Api(tags = "Auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @ApiOperation(value = "login method",notes = "sekinro ishlami qolishi mumkin",nickname = "nickname")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "succesful",response = ProfileDTO.class)
    })
    public ResponseEntity<ProfileDTO> authorization(@Valid @RequestBody AuthorizationDTO dto) {
        ProfileDTO response = authService.authorization(dto);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/registration")
    @ApiOperation(value = "Registration method")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "succesful",response = String.class)
    })
    public ResponseEntity<String> registration(@Valid @RequestBody RegistrationDto dto){
        authService.registration(dto);
        return ResponseEntity.ok("registrated");
    }
    @GetMapping("/verification/{id}")
    @ApiOperation(value = "Verivication method")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "succesful",response = String.class)
    })
    public ResponseEntity<String> verification(@Valid@ApiParam(name = "jwt tooken") @PathVariable("id") String jwt){
        authService.verification(jwt);
        return ResponseEntity.ok("Your status Active");
    }

}
