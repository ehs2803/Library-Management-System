package com.ehs.library.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberDto {
    private Long id;
    private String name;
    private String email;
    private String tel;
    private String address;
    private String role;
}
