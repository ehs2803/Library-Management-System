package com.ehs.library.base.constant;

public class Policy {
    public final static int LOAN_BOOK_CNT=5; // 일반 유저가 빌릴 수 있는 책의 최대 개수
    public final static int LOAN_BOOK_DAY=14; // 일반 유저의 책 대출 기간
    public final static int LOAN_BOOK_EXTENSION_CNT=1; // 일반 유저의 한 책에 대한 대출연기 가능 횟수
    public final static int LOAN_BOOK_EXTENSION_DAY=7; // 일반 유저의 한 책에 대한 대출 연기 일수

    public final static int RESERVATION_LIMIT_BOOK = 3; // 도서는 최대 3명의 회원이 예약할 수 있음
    public final static int RESERVATION_LIMIT_MEMBER = 3; // 회원은 최대 3개의 도서를 예약할 수 있음
    public final static int RESERVATION_DAY = 3;
}
