package com.zerogravitysolutions.core.volunteers.commons;

import com.zerogravitysolutions.core.volunteers.VolunteersDTO;
import com.zerogravitysolutions.core.volunteers.VolunteersDocument;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VolunteersMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapDtoToDocument(VolunteersDTO source, @MappingTarget VolunteersDocument target);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    VolunteersDocument mapDtoToDocument(VolunteersDTO source);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    VolunteersDTO mapDocumentToDto(VolunteersDocument source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<VolunteersDTO> mapDocumentsToDtos (List<VolunteersDocument> sourceList);

}

