package com.ehs.library.book.service;

import com.ehs.library.book.dto.BookFormDto;
import com.ehs.library.book.dto.BookImgDto;
import com.ehs.library.book.entity.Book;
import com.ehs.library.book.entity.BookImg;
import com.ehs.library.book.repository.BookImgRepository;
import com.ehs.library.book.repository.BookRepository;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final BookImgService bookImgService;

    private final BookImgRepository bookImgRepository;

    private final MemberRepository memberRepository;

    public Long saveItem(BookFormDto itemFormDto, MultipartFile itemImgFileList, String email) throws Exception{

        //상품 등록
        Book item = itemFormDto.createBook();
        Member findMember = memberRepository.findByEmail(email);
        item.setMember(findMember);
        bookRepository.save(item);

        //이미지 등록
        BookImg itemImg = new BookImg();
        itemImg.setBook(item);

        bookImgService.saveItemImg(itemImg, itemImgFileList);

        return item.getId();
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



}
