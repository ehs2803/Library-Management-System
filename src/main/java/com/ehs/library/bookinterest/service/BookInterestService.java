package com.ehs.library.bookinterest.service;

import com.ehs.library.book.entity.Book;
import com.ehs.library.book.repository.BookRepository;
import com.ehs.library.bookinterest.entity.BookInterest;
import com.ehs.library.bookinterest.repository.BookInterestRepository;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class BookInterestService {
    private final BookInterestRepository bookInterestRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    // 관심도서 등록
    public void insertBookInterest(String email, Long bid){
        Member member = memberRepository.findByEmail(email);
        Book book = bookRepository.findById(bid).get();

        // 로그인한 유저가 이미 해당 도서를 관심도서로 등록하지 않은 경우에만 db에 저장
        if(bookInterestRepository.countBookInterestByMemberAndBook(member, book)==0){
            BookInterest bookInterest = new BookInterest(book, member, LocalDateTime.now());
            bookInterestRepository.save(bookInterest);
        }
    }

    // 관심도서 삭제
    public void deleteBookInterest(Long id){
        bookInterestRepository.delete(bookInterestRepository.findById(id).get());
    }
}
