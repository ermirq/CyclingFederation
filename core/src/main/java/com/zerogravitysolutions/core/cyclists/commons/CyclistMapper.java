package com.zerogravitysolutions.core.cyclists.commons;

import com.zerogravitysolutions.core.cyclists.CyclistDTO;
import com.zerogravitysolutions.core.cyclists.CyclistDocument;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CyclistMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapDtoToDocument(CyclistDTO source, @MappingTarget CyclistDocument target);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CyclistDocument mapDtoToDocument(CyclistDTO source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CyclistDTO mapDocumentToDto(CyclistDocument source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<CyclistDTO> mapDocumentToDto (List<CyclistDocument> sourceList);
}
