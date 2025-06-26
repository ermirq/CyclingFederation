package com.zerogravitysolutions.core.members;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {
    MemberDTO save(MemberDTO memberDto);

    MemberDTO getById(String id);

    Page<MemberDTO> getAllMembers(Pageable page);

    void softDelete(String id);

    MemberDTO updated(String id, MemberDTO updatedMember);

    MemberDTO patch(String id, MemberDTO memberDto);

    List<MemberDTO> findByName(String name);
}
