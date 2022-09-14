package com.ehs.library.bookhope.service.user;

import com.ehs.library.book.entity.Book;
import com.ehs.library.bookhope.constant.BookHopeState;
import com.ehs.library.bookhope.dto.BookHopeFormDto;
import com.ehs.library.bookhope.entity.BookHope;
import com.ehs.library.bookhope.repository.BookHopeRepository;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("user.BookHopeService")
@Transactional
@RequiredArgsConstructor
public class BookHopeService {
    private final MemberRepository memberRepository;
    private final BookHopeRepository bookHopeRepository;

    // 희망도서 신청
    public Long registerBookHope(BookHopeFormDto bookHopeFormDto, String email){
        Member findMember = memberRepository.findByEmail(email);
        BookHope bookHope = BookHope.builder()
                .name(bookHopeFormDto.getName())
                .isbn(bookHopeFormDto.getIsbn())
                .author(bookHopeFormDto.getAuthor())
                .publisher(bookHopeFormDto.getPublisher())
                .year(bookHopeFormDto.getYear())
                .state(BookHopeState.REVIEW)
                .user(findMember)
                .build();

        bookHopeRepository.save(bookHope);

        return bookHope.getId();
    }

    // 멤버, bookhope state 기반 bookhope 목록 조회
    @Transactional(readOnly = true)
    public List<BookHope> findByMemberAndState(String email, BookHopeState bookHopeState){
        Member member = memberRepository.findByEmail(email);
        return bookHopeRepository.findByIdAndState(member.getId(), bookHopeState);
    }

    // 이메일 기반 멤버 아이디 조회
    @Transactional(readOnly = true)
    public Long getMemberId(String email){
        return memberRepository.findByEmail(email).getId();
    }

    // id bookhope 조회
    @Transactional(readOnly = true)
    public BookHope findById(Long id){
        return bookHopeRepository.findById(id).get();
    }
}
