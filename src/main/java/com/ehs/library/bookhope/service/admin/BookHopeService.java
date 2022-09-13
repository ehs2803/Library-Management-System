package com.ehs.library.bookhope.service.admin;

import com.ehs.library.bookhope.constant.BookHopeState;
import com.ehs.library.bookhope.dto.BookHopeFormDto;
import com.ehs.library.bookhope.dto.BookHopeUpdateDto;
import com.ehs.library.bookhope.entity.BookHope;
import com.ehs.library.bookhope.repository.BookHopeRepository;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service("admin.BookHopeService")
@Transactional
@RequiredArgsConstructor
public class BookHopeService {
    private final BookHopeRepository bookHopeRepository;

    // 상태별 BookHope 반환
    @Transactional(readOnly = true)
    public List<BookHope> findByState(BookHopeState bookHopeState){
        return bookHopeRepository.findByState(bookHopeState);
    }

    // 상태별 BookHope 개수 반환
    @Transactional(readOnly = true)
    public int countByState(BookHopeState bookHopeState){
        return bookHopeRepository.countBookHopeByState(bookHopeState);
    }

    // id로 bookhope 찾기
    @Transactional(readOnly = true)
    public Optional<BookHope> findById(Long id){
        return bookHopeRepository.findById(id);
    }

    // bookhope state update
    public Long updateState(BookHopeUpdateDto bookHopeUpdateDto){
        BookHope bookHope = bookHopeRepository.findById(bookHopeUpdateDto.getId()).get();
        bookHope.setState(bookHopeUpdateDto.getState()); // state update

        // 각 상태별 시간 업데이트
        if(bookHopeUpdateDto.getState().toString().equals("ALLOW")){
            bookHope.setAllowTime(LocalDateTime.now());
        }
        else if(bookHopeUpdateDto.getState().toString().equals("SHIPPING")){
            bookHope.setShippingTime(LocalDateTime.now());
        }
        else if(bookHopeUpdateDto.getState().toString().equals("ARRANGE")){
            bookHope.setArrangeTime(LocalDateTime.now());
        }
        else if(bookHopeUpdateDto.getState().toString().equals("COMPLETE")){
            bookHope.setCompleteTime(LocalDateTime.now());
        }
        else if(bookHopeUpdateDto.getState().toString().equals("REJECT")){
            bookHope.setRejectTime(LocalDateTime.now());
            bookHope.setFailReason(bookHopeUpdateDto.getFailReason());
        }
        return bookHope.getId();
    }
}
