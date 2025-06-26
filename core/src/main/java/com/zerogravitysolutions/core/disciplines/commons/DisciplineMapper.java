package com.zerogravitysolutions.core.disciplines.commons;

import com.zerogravitysolutions.core.disciplines.DisciplineDTO;
import com.zerogravitysolutions.core.disciplines.DisciplineDocument;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DisciplineMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapDtoToDocument(DisciplineDTO source, @MappingTarget DisciplineDocument target);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DisciplineDocument mapDtoToDocument(DisciplineDTO source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DisciplineDTO mapDocumentToDto(DisciplineDocument source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<DisciplineDTO> mapDocumentsToDtos(List<DisciplineDocument> sourceList);

}

