package com.ehs.library.base.service;

import com.ehs.library.base.entity.HitPerDay;
import com.ehs.library.base.repository.HitPerDayRepository;
import com.ehs.library.book.repository.BookRepository;
import com.ehs.library.bookreservation.entity.BookReservation;
import com.ehs.library.member.constant.Role;
import com.ehs.library.member.repository.MemberRepository;
import com.ehs.library.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class MainService {
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final HitPerDayRepository hitPerDayRepository;

    //EntityManager em;

    // 일반 유저수 조회
    @Transactional(readOnly = true)
    public Long getuserMemberCnt(){
        return memberRepository.countByRole(Role.USER);
    }

    // 도서수 조회
    @Transactional(readOnly = true)
    public Long getBookCnt(){
        return bookRepository.count();
    }

    // 당일 조회수 조회
    @Transactional(readOnly = true)
    public int getHitPerDay(){
        return hitPerDayRepository.findByDay(LocalDate.now()).getHit();
    }

    // 당일 조회수 증가
    public Long increaseHit(){
        HitPerDay hitPerDay = hitPerDayRepository.findByDay(LocalDate.now());
        if(hitPerDay==null){ // 당일 날짜의 HipPerDay가 없는 경우
            HitPerDay newHitPerDay = new HitPerDay();
            newHitPerDay.setDay(LocalDate.now());
            newHitPerDay.setHit(1);
            hitPerDayRepository.save(newHitPerDay);
            return newHitPerDay.getId();
        }
        else{
            hitPerDay.setHit(hitPerDay.getHit()+1);
            return hitPerDay.getId();
        }
    }
}
