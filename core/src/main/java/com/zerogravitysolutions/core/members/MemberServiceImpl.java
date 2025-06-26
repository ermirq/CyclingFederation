package com.zerogravitysolutions.core.members;

import com.zerogravitysolutions.core.audit.AuditDocument;
import com.zerogravitysolutions.core.audit.AuditRepository;
import com.zerogravitysolutions.core.members.commons.MemberMapper;
import com.zerogravitysolutions.core.members.exceptions.MemberAgeRestrictionException;
import com.zerogravitysolutions.core.members.exceptions.MemberDuplicateException;
import com.zerogravitysolutions.core.members.exceptions.MemberNotFoundException;
import com.zerogravitysolutions.core.utilities.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final AuditRepository auditRepository;

    public MemberServiceImpl(
            final MemberRepository memberRepository,
            final MemberMapper memberMapper,
            final AuditRepository auditRepository) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.auditRepository = auditRepository;
    }

    @Override
    public MemberDTO save(final MemberDTO memberDto) {
        logger.info("Attempting to save member with email: {}", memberDto.getEmail());
        if (memberRepository.findByEmail(memberDto.getEmail()).isPresent()) {
            logger.warn("Duplicate email detected: {}", memberDto.getEmail());
            throw new MemberDuplicateException(
                    "This email is already being used. Please use another email!"
            );
        } else if (memberDto.getAge() <= 18) {
            logger.warn("Age restriction violated for member: {}", memberDto.getEmail());
            throw new MemberAgeRestrictionException(
                    "This person is too young to be part of the federation!"
            );
        } else if (memberRepository.findByPhoneNumber(memberDto.getPhoneNumber()).isPresent()) {
            throw new MemberDuplicateException(
                    "This phone number is already being used. Please use another phone number!"
            );
        } else {
            MemberDocument memberDocument = new MemberDocument();
            memberMapper.mapDtoToDocument(memberDto, memberDocument);

            memberDocument.setCreatedBy(UserContextHolder.getContext().getUserId());

            memberDocument = memberRepository.save(memberDocument);
            logger.info("Member saved successfully with ID: {}", memberDocument.getId());
            return memberMapper.mapDocumentToDto(memberDocument);
        }
    }

    @Override
    public MemberDTO getById(final String id) {
        logger.info("Fetching member by ID: {}", id);
        final MemberDocument memberDocument = findMemberById(id);
        logger.info("Member found with ID: {}", id);
        return memberMapper.mapDocumentToDto(memberDocument);
    }

    @Override
    public Page<MemberDTO> getAllMembers(final Pageable page) {
        logger.info("Fetching all members with pagination: {}", page);
        final Page<MemberDocument> documents = memberRepository.findAll(page);
        logger.info("Fetched {} members", documents.getTotalElements());
        return documents.map(memberMapper::mapDocumentToDto);
    }

    @Override
    public void softDelete(final String id) {
        logger.info("Soft deleting member with ID: {}", id);
        final MemberDocument members = findMemberById(id);

        final AuditDocument auditDocument = auditCurrentMember(id, members);

        members.setDeletedBy(UserContextHolder.getContext().getUserId());
        members.setDeletedAt(LocalDateTime.now());
        memberRepository.save(members);

        auditUpdatedMember(id, members, auditDocument);
        logger.info("Member with ID {} has been soft deleted", id);
    }

    @Override
    public MemberDTO updated(final String id, final MemberDTO updatedMember) {
        logger.info("Updating member with ID: {}", id);

        final MemberDocument existingMember = findExistingMember(id);
        validateMemberForUpdate(id, updatedMember);
        final AuditDocument auditDocument = auditCurrentMember(id, existingMember);

        updateMemberDetails(existingMember, updatedMember);

        existingMember.setUpdatedBy(UserContextHolder.getContext().getUserId());
        final MemberDocument updated = memberRepository.save(existingMember);
        logger.info("Member with ID {} has been updated", id);

        auditUpdatedMember(id, existingMember, auditDocument);
        return memberMapper.mapDocumentToDto(updated);
    }

    private MemberDocument findExistingMember(final String id) {
        return findMemberById(id);
    }

    private void validateMemberForUpdate(final String id, final MemberDTO updatedMember) {

        if (updatedMember.getAge() <= 18) {
            logger.warn("Age restriction violated for member with ID: {}", id);
            throw new MemberAgeRestrictionException(
                    "This person is too young to be part of the federation!"
            );
        }
    }

    private void updateMemberDetails(
            final MemberDocument existingMember,
            final MemberDTO updatedMember
    ) {
        existingMember.setFirstName(updatedMember.getFirstName());
        existingMember.setLastName(updatedMember.getLastName());
        existingMember.setEmail(updatedMember.getEmail());
        existingMember.setAge(updatedMember.getAge());
        existingMember.setPhoneNumber(updatedMember.getPhoneNumber());
        existingMember.setAddress(updatedMember.getAddress());
        existingMember.setJoinDate(updatedMember.getJoinDate());
        existingMember.setActive(updatedMember.getActive());
    }

    @Override
    public MemberDTO patch(final String id, final MemberDTO memberDto) {
        logger.info("Patching member with ID: {}", id);
        final MemberDocument memberDocument = findMemberById(id);

        final AuditDocument auditDocument = auditCurrentMember(id, memberDocument);

        if (memberDto.getAge() != null && memberDto.getAge() <= 18) {
            logger.warn("Age restriction violated for member with ID: {}", id);
            throw new MemberAgeRestrictionException(
                    "This person is too young to be part of the federation!"
            );
        }

        if (memberRepository.findByPhoneNumber(memberDto.getPhoneNumber()).isPresent()) {
            throw new MemberDuplicateException(
                    "This phone number is already being used. Please use another phone number!"
            );
        }

        if (memberRepository.findByEmail(memberDto.getEmail()).isPresent()) {
            logger.warn("Duplicate email detected: {}", memberDto.getEmail());
            throw new MemberDuplicateException(
                    "This email is already being used. Please use another email!"
            );
        }

        memberDocument.setUpdatedBy(UserContextHolder.getContext().getUserId());

        memberMapper.mapDtoToDocument(memberDto, memberDocument);
        final MemberDocument patched = memberRepository.save(memberDocument);
        logger.info("Member with ID {} has been patched", id);

        auditUpdatedMember(id, memberDocument, auditDocument);
        return memberMapper.mapDocumentToDto(patched);
    }

    @Override
    public List<MemberDTO> findByName(final String name) {
        logger.info("Finding member by name: {}", name);
        final List<MemberDocument> memberDocuments =
                memberRepository.
                        findByFirstNameContainsIgnoreCaseOrLastNameContainsIgnoreCase(name, name);

        final List<MemberDocument> activeMembers = memberDocuments.stream()
                .filter(member -> member.getDeletedAt() == null)
                .toList();

        if (activeMembers.isEmpty()) {
            logger.warn("No active member found with name: {}", name);
            throw new MemberNotFoundException(
                    "Member with name " + name + " not found or has been deleted."
            );
        }

        return memberMapper.mapDocumentsToDtos(memberDocuments);
    }


    private MemberDocument findMemberById (final String id) {
        return memberRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> {
                    logger.error("Member with id {} not found or has been deleted", id);
                    return new MemberNotFoundException(
                            "Member with ID " + id + " not found or has been deleted!");
                });
    }

    private AuditDocument auditCurrentMember (
            final String id,
            final MemberDocument member
    ){
        final MemberDocument oldMember = member.deepCopy();
        final AuditDocument auditDocument = new AuditDocument();
        auditDocument.setOldDocument(oldMember);
        auditDocument.setTimestamp(LocalDateTime.now());
        auditDocument.setAuthor(UserContextHolder.getContext().getUserId());
        auditRepository.save(auditDocument);
        logger.debug("Audit record created for old result of member id: {}", id);
        return auditDocument;
    }

    private void auditUpdatedMember(
            final String id,
            final MemberDocument member,
            final AuditDocument auditDocument
    ) {
        auditDocument.setNewDocument(member);
        auditDocument.setAuthor(UserContextHolder.getContext().getUserId());
        auditRepository.save(auditDocument);
        logger.debug("Audit record updated for member id: {}", id);
    }
}