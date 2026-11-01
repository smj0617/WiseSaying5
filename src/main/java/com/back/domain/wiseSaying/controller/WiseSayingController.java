package com.back.domain.wiseSaying.controller;

import com.back.Rq;
import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.Scanner;

// 입력(Scanner)과 출력(System.out)은 컨트롤러에서만 한다.
public class WiseSayingController {

    private final Scanner sc;
    private final WiseSayingService wiseSayingService;

    public WiseSayingController(Scanner sc, WiseSayingService wiseSayingService) {
        this.sc = sc;
        this.wiseSayingService = wiseSayingService;
    }

    public void actionWrite() {
        System.out.print("명언 : ");
        String sentence = sc.nextLine().trim();
        System.out.print("작가 : ");
        String author = sc.nextLine().trim();

        WiseSaying wiseSaying = wiseSayingService.write(sentence, author);
        System.out.printf("%d번 명언이 등록되었습니다.\n", wiseSaying.getId());
    }

    public void actionList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        wiseSayingService.findForList().forEach(e ->
                System.out.printf("%d / %s / %s\n", e.getId(), e.getAuthor(), e.getSentence()));
    }

    public void actionDelete(Rq rq) {
        int id = rq.getParamAsInt("id", -1);
        if (id == -1) {
            System.out.println("id를 숫자로 입력해주세요.");
            return;
        }

        if (!wiseSayingService.delete(id)) {
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
            return;
        }
        System.out.printf("%d번 명언이 삭제되었습니다.\n", id);
    }

    public void actionModify(Rq rq) {
        int id = rq.getParamAsInt("id", -1);
        if (id == -1) {
            System.out.println("id를 숫자로 입력해주세요.");
            return;
        }

        WiseSaying wiseSaying = wiseSayingService.findById(id).orElse(null);
        if (wiseSaying == null) {
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
            return;
        }

        System.out.printf("명언(기존) : %s\n", wiseSaying.getSentence());
        System.out.print("명언 : ");
        String sentence = sc.nextLine().trim();
        System.out.printf("작가(기존) : %s\n", wiseSaying.getAuthor());
        System.out.print("작가 : ");
        String author = sc.nextLine().trim();

        wiseSayingService.modify(wiseSaying, sentence, author);
    }
}
