package com.company.controller;

import com.company.dto.EmailHistoryDTO;
import com.company.dto.LikeCountDto;
import com.company.dto.LikeDto;
import com.company.dto.profile.ProfileJwtDTO;
import com.company.enums.LikeType;
import com.company.service.LikeService;
import com.company.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/like")
@Api(tags = "Like or DisLike")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping("/create")
    @ApiOperation(value = "create like or dislike method")
    @ApiResponse(code = 200,message = "Successful ",response = LikeDto.class)
    public ResponseEntity<LikeDto> createLike(@RequestBody LikeDto dto, HttpServletRequest request) {
        ProfileJwtDTO jwtDTO=JwtUtil.getProfile(request);
        LikeDto likeDto = likeService.create(dto,jwtDTO.getId());
        return ResponseEntity.ok(likeDto);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "delete like method")
    @ApiResponse(code = 200,message = "Successful",response = String.class)
    public ResponseEntity<String> delete(@ApiParam(name = "Id",value = "Id of Like")
                                             @PathVariable("id") Integer id, HttpServletRequest request) {
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request);
        likeService.delete(id, profileJwtDTO.getId());
        return ResponseEntity.ok("Deleted");
    }

    @PutMapping("/update")
    @ApiOperation(value = "update like method")
    @ApiResponse(code = 200,message = "Successful",response = String.class)
    public ResponseEntity<String> update(@RequestBody LikeDto dto, HttpServletRequest request) {
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request);
        likeService.update(profileJwtDTO.getId(), dto);
        return ResponseEntity.ok("updated");
    }

    @GetMapping("/articlelikeOrDislike")
    @ApiOperation(nickname ="LikeOrDisLikeCount" ,value = "like or dislike count for article method")
    @ApiResponse(code = 200,message = "Successful",response = LikeCountDto.class)
    public ResponseEntity<LikeCountDto> getCountLikeOfArticle(@ApiParam(value = "id of article",required = true)
                                                                  @RequestParam("articleId") Integer id) {
        LikeCountDto countDto = likeService.likeAndDislikeCount(id, LikeType.Article);
        return ResponseEntity.ok(countDto);
    }

    @GetMapping("/commentlikeOrDislike")
    @ApiOperation(nickname ="LikeOrDisLikeCount" ,value = "like or dislike count for comment method")
    @ApiResponse(code = 200,message = "Successful",response = LikeCountDto.class)
    public ResponseEntity<?> getCountLikeOfComment(@ApiParam(value = "id of comment",required = true)
                                                       @RequestParam("commentId") Integer id) {
        LikeCountDto countDto = likeService.likeAndDislikeCount(id, LikeType.Comment);
        return ResponseEntity.ok(countDto);
    }
}
