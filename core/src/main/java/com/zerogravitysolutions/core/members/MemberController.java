package com.zerogravitysolutions.core.members;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @RolesAllowed({"Administrator", "Member"})
    @PostMapping(path = "/members")
    public ResponseEntity<MemberDTO> createMember(@Valid @RequestBody final MemberDTO memberDto) {
        final MemberDTO newMember = memberService.save(memberDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMember);
    }

    @RolesAllowed({"Administrator", "Member", "Cyclist"})
    @GetMapping(path = "/members/{id}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable final String id) {
        final MemberDTO memberFound = memberService.getById(id);
        return ResponseEntity.ok(memberFound);
    }

    @RolesAllowed({"Administrator", "Member", "Cyclist"})
    @GetMapping(path = "/members/page")
    public ResponseEntity<Page<MemberDTO>> findAll(final Pageable page){
        final Page<MemberDTO> members = memberService.getAllMembers(page);
        return ResponseEntity.ok(members);
    }

    @RolesAllowed("Administrator")
    @DeleteMapping(path = "/members/{id}")
    public ResponseEntity<Void> deleteMembersById (@PathVariable final String id){
        memberService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"Administrator", "Member"})
    @PutMapping("/members/{id}")
    public ResponseEntity<MemberDTO> updateMember(
            @PathVariable final String id,
            @RequestBody final MemberDTO updatedMember
    ){
        final MemberDTO memberDto = memberService.updated(id, updatedMember);
        return ResponseEntity.ok(memberDto);
    }

    @RolesAllowed({"Administrator", "Member"})
    @PatchMapping("/members/{id}")
    public ResponseEntity<MemberDTO> patch(
            @PathVariable final String id,
            @RequestBody final MemberDTO memberDto
    ){
        final MemberDTO patched = memberService.patch(id, memberDto);
        return ResponseEntity.ok(patched);
    }

    @GetMapping(path = "/members")
    public ResponseEntity<List<MemberDTO>> findMemberByName(
            @RequestParam(name = "name", required = false) final String name
    ) {
        final List<MemberDTO> members = memberService.findByName(name);

        return ResponseEntity.ok(members);
    }
}
