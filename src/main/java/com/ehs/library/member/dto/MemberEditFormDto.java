package com.ehs.library.member.dto;

import com.ehs.library.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberEditFormDto {
    private Long id;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    private String tel;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min=8, max=16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요")
    private String password;

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;

    @NotEmpty(message = "필수 선택값 입니다.")
    private String role;

    public MemberEditFormDto() {
    }

    public MemberEditFormDto(Member member) {
        id = member.getId();
        name = member.getName();
        email = member.getEmail();
        tel = member.getTel();
        password = member.getPassword();
        address = member.getAddress();
        role = member.getRole().toString();
    }


}