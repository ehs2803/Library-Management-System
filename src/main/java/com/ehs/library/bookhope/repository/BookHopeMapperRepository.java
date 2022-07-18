package com.ehs.library.bookhope.repository;


import com.ehs.library.bookhope.dto.BookHopeMapperDto;
import com.ehs.library.bookhope.entity.BookHope;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BookHopeMapperRepository {
    List<BookHopeMapperDto> findProgressBookHope(Long member_id);
}
