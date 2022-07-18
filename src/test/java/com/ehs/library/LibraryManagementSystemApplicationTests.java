package com.ehs.library;

import com.ehs.library.bookhope.dto.BookHopeMapperDto;
import com.ehs.library.bookhope.entity.BookHope;
import com.ehs.library.bookhope.repository.BookHopeMapperRepository;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.util.List;

@SpringBootTest
class LibraryManagementSystemApplicationTests {
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private BookHopeMapperRepository bookHopeMapperRepository;

    @Test
    public void connection_test(){
        try(Connection con = sqlSessionFactory.openSession().getConnection()){
            System.out.println("커넥션 성공");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void mybatis_test(){
        List<BookHopeMapperDto> bookHopeList = bookHopeMapperRepository.findProgressBookHope(3L);
        System.out.println(bookHopeList.size());
        for(int i=0;i<bookHopeList.size();i++){
            System.out.println(bookHopeList.get(i).getBook_hope_id());
        }
    }

    @Test
    void contextLoads() {
    }

}
