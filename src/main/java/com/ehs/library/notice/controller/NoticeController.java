package com.ehs.library.notice.controller;

import com.ehs.library.book.dto.BookFormDto;
import com.ehs.library.notice.dto.NoticeAddFormDto;
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

    @GetMapping("")
    public String noticeList(Model model){
        List<NoticeFormDto> noticeFormDtoList = noticeService.findAllNoticeList();
        model.addAttribute("noticeList", noticeFormDtoList);
        return "notice/noticeBoard";
    }

    @GetMapping("/detail/{id}")
    public String noticeDetail(@PathVariable Long id, Model model){
        Notice notice = noticeService.noticeDetail(id);
        model.addAttribute("notice", notice);
        return "notice/noticeDetail";
    }

    @GetMapping("/edit/{id}")
    public String noticeEditForm(@PathVariable Long id, Model model){
        NoticeEditFormDto noticeEditFormDto = noticeService.editFormDto(id);
        model.addAttribute("notice", noticeEditFormDto);
        return "notice/noticeEditForm";
    }

    @PostMapping("/edit/{id}")
    public String editNotice(@Valid NoticeEditFormDto noticeEditFormDto, BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()) {
            return "notice/noticeEditForm";
        }
        String email = principal.getName();
        Long editId = noticeService.editNotice(noticeEditFormDto, email);

        return "redirect:/notice/detail/"+editId;
    }

    @GetMapping("/delete/{id}")
    public String noticeDelete(@PathVariable Long id){
        noticeService.deleteNotice(id);
        return "notice/noticeBoard";
    }

    @GetMapping("/new")
    public String addNoticeForm(Model model){
        model.addAttribute("noticeAddForm", new NoticeAddFormDto());
        return "notice/noticeAddForm";
    }

    @PostMapping("/new")
    public String addNotice(@Valid NoticeAddFormDto noticeAddFormDto, BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()) {
            return "notice/noticeAddForm";
        }
        String email = principal.getName();
        noticeService.saveNewNotice(noticeAddFormDto.getTitle(), noticeAddFormDto.getContent(), email);

        return "redirect:/notice";
    }

    // edit, delete
}
