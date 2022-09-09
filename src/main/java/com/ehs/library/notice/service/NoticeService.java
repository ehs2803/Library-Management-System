package com.ehs.library.notice.service;

import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import com.ehs.library.notice.dto.NoticeAddFormDto;
import com.ehs.library.notice.dto.NoticeDto;
import com.ehs.library.notice.dto.NoticeEditFormDto;
import com.ehs.library.notice.dto.NoticeFormDto;
import com.ehs.library.notice.entity.Notice;
import com.ehs.library.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    // 모든 공지사항 조회
    @Transactional(readOnly = true)
    public List<NoticeFormDto> findAllNoticeList(){
        List<Notice> notices= noticeRepository.findAll();
        List<NoticeFormDto> noticeFormDtoList = new ArrayList<>();

        for(Notice notice : notices){
            NoticeFormDto noticeFormDto = NoticeFormDto.builder()
                    .id(notice.getId())
                    .title(notice.getTitle())
                    .content(notice.getContent())
                    .hit(notice.getHit())
                    .createTime(notice.getRegTime())
                    .build();
            noticeFormDtoList.add(noticeFormDto);
        }

        return noticeFormDtoList;
    }

    // 특정 공지사항 조회
    public NoticeDto noticeDetail(Long id){
        Notice notice = noticeRepository.findById(id).get();

        notice.setHit(notice.getHit()+1); // 조회수 1 증가
        String content = notice.getContent().replace("\r\n","<br>"); // 컨텐츠 내용 줄바꿈 가능하게하기...

        NoticeDto noticeDto = NoticeDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .hit(notice.getHit())
                .content(content)
                .build();

        return noticeDto;
    }

    // 공지사항 수정
    public Long editNotice(NoticeEditFormDto noticeEditFormDto){
        Notice notice = noticeRepository.findById(noticeEditFormDto.getId()).get();
        notice.setTitle(noticeEditFormDto.getTitle());
        notice.setContent(noticeEditFormDto.getContent());

        return notice.getId();
    }

    // 공지사항 삭제
    public void deleteNotice(Long id){
        noticeRepository.deleteById(id);
    }

    // 공지사항 등록
    public Notice saveNewNotice(NoticeAddFormDto noticeAddFormDto){
        Notice notice = Notice.builder()
                .title(noticeAddFormDto.getTitle())
                .content(noticeAddFormDto.getContent())
                .hit(0).build();

        noticeRepository.save(notice);

        return notice;
    }

    // 수정하기 기능을 위한 db에 저장된 정보 불러오기
    @Transactional(readOnly = true)
    public NoticeEditFormDto editFormDto(Long id){
        Notice notice = noticeRepository.findById(id).get();

        NoticeEditFormDto noticeEditFormDto = NoticeEditFormDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .build();

        return noticeEditFormDto;
    }


}
