package com.back.standard.dto;

import java.util.List;

// 한 페이지에 보여 줄 항목과 페이지 정보를 함께 담는다.
public record Page<T>(List<T> content, int page, int pageSize, int totalCount) {

    // 항목이 0개여도 1페이지는 있다고 본다.
    public int totalPages() {
        return Math.max(1, (int) Math.ceil((double) totalCount / pageSize));
    }
}
