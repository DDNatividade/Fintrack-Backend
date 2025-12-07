package com.apis.fintrack.infrastructure.adapter.input.rest.mapper;

import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.CreateUserDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.UserLoginDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.UserPatch.ChangeUserBirthdayDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.UserPatch.ChangeUserEmailDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.UserPatch.ChangeUserNameDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.UserPatch.ChangeUserPasswordDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.UserPatch.ChangeUserSurnameDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.response.Exit.ShowUserDTO;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.UserJPAEntity;

public interface UserMapperService {
    ShowUserDTO toShowUserDTO(UserJPAEntity userEntity);
    UserJPAEntity toUserEntity(CreateUserDTO createUserDTO);
    UserJPAEntity toUserEntity(UserLoginDTO userLoginDTO);


    /*PATCH*/
    void updateUserFromDTO(ChangeUserNameDTO dto, UserJPAEntity userEntity);
    void updateUserFromDTO(ChangeUserSurnameDTO dto, UserJPAEntity userEntity);
    void updateUserFromDTO(ChangeUserEmailDTO dto, UserJPAEntity userEntity);
    void updateUserFromDTO(ChangeUserPasswordDTO dto, UserJPAEntity userEntity);
    void updateUserFromDTO(ChangeUserBirthdayDTO dto, UserJPAEntity userEntity);
}
