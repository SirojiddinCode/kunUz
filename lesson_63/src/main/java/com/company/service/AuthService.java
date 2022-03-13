package com.company.service;

import com.company.dto.profile.ProfileDTO;
import com.company.dto.auth.AuthorizationDTO;
import com.company.dto.auth.RegistrationDto;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exceptions.BadRequestException;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.ProfileRepository;
import com.company.util.JwtUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private EmailService emailService;

    public ProfileDTO authorization(AuthorizationDTO dto) {
        String pswd = DigestUtils.md5Hex(dto.getPassword());
        Optional<ProfileEntity> optional = profileRepository
                .findByLoginAndPswd(dto.getLogin(), pswd);
        if (!optional.isPresent()) {
            throw new BadRequestException("Login or Password incorrect");
        }
        if (optional.get().getStatus().equals(ProfileStatus.REGISTRATION) ||
                optional.get().getStatus().equals(ProfileStatus.BLOCK)) {
            throw new BadRequestException("you are not allowed");
        }

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setName(optional.get().getName());
        profileDTO.setSurname(optional.get().getSurname());
        profileDTO.setJwt(JwtUtil.createJwt(optional.get().getId(), optional.get().getRole()));
        return profileDTO;
    }

    public void registration(RegistrationDto dto) {
        ProfileEntity entity = new ProfileEntity();
        Integer id=0;
        if (profileRepository.existsByEmail(dto.getEmail())) {
            ProfileEntity profile=profileRepository.getByEmail(dto.getEmail());
            if (!profile.getStatus().equals(ProfileStatus.REGISTRATION)) {
                throw new BadRequestException("Mazgi Email uje band");
            }
            id=profile.getId();
        }
        if (profileRepository.existsByLogin(dto.getLogin())&&id==0) {
            throw new BadRequestException("Mazgi bunaqa login borku");
        }
        if (id!=0){
            entity.setId(id);
        }
        String pswd = DigestUtils.md5Hex(dto.getPassword());
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setLogin(dto.getLogin());
        entity.setPswd(pswd);
        entity.setEmail(dto.getEmail());
        entity.setRole(ProfileRole.USER_ROLE);
        entity.setStatus(ProfileStatus.REGISTRATION);
        profileRepository.save(entity);
        String jwtid = JwtUtil.createJwtById(entity.getId());
        StringBuilder builder = new StringBuilder();
        builder.append("Salom jigar Qalaysan\n");
        builder.append("Agar bu sen bo'lsang shu linkga bos: ");
        builder.append("http://localhost:8080/auth/verification/" + jwtid);
        emailService.sendEmail(dto.getEmail(), "Registration KunUz Test", builder.toString());
    }

    public void verification(String jwt) {
        Integer id = JwtUtil.decodeJwtAndGetId(jwt);
        Optional<ProfileEntity> optional = profileRepository.findById(id);
        if (!optional.isPresent()) {
            throw new ItemNotFoundException("User Not Found");
        }
        if (optional.get().getStatus().equals(ProfileStatus.BLOCK)) {
            throw new BadRequestException("you are not allowed");
        }
        emailService.updateEmailHistory();
        optional.get().setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(optional.get());
    }
}
