package com.pharmacy.management.user; // Updated package

import com.pharmacy.management.user.UserDTO; // Updated import
import com.pharmacy.management.user.User; // Updated import
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", source = "userId")
    @Mapping(target = "password", ignore = true) // Do not expose password in DTOs returned from the server
    UserDTO toDTO(User user);

    @Mapping(target = "userId", source = "id")
    User toEntity(UserDTO userDTO);
}
