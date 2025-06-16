package com.pharmacy.management.mapper;

import com.pharmacy.management.dto.UserDTO;
import com.pharmacy.management.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", source = "userId")
    UserDTO toDTO(User user);

    @Mapping(target = "userId", source = "id")
    User toEntity(UserDTO userDTO);
}
