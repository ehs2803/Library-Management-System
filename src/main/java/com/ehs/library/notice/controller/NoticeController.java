package com.ehs.library.notice.controller;

import com.ehs.library.book.dto.BookFormDto;
import com.ehs.library.notice.dto.NoticeAddFormDto;
import com.ehs.library.notice.dto.NoticeDto;
import com.ehs.library.notice.dto.NoticeEditFormDto;
import com.ehs.library.notice.dto.NoticeFormDto;
import com.ehs.library.notice.entity.Notice;
import com.ehs.library.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 공지사항 게시판
    @GetMapping("")
    public String noticeList(Model model){
        List<NoticeFormDto> noticeFormDtoList = noticeService.findAllNoticeList();
        model.addAttribute("noticeList", noticeFormDtoList);
        return "notice/noticeBoard";
    }

    // 특정한 공지사항 선택
    @GetMapping("/detail/{id}")
    public String noticeDetail(@PathVariable Long id, Model model){
        NoticeDto notice = noticeService.noticeDetail(id);

        model.addAttribute("notice", notice);
        return "notice/noticeDetail";
    }

    // 공지사항 수정폼
    @GetMapping("/edit/{id}")
    public String noticeEditForm(@PathVariable Long id, Model model){
        NoticeEditFormDto noticeEditFormDto = noticeService.editFormDto(id);
        model.addAttribute("notice", noticeEditFormDto);
        return "notice/noticeEditForm";
    }

    // 공지사항 수정
    @PostMapping("/edit/{id}")
    public String editNotice(@Valid NoticeEditFormDto noticeEditFormDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "notice/noticeEditForm";
        }

        Long editId = noticeService.editNotice(noticeEditFormDto);

        return "redirect:/notice/detail/"+editId;
    }

    // 공지사항 삭제
    @GetMapping("/delete/{id}")
    public String noticeDelete(@PathVariable Long id){
        noticeService.deleteNotice(id);
        return "notice/noticeBoard";
    }

    // 새로운 공지사항 등록폼
    @GetMapping("/new")
    public String addNoticeForm(Model model){
        model.addAttribute("noticeAddForm", new NoticeAddFormDto());
        return "notice/noticeAddForm";
    }

    // 새로운 공지사항 등록
    @PostMapping("/new")
    public String addNotice(@Valid NoticeAddFormDto noticeAddFormDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "notice/noticeAddForm";
        }

        noticeService.saveNewNotice(noticeAddFormDto);

        return "redirect:/notice";
    }

}
