package com.ehs.library.book.service;

import com.ehs.library.book.constant.BookHopeState;
import com.ehs.library.book.dto.BookHopeFormDto;
import com.ehs.library.book.dto.BookHopeUpdateDto;
import com.ehs.library.book.entity.BookHope;
import com.ehs.library.book.repository.BookHopeRepository;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookHopeService {
    private final MemberRepository memberRepository;
    private final BookHopeRepository bookHopeRepository;

    public Long registerBookHope(BookHopeFormDto bookHopeFormDto, String email){
        BookHope bookHope = new BookHope();
        bookHope.setName(bookHopeFormDto.getName());
        bookHope.setIsbn(bookHopeFormDto.getIsbn());
        bookHope.setAuthor(bookHopeFormDto.getAuthor());
        bookHope.setPublisher(bookHopeFormDto.getPublisher());
        bookHope.setYear(bookHopeFormDto.getYear());
        bookHope.setState(BookHopeState.REVIEW);
        Member findMember = memberRepository.findByEmail(email);
        bookHope.setUser(findMember);
        bookHopeRepository.save(bookHope);

        return bookHope.getId();
    }

    public List<BookHope> findByState(BookHopeState bookHopeState){
        return bookHopeRepository.findByState(bookHopeState);
    }

    public int countByState(BookHopeState bookHopeState){
        return bookHopeRepository.countBookHopeByState(bookHopeState);
    }

    public Optional<BookHope> findById(Long id){
        return bookHopeRepository.findById(id);
    }

    public Long updateState(BookHopeUpdateDto bookHopeUpdateDto){
        BookHope bookHope = bookHopeRepository.findById(bookHopeUpdateDto.getId()).get();
        bookHope.setState(bookHopeUpdateDto.getState());
        if(bookHopeUpdateDto.getState().toString().equals("ALLOW")){
            bookHope.setAllowTime(LocalDateTime.now());
        }
        return bookHope.getId();
    }
}