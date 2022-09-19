package com.ehs.library.sanction.service;

import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import com.ehs.library.sanction.constant.SanctionState;
import com.ehs.library.sanction.entity.Sanction;
import com.ehs.library.sanction.repository.SanctionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SanctionService {
    private final SanctionRepository sanctionRepository;
    private final MemberRepository memberRepository;


    // 특정 멤버의 제재 내역 조회
    public List<Sanction> findBookSanctionsByMemberFetchJoin(String email){
        return sanctionRepository.findBookSanctionsByMemberFetchJoin(memberRepository.findByEmail(email));
    }

    public List<Sanction> findStudyRoomSanctionsByMemberFetchJoin(String email){
        return sanctionRepository.findStudyRoomSanctionsByMemberFetchJoin(memberRepository.findByEmail(email));
    }

    // remainDay -1
    public void decreaseRemainDay(){
        List<Member> memberList = memberRepository.findByRole(Role.USER);

        for(Member member : memberList){
            if(member.getSanctionBookDay()>0) {
                member.setSanctionBookDay(member.getSanctionBookDay()-1);
            }
            if(member.getSanctionStudyRoomDay()>0){
                member.setSanctionStudyRoomDay(member.getSanctionStudyRoomDay()-1);
            }
        }

    }
}
