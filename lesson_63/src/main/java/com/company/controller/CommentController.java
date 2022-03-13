package com.company.controller;

import com.company.dto.comment.CommentDto;
import com.company.dto.comment.CommentFilterDto;
import com.company.dto.profile.ProfileJwtDTO;
import com.company.service.CommentService;
import com.company.util.JwtUtil;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/comment")
@Api(tags = "Comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    @ApiOperation(value = "Comment create method")
    @ApiResponse(code = 200,message = "Succesful",response = CommentDto.class)
    public ResponseEntity<CommentDto> create(@Valid @RequestBody CommentDto dto,
                                    HttpServletRequest request) {
        ProfileJwtDTO profile = JwtUtil.getProfile(request);
        CommentDto response = commentService.create(dto, profile.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "get comment method")
    @ApiResponse(code = 200,message = "Succesful",response = CommentDto.class)
    public ResponseEntity<CommentDto> getById(@ApiParam(value = "Id of Comment",required = true)
                                                  @PathVariable("id") Integer id) {
        CommentDto dto = commentService.get(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "Update comment method")
    @ApiResponse(code = 200,message = "Succesful",response = String.class)
    public ResponseEntity<String> update(@Valid @RequestBody CommentDto dto,
                                         @ApiParam(value = "Id of Comment",required = true)
                                         @PathVariable("id") Integer commentId,
                                         HttpServletRequest request) {
        ProfileJwtDTO profile = JwtUtil.getProfile(request);
        commentService.update(dto, profile.getId(), commentId);
        return ResponseEntity.ok("Updated");
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete comment method")
    @ApiResponse(code = 200,message = "Succesful",response = String.class)
    public ResponseEntity<String> delete(@ApiParam(value = "Id of Comment",required = true)@PathVariable("id") Integer commentId,
                                    HttpServletRequest request) {
        ProfileJwtDTO profile = JwtUtil.getProfile(request);
        commentService.delete(commentId, profile.getId());
        return ResponseEntity.ok("deleted");
    }

    @GetMapping("/getAllByProfile")
    @ApiOperation(value = "get All comments by Profile method")
    @ApiResponse(code = 200,message = "Succesful",response = CommentDto.class,responseContainer = "List")
    public ResponseEntity<List<CommentDto>> getAllByProfile(@ApiParam(name ="Id" ,value = "id of Profile",required = true)
                                                                @RequestParam("profileId") Integer profileId) {
        List<CommentDto> dtoList = commentService.getAllByProfile(profileId);
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/getByArticle/{articleId}")
    @ApiOperation(value = "get All comments by article method")
    @ApiResponse(code = 200,message = "Succesful",response = CommentDto.class,responseContainer = "PageImpl")
    public ResponseEntity<PageImpl<CommentDto>> getAllByArticle(@ApiParam(name = "Id",value = "Id of article", required = true)
                                                  @PathVariable("articleId")Integer articleId,
                                                  @RequestParam("page") Integer page,
                                                  @RequestParam("size") Integer size){
        PageImpl<CommentDto> dtoPage=commentService.getAllByArticle(articleId,page,size);
        return ResponseEntity.ok(dtoPage);
    }
    @GetMapping("/filter")
    @ApiOperation(value = "filter method")
    @ApiResponse(code = 200,message = "Succesful",response = CommentDto.class,responseContainer = "PageImpl")
    public ResponseEntity<PageImpl<CommentDto>>  filter(@RequestParam("page")int page, @RequestParam("size") int size,
                                     @RequestBody CommentFilterDto filterDto){
        PageImpl<CommentDto> dtoPage=commentService.filter(page,size,filterDto);
        return ResponseEntity.ok(dtoPage);
    }



}
