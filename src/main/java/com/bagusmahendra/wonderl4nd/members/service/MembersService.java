package com.bagusmahendra.wonderl4nd.members.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.bagusmahendra.wonderl4nd.members.controller.dto.MembersDto;
import com.bagusmahendra.wonderl4nd.members.model.Members;
import com.bagusmahendra.wonderl4nd.members.repository.MembersRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class MembersService {
    private final MembersRepository membersRepository;

    public MembersService(MembersRepository membersRepository) {
        this.membersRepository = membersRepository;
    }

    public Mono<MembersDto> createMember(MembersDto membersDto) {
        Members newMembers = new Members();
        BeanUtils.copyProperties(membersDto, newMembers);
        newMembers.setUserstatus("ACTIVE");
        log.info("Creating new member with email: {}", membersDto.getEmail());
        return membersRepository.findByEmail(membersDto.getEmail())
                .flatMap(existingMember -> {
                    log.error("Email already exists: {}", membersDto.getEmail());
                    throw new RuntimeException("Email already exists");
                })
                .switchIfEmpty(membersRepository.save(newMembers))
                .map(saveMember ->{
                    log.info("Member created successfully: {}", ((Members) saveMember).getUsername());
                    MembersDto returnMembersDto = new MembersDto();
                    BeanUtils.copyProperties(saveMember, returnMembersDto);
                    return returnMembersDto;
                });
    }

    public Mono<MembersDto> getMember(String username) {
        log.info("Getting member with username: {}", username);
        return membersRepository.findByUsername(username)
        .switchIfEmpty(Mono.error(new RuntimeException("Member with username " + username + " not found")))
        .map(MembersDto::fromEntity);
    }

    public Mono<MembersDto> updateMember(String username, MembersDto memberDto) {
        log.info("Updating member with username: {}", username);
        return membersRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new RuntimeException("Member with username " + username + " not found")))
                .flatMap(existingMember -> {
                    existingMember.setFirstname(memberDto.getFirstname() != null ? memberDto.getFirstname() : existingMember.getFirstname());
                    existingMember.setLastname(memberDto.getLastname() != null ? memberDto.getLastname() : existingMember.getLastname());
                    existingMember.setPassword(memberDto.getPassword() != null ? memberDto.getPassword() : existingMember.getPassword());
                    existingMember.setPhone(memberDto.getPhone() != null ? memberDto.getPhone() : existingMember.getPhone());
                    existingMember.setUserstatus(memberDto.getUserstatus() != null ? memberDto.getUserstatus() : existingMember.getUserstatus());
                    return membersRepository.save(existingMember);
                })
                .map(MembersDto::fromEntity);
    }
    

    public Mono<Void> deleteMember(String username) {
        log.info("Deleting member with username: {}", username);
        return membersRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new RuntimeException("Member with username " + username + " not found")))
                .flatMap(membersRepository::delete);
    }

    public Mono<String> login(String email, String password) {
        log.info("Logging in with email: {}", email);
        return membersRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid email or password")))
                .filter(member -> member.getPassword().equals(password))
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid email or password")))
                .map(member -> {
                    log.info("User {} logged in successfully.", member.getUsername());
                    return "Bearer " + "token";
                });
    }
}
