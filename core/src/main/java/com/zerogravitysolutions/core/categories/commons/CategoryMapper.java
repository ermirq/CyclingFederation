package com.zerogravitysolutions.core.categories.commons;

import com.zerogravitysolutions.core.categories.CategoryDTO;
import com.zerogravitysolutions.core.categories.CategoryDocument;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapDtoToDocument(CategoryDTO source, @MappingTarget CategoryDocument target);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CategoryDocument mapDtoToDocument(CategoryDTO source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CategoryDTO mapDocumentToDto(CategoryDocument source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<CategoryDTO> mapDocumentsToDtos (List<CategoryDocument> sourceList);
}


