package com.bagusmahendra.wonderl4nd.members.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bagusmahendra.wonderl4nd.members.controller.dto.LoginDto;
import com.bagusmahendra.wonderl4nd.members.controller.dto.MembersDto;
import com.bagusmahendra.wonderl4nd.members.service.MembersService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/members")
public class MembersController {
    @Autowired
    private MembersService membersService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MembersDto> createMember(@RequestBody MembersDto member) {
        return membersService.createMember(member);
    }

    @GetMapping("/{username}")
    public Mono<ResponseEntity<MembersDto>> getMember(@PathVariable("username") String username) {
        return membersService.getMember(username)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{username}")
    public Mono<ResponseEntity<MembersDto>> updateMember(@PathVariable("username")String username, @RequestBody MembersDto member) {
        return membersService.updateMember(username, member)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMember(@PathVariable("username") String username) {
        return membersService.deleteMember(username);
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody LoginDto loginRequest) {
        return membersService.login(loginRequest.getEmail(), loginRequest.getPassword())
                .map(token -> ResponseEntity.ok(token))
                .defaultIfEmpty(ResponseEntity.badRequest().body("Invalid credentials"));
    }
}
