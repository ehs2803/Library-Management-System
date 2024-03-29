package com.ehs.library.member.repository;

import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    Page<Member> findByRoleAndEmailContaining(Role role, String keyword, Pageable pageable);

    Long countByRole(Role role);

    List<Member> findByRole(Role role);
}