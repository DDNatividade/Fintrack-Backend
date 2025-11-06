package com.apis.fintrack.Mapper;

import com.apis.fintrack.DTO.UserEntity.Entry.CreateUserDTO;
import com.apis.fintrack.DTO.UserEntity.Exit.ShowUserDTO;
import com.apis.fintrack.Entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapStruct {
    public UserEntity toUserEntity(CreateUserDTO dto);
    public ShowUserDTO toShowUserDTO(UserEntity userEntity);

}
