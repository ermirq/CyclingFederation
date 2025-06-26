package com.zerogravitysolutions.core.users.commons;

import com.zerogravitysolutions.core.users.UserDocument;
import com.zerogravitysolutions.core.users.UserDTO;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserDocumentFromDto(UserDTO userDTO, @MappingTarget UserDocument userDocument);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserDTO toUserDTO(UserDocument userDocument);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Set<UserDTO> toUserDTOs(Set<UserDocument> userDocuments);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<UserDTO> toUserDTOs(List<UserDocument> userDocuments);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserDocument toUserDocument(UserDTO userDTO);
}
