package com.zerogravitysolutions.core.results.commons;

import com.zerogravitysolutions.core.results.ResultDTO;
import com.zerogravitysolutions.core.results.ResultDocument;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ResultMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateResultDocumentFromDto(
            ResultDTO resultDTO,
            @MappingTarget ResultDocument resultDocument
    );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ResultDTO toResultDTO(ResultDocument resultDocument);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Set<ResultDTO> toResultDTOs(Set<ResultDocument> resultDocuments);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<ResultDTO> toResultDTOsList(List<ResultDocument> resultDocuments);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ResultDocument toResultDocument(ResultDTO resultDTO);

}
