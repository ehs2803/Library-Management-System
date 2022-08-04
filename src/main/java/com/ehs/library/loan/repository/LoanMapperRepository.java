package com.ehs.library.loan.repository;

import com.ehs.library.loan.dto.LoanMapperDto;
import com.ehs.library.loan.entity.Loan;
import com.ehs.library.loan.vo.LoanVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface LoanMapperRepository {
    List<LoanVo> findLoanBookList(Long member_id);
    List<LoanVo> findLoanOverdueList(Long member_id);
}
