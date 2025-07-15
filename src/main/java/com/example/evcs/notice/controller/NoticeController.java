package com.example.evcs.notice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.evcs.exception.NoticeNotFoundException;
import com.example.evcs.notice.model.dto.NoticeDto;
import com.example.evcs.notice.model.service.NoticeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping
    public List<NoticeDto> getAllNotices() {
        List<NoticeDto> notices = noticeService.getAllNotices();
        System.out.println("조회된 공지사항 목록: " + notices);
        return notices;
    }


    @GetMapping("/{id}")
    public NoticeDto getNoticeById(@PathVariable("id") Long id) {
        return noticeService.getNoticeById(id)
                .orElseThrow(() -> new NoticeNotFoundException("Notice not found with ID: " + id));
    }

    @PostMapping
    public ResponseEntity<Void> createNotice(@RequestBody NoticeDto dto) {
        noticeService.createNotice(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public NoticeDto updateNotice(@PathVariable("id") Long id, @RequestBody NoticeDto dto) {
        return noticeService.updateNotice(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotice(@PathVariable("id") Long id) {
        try {
            noticeService.deleteNotice(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("공지사항 삭제 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("삭제 실패: " + e.getMessage());
        }
    }

}

