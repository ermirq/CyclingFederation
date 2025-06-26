package com.zerogravitysolutions.core.members.commons;

import com.zerogravitysolutions.core.members.MemberDTO;
import com.zerogravitysolutions.core.members.MemberDocument;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapDtoToDocument(MemberDTO source, @MappingTarget MemberDocument target);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MemberDocument mapDtoToDocument(MemberDTO source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MemberDTO mapDocumentToDto(MemberDocument source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<MemberDTO> mapDocumentsToDtos (List<MemberDocument> sourceList);
}
