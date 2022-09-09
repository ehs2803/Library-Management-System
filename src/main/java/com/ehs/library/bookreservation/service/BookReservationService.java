package com.ehs.library.bookreservation.service;

import com.ehs.library.base.constant.Policy;
import com.ehs.library.book.entity.Book;
import com.ehs.library.book.repository.BookRepository;
import com.ehs.library.bookreservation.entity.BookReservation;
import com.ehs.library.bookreservation.repository.BookReservationRepository;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookReservationService {
    private final BookReservationRepository bookReservationRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    // 도서 예약하기
    public Long reservationBook(String email, Long bid) throws Exception {
        Book book = bookRepository.findById(bid).get();
        Member member = memberRepository.findByEmail(email);

        int reservationCnt_member = bookReservationRepository.countBookReservationByMember(member);
        int reservationCnt_book = bookReservationRepository.countBookReservationByBook(book);
        if(bookReservationRepository.existsByMemberAndBook(member, book)){
            throw new Exception("회원님은 이미 해당 도서를 예약했습니다.");
        }
        if(reservationCnt_member >= Policy.RESERVATION_LIMIT_MEMBER){
            throw new Exception("회원님은 예약가능 횟수를 초과했습니다.");
        }
        if(reservationCnt_book>=Policy.RESERVATION_LIMIT_BOOK){
            throw new Exception("이 책은 현재 3명의 회원이 예약했습니다. 더이상 예약이 불가능합니다.");
        }

        BookReservation bookReservation = BookReservation.builder()
                .book(book)
                .member(member)
                .remainDay(Policy.RESERVATION_DAY)
                .sequence(reservationCnt_book+1)
                .resTime(LocalDateTime.now())
                .build();
        bookReservationRepository.save(bookReservation);
        return bookReservation.getId();
    }

    // 도서 예약 취소하기
    public void deleteReservation(Long id){
        BookReservation bookReservation = bookReservationRepository.findById(id).get();
        bookReservationRepository.delete(bookReservation);
    }

    // remainday(예약도서 대출가능일) 하루에 한번씩 줄이기. sequence가 1인 예약건에대해서. book이 BOOKED인 것만.
    public void decreaseRemainDay(){
        List<BookReservation> bookReservationList = bookReservationRepository.findAll();

        for(int i=0;i<bookReservationList.size();i++){
            int remainday = bookReservationList.get(i).getRemainDay();
            // 대출가능 순서(sequence)가 1번이고, 현재 책 상태가 대출가능상태(BOOKED)인 경우
            if(bookReservationList.get(i).getSequence()==1 &&
                    bookReservationList.get(i).getBook().getState().toString().equals("BOOKED")){

                bookReservationList.get(i).setRemainDay(remainday-1); // remainday -1 하기
                if(remainday-1==0){ // 만약 remainday가 0인경우
                    deleteReservation(bookReservationList.get(i)); // 해당 예약건 삭제.
                }
            }
        }
    }

    // 도서 예약 삭제
    private void deleteReservation(BookReservation bookReservation){
        bookReservationRepository.delete(bookReservation);
    }
}
