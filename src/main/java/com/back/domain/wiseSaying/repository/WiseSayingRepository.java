package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.standard.dto.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// 데이터 저장·조회·삭제만 한다. 콘솔 입출력 없음.
public class WiseSayingRepository {

    private final List<WiseSaying> wiseSayings = new ArrayList<>();
    private int lastId = 0;

    public WiseSaying save(WiseSaying wiseSaying) {
        if (wiseSaying.isNew()) {
            wiseSaying.setId(++lastId);
            wiseSayings.add(wiseSaying);
        }
        return wiseSaying;
    }

    // 최신순 (나중에 등록한 것이 먼저)
    public List<WiseSaying> findForList() {
        return wiseSayings.reversed();
    }

    // 검색 조건에 맞는 것만 최신순으로 골라 page 번째 묶음(pageSize 개)을 돌려준다.
    // keyword 가 비어 있으면 전체가 대상이다.
    public Page<WiseSaying> findForList(String keywordType, String keyword, int page, int pageSize) {
        List<WiseSaying> filtered = wiseSayings.reversed()
                .stream()
                .filter(e -> matches(e, keywordType, keyword))
                .toList();

        List<WiseSaying> content = filtered
                .stream()
                .skip((long) (page - 1) * pageSize)
                .limit(pageSize)
                .toList();

        return new Page<>(content, page, pageSize, filtered.size());
    }

    private boolean matches(WiseSaying wiseSaying, String keywordType, String keyword) {
        if (keyword == null || keyword.isBlank()) return true;

        return switch (keywordType) {
            case "author" -> wiseSaying.getAuthor().contains(keyword);
            default -> wiseSaying.getSentence().contains(keyword);
        };
    }

    public Optional<WiseSaying> findById(int id) {
        return wiseSayings
                .stream()
                .filter(e -> e.getId() == id)
                .findFirst();
    }

    public boolean delete(int id) {
        return wiseSayings.removeIf(e -> e.getId() == id);
    }

    public int count() {
        return wiseSayings.size();
    }
}
