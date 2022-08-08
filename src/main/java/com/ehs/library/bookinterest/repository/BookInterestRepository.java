package com.ehs.library.bookinterest.repository;

import com.ehs.library.bookinterest.entity.BookInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookInterestRepository extends JpaRepository<BookInterest, Long> {

}
