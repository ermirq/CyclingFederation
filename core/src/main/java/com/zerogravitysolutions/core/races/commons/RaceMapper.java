package com.zerogravitysolutions.core.races.commons;

import com.zerogravitysolutions.core.races.RaceDTO;
import com.zerogravitysolutions.core.races.RaceDocument;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RaceMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapDtoToDocument(RaceDTO source, @MappingTarget RaceDocument target);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RaceDocument mapDtoToDocument(RaceDTO source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RaceDTO mapDocumentToDto(RaceDocument source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<RaceDTO> mapDocumentsToDtos(List<RaceDocument> sourceList);

    List<RaceDocument.Sponsor> mapSponsorsDtoToDocument(List<RaceDTO.SponsorDTO> sponsorDTOS);

    List<RaceDTO.SponsorDTO> mapSponsorsDocumentToDto(List<RaceDocument.Sponsor> sponsors);

    List<RaceDTO.LogisticsDTO> mapLogisticsDocumentToDto(List<RaceDocument.Logistics> logistics);

    List<RaceDocument.Logistics> mapLogisticsDtoToDocument(
            List<RaceDTO.LogisticsDTO> logisticsDTOs);

    List<RaceDocument.CategoriesDocument>mapCategoriesDtoToDocument
            (List<RaceDTO.CategoriesDTO> categoriesDTOS);

    List<RaceDTO.CategoriesDTO>mapCategoriesDocumentToDto
            (List<RaceDocument.CategoriesDocument> categories);

    List<RaceDocument.Participants>mapParticipantsDtoToDocument
            (List<RaceDTO.ParticipantsDTO> participantsDTOS);

    List<RaceDTO.ParticipantsDTO>mapParticipantsDocumentToDto
            (List<RaceDocument.Participants> participants);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapSponsorDtoToDocument(RaceDTO.SponsorDTO source,
                                 @MappingTarget RaceDocument.Sponsor target);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapLogisticDtoToDocument(RaceDTO.LogisticsDTO source,
                                  @MappingTarget RaceDocument.Logistics target);

    RaceDTO.SponsorDTO mapSponsorDocumentToDto(RaceDocument.Sponsor sponsor);

    RaceDTO.LogisticsDTO mapLogisticDocumentToDto(RaceDocument.Logistics logistics);
}
