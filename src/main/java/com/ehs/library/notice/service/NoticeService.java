package com.ehs.library.notice.service;

import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
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

    public Notice saveNewNotice(String title, String content, String email){
        Member findMember = memberRepository.findByEmail(email);
        Notice notice = new Notice();
        notice.setMember(findMember);
        notice.setTitle(title);
        notice.setContent(content);
        notice.setHit(0);

        noticeRepository.save(notice);

        return notice;
    }

    public Notice noticeDetail(Long id){
        Notice notice = noticeRepository.findById(id).get();
        notice.setHit(notice.getHit()+1);
        return notice;
    }

    public void deleteNotice(Long id){
        noticeRepository.deleteById(id);
    }

    public NoticeEditFormDto editFormDto(Long id){
        Notice notice = noticeRepository.findById(id).get();
        NoticeEditFormDto noticeEditFormDto = new NoticeEditFormDto();
        noticeEditFormDto.setId(notice.getId());
        noticeEditFormDto.setTitle(notice.getTitle());
        noticeEditFormDto.setContent(notice.getContent());
        return noticeEditFormDto;
    }

    public Long editNotice(NoticeEditFormDto noticeEditFormDto, String email){
        Notice notice = noticeRepository.findById(noticeEditFormDto.getId()).get();
        notice.setTitle(noticeEditFormDto.getTitle());
        notice.setContent(noticeEditFormDto.getContent());
        Member editMember = memberRepository.findByEmail(email);
        notice.setMember(editMember);

        return notice.getId();
    }

}
