package com.ehs.library.member.repository;

import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    List<Member> findByRole(Role role);

    Page<Member> findByRoleAndEmailContaining(Role role, String keyword, Pageable pageable);

}