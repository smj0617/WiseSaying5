package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;
import com.back.standard.dto.Page;

import java.util.List;
import java.util.Optional;

// 순수 비즈니스 로직. 콘솔 입출력 없음.
public class WiseSayingService {

    private final WiseSayingRepository wiseSayingRepository;

    public WiseSayingService(WiseSayingRepository wiseSayingRepository) {
        this.wiseSayingRepository = wiseSayingRepository;
    }

    public WiseSaying write(String sentence, String author) {
        return wiseSayingRepository.save(new WiseSaying(sentence, author));
    }

    public List<WiseSaying> findForList() {
        return wiseSayingRepository.findForList();
    }

    public Page<WiseSaying> findForList(String keywordType, String keyword, int page, int pageSize) {
        return wiseSayingRepository.findForList(keywordType, keyword, page, pageSize);
    }

    public Optional<WiseSaying> findById(int id) {
        return wiseSayingRepository.findById(id);
    }

    public boolean delete(int id) {
        return wiseSayingRepository.delete(id);
    }

    public void modify(WiseSaying wiseSaying, String sentence, String author) {
        wiseSaying.setSentence(sentence);
        wiseSaying.setAuthor(author);
        wiseSayingRepository.save(wiseSaying);
    }

    // 명언이 하나도 없을 때만 샘플(명언 1 ~ 명언 count, 작자미상 1 ~ 작자미상 count)을 만든다.
    public void makeSampleDataIfEmpty(int count) {
        if (wiseSayingRepository.count() > 0) return;

        for (int i = 1; i <= count; i++) {
            write("명언 " + i, "작자미상 " + i);
        }
    }
}
