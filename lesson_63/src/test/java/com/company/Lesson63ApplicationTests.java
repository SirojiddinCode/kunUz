package com.company;

import com.company.dto.article.ArticleFilterDto;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.repository.repositoryImpl.ArticleCustomRepositoryImpl;
import com.company.repository.ProfileRepository;
import com.company.service.ArticleService;
import com.company.service.ProfileService;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Lesson63ApplicationTests {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ProfileRepository repository;

    @Test
    void createProfile() {
        ProfileEntity dto = new ProfileEntity();
        dto.setName("Sirojiddin");
        dto.setSurname("Mamatqulov");
        dto.setLogin("sirojiddin");
        dto.setEmail("mamatqulovsirojiddin7@gmail.com");
        String psw= DigestUtils.md5Hex("sirojiddin28");
        dto.setPswd(psw);
        dto.setRole(ProfileRole.ADMIN_ROLE);
        dto.setStatus(ProfileStatus.ACTIVE);
        repository.save(dto);
    }

    @Autowired
    private ArticleCustomRepositoryImpl articleCustomRepository;
    @Test
    public void createArticle() {
        ArticleFilterDto dto = new ArticleFilterDto();
       dto.setProfileId(16);
        articleCustomRepository.filter2(dto);

    }

}
