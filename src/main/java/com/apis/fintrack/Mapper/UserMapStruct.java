package com.apis.fintrack.Mapper;

import com.apis.fintrack.DTO.UserEntity.Entry.CreateUserDTO;
import com.apis.fintrack.DTO.UserEntity.Exit.ShowUserDTO;
import com.apis.fintrack.Entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapStruct {
    @Mapping(source = "role", target = "role.roleName")
    public UserEntity toUserEntity(CreateUserDTO dto);
    @Mapping(source = "", target = "")
    public ShowUserDTO toShowUserDTO(UserEntity userEntity);

}
