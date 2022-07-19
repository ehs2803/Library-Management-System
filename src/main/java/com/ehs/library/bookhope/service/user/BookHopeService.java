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

    public List<BookHope> findByMemberAndState(String email, BookHopeState bookHopeState){
        Member member = memberRepository.findByEmail(email);
        return bookHopeRepository.findByIdAndState(member.getId(), bookHopeState);
    }

    public Long getMemberId(String email){
        return memberRepository.findByEmail(email).getId();
    }

    public BookHope findById(Long id){
        return bookHopeRepository.findById(id).get();
    }
}
