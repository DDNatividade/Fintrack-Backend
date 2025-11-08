package com.apis.fintrack.Mapper;
import com.apis.fintrack.DTO.UserEntity.Entry.CreateUserDTO;
import com.apis.fintrack.DTO.UserEntity.Exit.ShowUserDTO;
import com.apis.fintrack.Entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapStruct {


    @Mapping(source = "role.roleName", target = "role")
    ShowUserDTO toShowUserDTO(UserEntity userEntity);

    @Mapping(source="role", target="role.roleName")
    UserEntity toUserEntity(CreateUserDTO dto);


}
