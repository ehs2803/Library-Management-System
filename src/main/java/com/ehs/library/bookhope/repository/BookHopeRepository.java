package com.ehs.library.bookhope.repository;

import com.ehs.library.book.entity.Book;
import com.ehs.library.bookhope.constant.BookHopeState;
import com.ehs.library.bookhope.entity.BookHope;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BookHopeRepository extends JpaRepository<BookHope, Long> {
    @EntityGraph(attributePaths = {"user"})
    List<BookHope> findByState(BookHopeState bookHopeState);

    int countBookHopeByState(BookHopeState bookHopeState);

    @Query("select b from BookHope b join fetch b.user u where b.state= :state and u.id= :id")
    List<BookHope> findByIdAndState(@Param("id") Long id, @Param("state") BookHopeState state);

//    @Query("select b from BookHope b join fetch b.user u where u.id= :id and b.state!= :'REJECT'")
//    List<BookHope> findByIdAndProgress(@Param("id") Long id);

    //List<BookHope> findByIdAndState(Long id, BookHopeState bookHopeState);

    //@Query("select count(b) from BookHope b where b.state= :state")
    //int countBookHopeByState(@Param("state") String state);
}
