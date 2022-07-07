package com.ehs.library.book.repository;

import com.ehs.library.book.constant.BookHopeState;
import com.ehs.library.book.entity.BookHope;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BookHopeRepository extends JpaRepository<BookHope, Long> {
    @EntityGraph(attributePaths = {"user"})
    List<BookHope> findByState(BookHopeState bookHopeState);

    int countBookHopeByState(BookHopeState bookHopeState);

    //@Query("select count(b) from BookHope b where b.state= :state")
    //int countBookHopeByState(@Param("state") String state);
}
