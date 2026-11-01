package com.back;

import com.back.domain.system.controller.SystemController;
import com.back.domain.wiseSaying.controller.WiseSayingController;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.Scanner;

public class App {

    private final Scanner sc;
    private final SystemController systemController;
    private final WiseSayingController wiseSayingController;

    // App 을 만들 때마다 저장소를 새로 만든다.
    // → 테스트마다 new App(...) 을 하니, 목록은 비어 있고 번호는 1번부터 다시 시작한다.
    public App(Scanner sc) {
        this.sc = sc;

        WiseSayingRepository wiseSayingRepository = new WiseSayingRepository();
        WiseSayingService wiseSayingService = new WiseSayingService(wiseSayingRepository);

        this.systemController = new SystemController();
        this.wiseSayingController = new WiseSayingController(sc, wiseSayingService);
    }

    public void run() {
        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            Rq rq = new Rq(sc.nextLine());

            switch (rq.getActionName()) {
                case "종료" -> {
                    systemController.actionExit();
                    return;
                }
                case "등록" -> wiseSayingController.actionWrite();
                case "목록" -> wiseSayingController.actionList();
                case "삭제" -> wiseSayingController.actionDelete(rq);
                case "수정" -> wiseSayingController.actionModify(rq);
                default -> System.out.println("알 수 없는 명령입니다.");
            }
        }
    }
}
