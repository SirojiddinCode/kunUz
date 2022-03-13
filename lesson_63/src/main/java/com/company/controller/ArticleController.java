package com.company.controller;

import com.company.dto.article.ArticleDTO;
import com.company.dto.article.ArticleFilterDto;
import com.company.dto.profile.ProfileJwtDTO;
import com.company.enums.ProfileRole;
import com.company.service.ArticleService;
import com.company.util.JwtUtil;
import io.swagger.annotations.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/article")
@Api(tags = "Article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/create")
    @ApiOperation(value = "created method")
    @ApiResponses(value = {
            @ApiResponse(code =200,message = "Successful",response = ArticleDTO.class,responseContainer = "List")
    })
    public ResponseEntity<ArticleDTO> create(@Valid @RequestBody ArticleDTO dto,
                                    HttpServletRequest request) {


        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.MODERATOR_ROLE);
        ArticleDTO response = articleService.create(dto, jwtDTO.getId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/publish/{id}")
    @ApiOperation(value = "article publish method")
    @ApiResponses(value = {
            @ApiResponse(code =200,message = "Successful",response = String.class)
    })
    public ResponseEntity<String> publish(@ApiParam(value = "articleId",required = true)
                                         @PathVariable("id") Integer id,
                                         HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.PUBLISHER_ROLE);
        articleService.publish(id, jwtDTO.getId());
        return ResponseEntity.ok("published");
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "update method")
    @ApiResponses(value = {
            @ApiResponse(code =200,message = "Successful",response = ArticleDTO.class)
    })
    public ResponseEntity<ArticleDTO> update(@Valid @ApiParam(value = "id of article",required = true)
                                    @PathVariable("id")  Integer id,
                                    @RequestBody ArticleDTO dto,
                                    HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.MODERATOR_ROLE);
        ArticleDTO result = articleService.update(id, jwtDTO.getId(), dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "delete method")
    @ApiResponses(value = {
            @ApiResponse(code =200,message = "Successful",response = String.class)
    })
    public ResponseEntity<String> delete (@ApiParam(value = "id of article",required = true)@PathVariable("id") Integer id, HttpServletRequest request) {
        JwtUtil.getProfile(request, ProfileRole.PUBLISHER_ROLE);
        articleService.delete(id);
        return ResponseEntity.ok("Deleted");
    }

    @PutMapping("/block/{id}")
    @ApiOperation(value = "article blocking method")
    @ApiResponses(value = {
            @ApiResponse(code =200,message = "Successful",response = String.class)
    })
    public ResponseEntity<String> blockArticle(@ApiParam(name = "id of article",required = true)
                                                   @PathVariable("id") Integer id, HttpServletRequest request) {
        JwtUtil.getProfile(request, ProfileRole.PUBLISHER_ROLE);
        articleService.blockArticle(id);
        return ResponseEntity.ok("Article Blocked");
    }

    @GetMapping("/get/{id}")
    @ApiOperation(value = "get article by id ")
    @ApiResponses(value = {
            @ApiResponse(code =200,message = "Successful",response = ArticleDTO.class)
    })
    public ResponseEntity<ArticleDTO> getByid(@ApiParam(name = "id of article",required = true)@PathVariable("id") Integer id) {
        ArticleDTO dto = articleService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/getAll")
    @ApiOperation(value = "all article get method")
    @ApiResponses(value = {
            @ApiResponse(code =200,message = "Successful",response = ArticleDTO.class,responseContainer = "List")
    })
    public ResponseEntity<List<ArticleDTO>> getAll() {
        List<ArticleDTO> dtoList = articleService.getAll();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/filter")
    @ApiOperation(value = "filter method ")
    @ApiResponses(value = {
            @ApiResponse(code =200,message = "Successful",response = ArticleDTO.class)
    })
    public ResponseEntity<PageImpl<ArticleDTO>> filter(@RequestBody ArticleFilterDto filterDto,
                                    @ApiParam(required = true)@RequestParam("page")int page,
                                    @ApiParam(required = true)@RequestParam("size")int size){
        PageImpl<ArticleDTO> dtoList=articleService.filter(page, size,filterDto);
        return ResponseEntity.ok(dtoList);
    }

}
