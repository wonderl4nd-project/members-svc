package com.bagusmahendra.wonderl4nd.members.controller.dto;

import com.bagusmahendra.wonderl4nd.members.model.Members;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembersDto {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phone;
    private String userstatus;

    public static MembersDto fromEntity(Members member) {
        MembersDto membersDto = new MembersDto();
        membersDto.setUsername(member.getUsername());
        membersDto.setFirstname(member.getFirstname());
        membersDto.setLastname(member.getLastname());
        membersDto.setEmail(member.getEmail());
        membersDto.setPassword(member.getPassword());
        membersDto.setPhone(member.getPhone());
        membersDto.setUserstatus(member.getUserstatus());
        return membersDto;
    }
}