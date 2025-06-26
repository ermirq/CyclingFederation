package com.zerogravitysolutions.core.checkpoints.commons;

import com.zerogravitysolutions.core.checkpoints.CheckpointDTO;
import com.zerogravitysolutions.core.checkpoints.CheckpointDocument;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CheckpointMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCheckpointDocumentFromDto(
            CheckpointDTO checkpointDTO,
            @MappingTarget CheckpointDocument checkpointDocument
    );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CheckpointDTO toCheckpointDTO(CheckpointDocument checkpointDocument);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Set<CheckpointDTO> toCheckpointsDTOsSet(Set<CheckpointDocument> checkpointDocuments);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<CheckpointDTO> toCheckpointDTOsList(List<CheckpointDocument> checkpointDocuments);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CheckpointDocument toCheckpointDocument(CheckpointDTO checkpointDTO);

}
