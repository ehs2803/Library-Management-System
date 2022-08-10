package com.ehs.library.bookreservation.service;

import com.ehs.library.base.constant.Policy;
import com.ehs.library.book.constant.BookState;
import com.ehs.library.book.entity.Book;
import com.ehs.library.book.repository.BookRepository;
import com.ehs.library.bookreservation.entity.BookReservation;
import com.ehs.library.bookreservation.repository.BookReservationRepository;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookReservationService {
    private final BookReservationRepository bookReservationRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public Long reservationBook(String email, Long bid){
        Book book = bookRepository.findById(bid).get();
        Member member = memberRepository.findByEmail(email);

        int reservationCnt = bookReservationRepository.countBookReservationByBook(book);
        if(reservationCnt< Policy.RESERVATION_LIMIT){
            BookReservation bookReservation = new BookReservation(book, member,
                    Policy.RESERVATION_DAY, reservationCnt+1, LocalDateTime.now());
            bookReservationRepository.save(bookReservation);
            return bookReservation.getId();
        }
        return -1L;
    }

    public void deleteReservation(Long id){
        BookReservation bookReservation = bookReservationRepository.findById(id).get();

        bookReservationRepository.delete(bookReservation);
    }
}
