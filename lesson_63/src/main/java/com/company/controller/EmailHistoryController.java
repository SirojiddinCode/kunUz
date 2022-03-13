package com.company.controller;

import com.company.dto.EmailHistoryDTO;
import com.company.dto.comment.CommentDto;
import com.company.enums.ProfileRole;
import com.company.service.EmailService;
import com.company.util.JwtUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.parser.TagElement;
import java.util.List;

@RestController
@RequestMapping("/emailhistory")
@Api(tags = "Email History")
public class EmailHistoryController {
    @Autowired
    private EmailService emailService;

    @GetMapping("/all")
    @ApiOperation(value = "get all history for Admin method")
    @ApiResponse(code = 200,message = "Successful",response = EmailHistoryDTO.class, responseContainer = "PageImpl")
    public ResponseEntity<PageImpl<EmailHistoryDTO>> getAll(@ApiParam(required = true)@RequestParam("size") Integer size,
                                    @ApiParam(required = true)@RequestParam("page") Integer page,
                                    HttpServletRequest request) {
        JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        PageImpl<EmailHistoryDTO> dtoList = emailService.getAll(page, size);
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/today")
    @ApiOperation(value = "get today history for Admin method")
    @ApiResponse(code = 200,message = "Successful",response = EmailHistoryDTO.class, responseContainer = "List")
    public ResponseEntity<List<EmailHistoryDTO>> getToday(HttpServletRequest request) {
        JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        List<EmailHistoryDTO> dtoList = emailService.getToday();
        return ResponseEntity.ok(dtoList);

    }

    @GetMapping("/last")
    @ApiOperation(value = "get last history for Admin method")
    @ApiResponse(code = 200,message = "Successful",response = EmailHistoryDTO.class)
    public ResponseEntity<EmailHistoryDTO> getLast(HttpServletRequest request) {
        JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        EmailHistoryDTO dto = emailService.getLast();
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/not_used")
    @ApiOperation(value = "get not used history for Admin method")
    @ApiResponse(code = 200,message = "Successful",response = EmailHistoryDTO.class, responseContainer = "PageImpl")
    public ResponseEntity<PageImpl<EmailHistoryDTO>> getNotUsed(@RequestParam("size")Integer size,@RequestParam("page")Integer page,
                                        HttpServletRequest request){
        JwtUtil.getProfile(request,ProfileRole.ADMIN_ROLE);
        PageImpl<EmailHistoryDTO> dtoPage=emailService.getbyStatus(page,size);
          return ResponseEntity.ok(dtoPage);
    }
}
