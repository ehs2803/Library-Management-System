package com.ehs.library.book.repository;

import com.ehs.library.book.constant.BookHopeState;
import com.ehs.library.book.entity.BookHope;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BookHopeRepository extends JpaRepository<BookHope, Long> {
    @EntityGraph(attributePaths = {"user"})
    List<BookHope> findByState(BookHopeState bookHopeState);
}
