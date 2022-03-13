package com.company.service;

import com.company.dto.auth.RegistrationDto;
import com.company.dto.profile.ProfileFilterDto;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.repositoryImpl.ProfileCustomRepositoryImpl;
import com.company.repository.ProfileRepository;
import com.company.dto.profile.ProfileDTO;
import com.company.entity.ProfileEntity;
import com.company.spec.ProfileSpecification;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Profiles;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileCustomRepositoryImpl profileCustomRepository;

    public ProfileEntity get(Integer id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Profile not found"));
    }

    public ProfileDTO createProfileAdmin(ProfileDTO dto, Integer adminId) {
        if (!isAdmin(adminId)) {
            return null;
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setLogin(dto.getLogin());
        String pswd = DigestUtils.md5Hex(dto.getPswd());
        entity.setPswd(pswd);
        entity.setRole(dto.getRole());
        entity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public List<ProfileDTO> getAll() {
        List<ProfileEntity> entityList = profileRepository.findAll();
        return entityList.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ProfileDTO getById(Integer id) {
        Optional<ProfileEntity> entity = profileRepository.findById(id);
        return entity.map(this::toDTO).orElse(null);
    }

    public boolean update(Integer adminId, ProfileDTO dto) {
        @NotNull
        Integer id = dto.getId();
        if (profileRepository.existsById(id)) {
            String pswd = DigestUtils.md5Hex(dto.getPswd());
            profileRepository.update(id, dto.getName(), dto.getSurname(), dto.getLogin(),
                    pswd);
            return true;
        }
        throw new ItemNotFoundException("Not Found");
    }

    public boolean delete(Integer id) {
        if (profileRepository.existsById(id)) {
            profileRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean isAdmin(Integer id) {
        return getById(id).getRole().equals(ProfileRole.ADMIN_ROLE);
    }

    public ProfileDTO toDTO(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setLogin(entity.getLogin());
        dto.setPswd(entity.getPswd());
        dto.setRole(entity.getRole());
        return dto;
    }

    public boolean existsById(Integer id) {
        return profileRepository.existsById(id);
    }

   /* public PageImpl<ProfileDTO> filter(int page, int size, ProfileFilterDto filterDto){
        PageImpl<ProfileEntity> entityPage=profileCustomRepository.filter(page,size,filterDto);
        List<ProfileDTO> dtoList=entityPage.stream().map(this::toDTO).toList();
        return new PageImpl<>(dtoList,entityPage.getPageable(),entityPage.getTotalElements());
    }*/

    public PageImpl<ProfileDTO> filter(int page, int size, ProfileFilterDto filterDto) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        Specification<ProfileEntity> specification;
        if (filterDto.getStatus() != null) {
            specification = Specification.where(ProfileSpecification.profileStatus(filterDto.getStatus()));
        } else {
            specification = Specification.where(ProfileSpecification.profileStatus(ProfileStatus.ACTIVE));
        }
        if (filterDto.getId() != null) {
            specification = specification.and(ProfileSpecification.id("id", filterDto.getId()));
        }
        if (filterDto.getRole() != null) {
            specification = specification.and(ProfileSpecification.role(filterDto.getRole()));
        }
        if (filterDto.getEmail() != null || filterDto.getEmail().isEmpty()) {
            specification = specification.and(ProfileSpecification.stringField("email", filterDto.getEmail()));
        }
        if (filterDto.getName() != null || filterDto.getName().isEmpty()) {
            specification = specification.and((ProfileSpecification.stringField("name", filterDto.getName())));
        }
        if (filterDto.getSurname() != null || filterDto.getSurname().isEmpty()) {
            specification = specification.and((ProfileSpecification.stringField("surname", filterDto.getSurname())));
        }
        if (filterDto.getFromDate() != null) {
            specification = specification.and((ProfileSpecification.greaterThanOrEqualTo(filterDto.getFromDate())));
        }
        if (filterDto.getToDate() != null) {
            specification = specification.and(ProfileSpecification.lessThanOrEqualTo(filterDto.getToDate()));
        }
        Page<ProfileEntity> entityPage = profileRepository.findAll(specification, pageable);
        List<ProfileDTO> dtoList = entityPage.getContent().stream().map(this::toDTO).toList();

        return new PageImpl<>(dtoList, entityPage.getPageable(), entityPage.getTotalElements());
    }
}
