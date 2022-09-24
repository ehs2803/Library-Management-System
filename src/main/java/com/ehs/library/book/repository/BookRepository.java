package com.ehs.library.book.repository;

import com.ehs.library.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom{
    List<Book> findByNameContaining(String keyword);

    @EntityGraph(attributePaths = {"bookImg"})
    Page<Book> findByNameContaining(String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"bookImg"})
    Optional<Book> findById(Long id);

    @EntityGraph(attributePaths = {"bookImg"})
    List<Book> findByIsbn(String isbn);

    int countByIsbn(String isbn);
}
