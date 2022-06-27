package com.ehs.library.book.repository;

import com.ehs.library.book.dto.BookImgDto;
import com.ehs.library.book.entity.BookImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookImgRepository extends JpaRepository<BookImg, Long> {

}
