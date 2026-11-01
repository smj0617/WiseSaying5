package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

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

    public Optional<WiseSaying> findById(int id) {
        return wiseSayings
                .stream()
                .filter(e -> e.getId() == id)
                .findFirst();
    }

    public boolean delete(int id) {
        return wiseSayings.removeIf(e -> e.getId() == id);
    }
}
