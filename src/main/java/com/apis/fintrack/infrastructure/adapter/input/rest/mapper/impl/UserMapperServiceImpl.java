package com.apis.fintrack.infrastructure.adapter.input.rest.mapper.impl;

import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.CreateUserDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.UserLoginDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.UserPatch.ChangeUserBirthdayDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.UserPatch.ChangeUserEmailDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.UserPatch.ChangeUserNameDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.UserPatch.ChangeUserPasswordDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.UserPatch.ChangeUserSurnameDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.response.Exit.ShowUserDTO;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.UserJPAEntity;
import com.apis.fintrack.infrastructure.adapter.input.rest.mapper.UserMapperService;
import org.springframework.stereotype.Service;

@Service
public class UserMapperServiceImpl implements UserMapperService {

    @Override
    public ShowUserDTO toShowUserDTO(UserJPAEntity userEntity) {
        return new ShowUserDTO(
                userEntity.getName(),
                userEntity.getSurname(),
                userEntity.getEmail(),
                userEntity.getAvailableFunds(),
                userEntity.getRole().getRoleName()
        );
    }

    @Override
    public UserJPAEntity toUserEntity(CreateUserDTO createUserDTO) {
        UserJPAEntity userEntity = new UserJPAEntity();
        userEntity.setName(createUserDTO.name());
        userEntity.setSurname(createUserDTO.surname());
        userEntity.setEmail(createUserDTO.email());
        userEntity.setPassword(createUserDTO.password());
        userEntity.setBirthDate(createUserDTO.birthDate());
        return userEntity;
    }

    @Override
    public UserJPAEntity toUserEntity(UserLoginDTO userLoginDTO) {
        UserJPAEntity userEntity = new UserJPAEntity();
        userEntity.setEmail(userLoginDTO.email());
        userEntity.setPassword(userLoginDTO.password());
        return userEntity;
    }

    @Override
    public void updateUserFromDTO(ChangeUserNameDTO dto, UserJPAEntity userEntity) {
        userEntity.setName(dto.name());
    }

    @Override
    public void updateUserFromDTO(ChangeUserSurnameDTO dto, UserJPAEntity userEntity) {
        userEntity.setSurname(dto.surname());
    }

    @Override
    public void updateUserFromDTO(ChangeUserEmailDTO dto, UserJPAEntity userEntity) {
        userEntity.setEmail(dto.email());
    }

    @Override
    public void updateUserFromDTO(ChangeUserPasswordDTO dto, UserJPAEntity userEntity) {
        userEntity.setPassword(dto.password());
    }

    @Override
    public void updateUserFromDTO(ChangeUserBirthdayDTO dto, UserJPAEntity userEntity) {
        userEntity.setBirthDate(dto.birthday());
    }
}

