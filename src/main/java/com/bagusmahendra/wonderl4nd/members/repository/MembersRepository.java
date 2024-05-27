package com.bagusmahendra.wonderl4nd.members.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bagusmahendra.wonderl4nd.members.model.Members;

import reactor.core.publisher.Mono;

public interface MembersRepository extends ReactiveMongoRepository<Members, String> {

    Mono<Members> findByEmail(String email);
    Mono<Members> findByUsername(String username);
    
}
