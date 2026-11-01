package com.back;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;

// 저장소 초기화: AppTestRunner.run() 이 테스트마다 new App(scanner) 를 만들고,
// App 생성자가 저장소를 새로 만든다. 그래서 어떤 순서로 돌려도 번호는 1번부터 시작한다.
class WiseSayingControllerTest {
    @Test
    @DisplayName("종료")
    void t1() {
        String out = AppTestRunner.run("""
                종료
                """);
        assertThat(out).contains("== 명언 앱 ==", "프로그램을 종료합니다.");
    }

    @Test
    @DisplayName("등록")
    void t3() {
        // App must create a new empty repository for this test.
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                종료
                """);
        assertThat(out).contains("명언 :", "작가 :", "1번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("등록을 두 번 하면 번호가 1씩 늘어난다")
    void t4() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                종료
                """);
        assertThat(out).contains("1번 명언이 등록되었습니다.", "2번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("목록: 최신순으로 보여준다")
    void t5() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                목록
                종료
                """);
        assertThat(out)
                .contains("번호 / 작가 / 명언", "----------------------")
                .containsSubsequence(
                        "2 / 작자미상 / 과거에 집착하지 마라.",
                        "1 / 작자미상 / 현재를 사랑하라."
                );
    }

    @Test
    @DisplayName("삭제?id=1")
    void t6() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                삭제?id=1
                목록
                종료
                """);
        assertThat(out)
                .contains("1번 명언이 삭제되었습니다.", "2 / 작자미상 / 과거에 집착하지 마라.")
                .doesNotContain("1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    @DisplayName("없는 번호 삭제")
    void t7() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                삭제?id=1
                삭제?id=1
                종료
                """);
        assertThat(out).contains("1번 명언이 삭제되었습니다.", "1번 명언은 존재하지 않습니다.");
    }

    @Test
    @DisplayName("수정?id=1")
    void t8() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                수정?id=1
                현재와 자신을 사랑하라.
                홍길동
                목록
                종료
                """);
        assertThat(out)
                .contains(
                        "명언(기존) : 현재를 사랑하라.",
                        "작가(기존) : 작자미상",
                        "1 / 홍길동 / 현재와 자신을 사랑하라."
                )
                .doesNotContain("1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    @DisplayName("없는 번호 수정")
    void t9() {
        String out = AppTestRunner.run("""
                수정?id=3
                종료
                """);
        assertThat(out)
                .contains("3번 명언은 존재하지 않습니다.")
                .doesNotContain("명언(기존) :");
    }
}
