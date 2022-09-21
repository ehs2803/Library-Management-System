package com.ehs.library.book.service;

import com.ehs.library.book.constant.BookState;
import com.ehs.library.book.dto.*;
import com.ehs.library.book.entity.Book;
import com.ehs.library.book.entity.BookImg;
import com.ehs.library.book.repository.BookImgRepository;
import com.ehs.library.book.repository.BookRepository;
import com.ehs.library.loan.constant.LoanState;
import com.ehs.library.loan.entity.Loan;
import com.ehs.library.loan.repository.LoanRepository;
import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookImgService bookImgService;
    private final BookImgRepository bookImgRepository;
    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;

    // 새로운 책 등록
    public Long saveItem(BookFormDto itemFormDto, MultipartFile itemImgFileList, String email) throws Exception{
        Member findMember = memberRepository.findByEmail(email);
        Book item = Book.builder()
                .name(itemFormDto.getName())
                .isbn(itemFormDto.getIsbn())
                .author(itemFormDto.getAuthor())
                .publisher(itemFormDto.getPublisher())
                .year(itemFormDto.getYear())
                .price(itemFormDto.getPrice())
                .page(itemFormDto.getPage())
                .content(itemFormDto.getContent())
                .register_numer(itemFormDto.getRegister_numer())
                .symbol(itemFormDto.getSymbol())
                .classification(itemFormDto.getClassification())
                .classification_detail(itemFormDto.getClassification_detail())
                .location(itemFormDto.getLocation())
                .loanCnt(0)
                .member(findMember)
                .state(BookState.AVAILABLE)
                .build();

        bookRepository.save(item);

        //이미지 등록
        BookImg itemImg = new BookImg();
        itemImg.setBook(item);
        bookImgService.saveItemImg(itemImg, itemImgFileList);

        return item.getId();
    }

    // 검색 v1
    @Transactional(readOnly = true)
    public Page<Book> searchBookList(String keyword, Pageable pageable) {
        return bookRepository.findByNameContaining(keyword, pageable);
    }

    // 검색 v2
    @Transactional(readOnly = true)
    public List<Book> searchBookCondition(BookSearchCondition condition){
        return bookRepository.searchBook(condition);
    }

    // 검색 v3
    @Transactional(readOnly = true)
    public Page<Book> searchBookConditionPage(BookSearchCondition condition, Pageable pageable){
        return bookRepository.searchBookPageSimple(condition, pageable);
    }

    // 도서 id 기반 검색 -> BookDto 변환
    @Transactional(readOnly = true)
    public BookDto findBookById(Long id){
        Book book = bookRepository.findById(id).get();
        String content = book.getContent().replace("\r\n","<br>");

        BookDto bookDto = BookDto.builder()
                .name(book.getName())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .year(book.getYear())
                .symbol(book.getSymbol())
                .state(book.getState().toString())
                .location(book.getLocation())
                .bookImg(new BookImgSimpleDto(book.getBookImg().getImgUrl()))
                .isbn(book.getIsbn())
                .content(content)
                .build();

        return bookDto;
    }

    // 도서 isbn 기반 조회
    @Transactional(readOnly = true)
    public List<Book> findBookbyISBN(String isbn){
        return bookRepository.findByIsbn(isbn);
    }

    // 도서 반납예정일 dto에 입력
    public void setReturnDay(List<BookListDto> bookList){
        for(int i=0;i<bookList.size();i++){
            Loan loan = loanRepository.findByBookAndLoanState(bookRepository.findById(bookList.get(i).getId()).get(),LoanState.LOAN);
            if(loan!=null){
                bookList.get(i).setReturn_day(LocalDate.now().plus(loan.getRemainDay(), ChronoUnit.DAYS));
            }
        }
    }

    @Transactional(readOnly = true)
    public BookFormDto getItemDtl(Long itemId){
        BookImg itemImgList = bookImgRepository.findById(itemId).get();
        BookImgDto itemImgDtoList = BookImgDto.of(itemImgList);

        Book item = bookRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        BookFormDto itemFormDto = BookFormDto.of(item);
        itemFormDto.setBookImgDto(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(BookFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        //상품 수정
        Book item = bookRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateBook(itemFormDto);
        Long itemImgId = itemFormDto.getBookImgId();

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            bookImgService.updateItemImg(itemImgId, itemImgFileList.get(i));
        }

        return item.getId();
    }

    // 책 이름 키워드로 도서 검색(json 반환)
    public String findByNameContainingRetrunJson(String keyword){
        List<Book> bookList = bookRepository.findByNameContaining(keyword);

        JsonObject jo = new JsonObject();
        if(bookList.size()>0) jo.addProperty("status", "YES");
        else jo.addProperty("status", "NO");

        JsonArray ja = new JsonArray();
        for (Book book : bookList) {
            JsonObject jObj = new JsonObject();
            jObj.addProperty("id", book.getId());
            jObj.addProperty("name", book.getName());
            jObj.addProperty("isbn", book.getIsbn());
            jObj.addProperty("author", book.getAuthor());
            jObj.addProperty("publisher", book.getPublisher());
            jObj.addProperty("register_numer", book.getRegister_numer());
            jObj.addProperty("symbol", book.getSymbol());
            jObj.addProperty("state", book.getState().toString());
            ja.add(jObj);
        }
        jo.add("books", ja);

        return jo.toString();
    }

}
