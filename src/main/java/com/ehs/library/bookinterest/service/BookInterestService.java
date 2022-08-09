package com.ehs.library.bookinterest.service;

import com.ehs.library.book.entity.Book;
import com.ehs.library.book.repository.BookRepository;
import com.ehs.library.bookinterest.entity.BookInterest;
import com.ehs.library.bookinterest.repository.BookInterestRepository;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookInterestService {
    private final BookInterestRepository bookInterestRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public Long insertBookInterest(String email, Long bid){
        Member member = memberRepository.findByEmail(email);
        Book book = bookRepository.findById(bid).get();

//        if(bookInterestRepository.countByMemberAndBook(book,member)==0){
            BookInterest bookInterest = new BookInterest(book, member, LocalDateTime.now());
            bookInterestRepository.save(bookInterest);
            return bookInterest.getId();
//        }
//        else{
//            return -1L;
//        }
    }

    public void deleteBookInterest(Long id){
        bookInterestRepository.delete(bookInterestRepository.findById(id).get());
    }
}
