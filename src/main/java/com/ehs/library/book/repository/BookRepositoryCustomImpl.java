package com.ehs.library.book.repository;

import com.ehs.library.book.dto.BookDto;
import com.ehs.library.book.dto.BookSearchCondition;
import com.ehs.library.book.entity.Book;
import com.ehs.library.book.entity.QBook;
import com.ehs.library.book.entity.QBookImg;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.isEmpty;

public class BookRepositoryCustomImpl implements BookRepositoryCustom{
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Book> searchBook(BookSearchCondition condition){
        return jpaQueryFactory
                .selectFrom(QBook.book)
                .join(QBook.book.bookImg, QBookImg.bookImg).fetchJoin()
                .where(bookNameContains(condition.getName()),
                        bookIsbnEq(condition.getIsbn()),
                        bookAuthorEq(condition.getAuthor()),
                        bookPublisherEq(condition.getPublisher()),
                        bookYearEq(condition.getYear()))
                .orderBy(QBook.book.loanCnt.desc(), QBook.book.year.desc())
                .fetch();
    }

    @Override
    public Page<Book> searchBookPageSimple(BookSearchCondition condition, Pageable pageable){
        QueryResults<Book> results = jpaQueryFactory
                .selectFrom(QBook.book)
                .join(QBook.book.bookImg, QBookImg.bookImg).fetchJoin()
                .where(bookNameContains(condition.getName()),
                        bookIsbnEq(condition.getIsbn()),
                        bookAuthorEq(condition.getAuthor()),
                        bookPublisherEq(condition.getPublisher()),
                        bookYearEq(condition.getYear()))
                .orderBy(QBook.book.loanCnt.desc(), QBook.book.year.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();


        List<Book> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression bookNameContains(String name) {
        return isEmpty(name) ? null : QBook.book.name.contains(name);
    }

    private BooleanExpression bookIsbnEq(String isbn) {
        return isEmpty(isbn) ? null : QBook.book.isbn.eq(isbn);
    }

    private BooleanExpression bookAuthorEq(String author){
        return isEmpty(author) ? null : QBook.book.author.eq(author);
    }

    private BooleanExpression bookPublisherEq(String publisher){
        return isEmpty(publisher) ? null : QBook.book.publisher.eq(publisher);
    }

    private BooleanExpression bookYearEq(Integer year){
        return year==null ? null : QBook.book.year.eq(year);
    }

}
