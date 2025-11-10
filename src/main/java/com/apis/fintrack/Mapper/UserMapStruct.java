package com.apis.fintrack.Mapper;
import com.apis.fintrack.DAO.RoleRepository;
import com.apis.fintrack.DTO.UserEntity.Entry.CreateUserDTO;
import com.apis.fintrack.DTO.UserEntity.Exit.ShowUserDTO;
import com.apis.fintrack.Entity.RoleEntity;
import com.apis.fintrack.Entity.RoleEnum;
import com.apis.fintrack.Entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring")
public interface UserMapStruct {





    ShowUserDTO toShowUserDTO(UserEntity userEntity);

    UserEntity toUserEntity(CreateUserDTO dto);



}
