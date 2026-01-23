package com.apis.fintrack.infrastructure.adapter.input.rest.mapper;

import com.apis.fintrack.domain.user.model.User;
import com.apis.fintrack.domain.user.port.input.RegisterUserUseCase.RegisterUserCommand;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.CreateUserDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.response.Exit.ShowUserDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre DTOs REST y entidades/comandos de dominio.
 * 
 * Este mapper es parte de la capa de infraestructura (adaptador de entrada)
 * y conoce tanto los DTOs como las entidades de dominio.
 */
@Component
public class UserRestMapper {
    
    /**
     * Convierte un CreateUserDTO a un RegisterUserCommand.
     *
     * @param dto el DTO de entrada
     * @return el comando para el caso de uso
     */
    public RegisterUserCommand toCommand(CreateUserDTO dto) {
        return new RegisterUserCommand(
            dto.getName(),
            dto.getSurname(),
            dto.getEmail(),
            dto.getPassword(),
            dto.getBirthDate()
        );
    }
    
    /**
     * Convierte una entidad de dominio User a un ShowUserDTO.
     * 
     * @param user la entidad de dominio
     * @return el DTO de respuesta
     */
    public ShowUserDTO toShowUserDTO(User user) {
        return new ShowUserDTO(
            user.getFullName().getName(),
            user.getFullName().getSurname(),
            user.getEmail().getValue(),
            user.getAvailableFunds().getAmount(),
            user.getRole()
        );
    }
    
    /**
     * Convierte una lista de usuarios a una lista de DTOs.
     * 
     * @param users la lista de entidades de dominio
     * @return la lista de DTOs
     */
    public List<ShowUserDTO> toShowUserDTOList(List<User> users) {
        return users.stream()
                .map(this::toShowUserDTO)
                .collect(Collectors.toList());
    }
}


