package com.zerogravitysolutions.core.clubs.mapper;

import com.zerogravitysolutions.core.clubs.ClubDTO;
import com.zerogravitysolutions.core.clubs.ClubDocument;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ClubMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapDtoToDocument(ClubDTO source, @MappingTarget ClubDocument target);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClubDocument mapDtoToDocument(ClubDTO source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClubDTO mapDocumentToDto(ClubDocument source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Set<ClubDTO> mapDocumentToDto(Set<ClubDocument> source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<ClubDTO> mapDocumentsToDtos (List<ClubDocument> sourceList);
}
