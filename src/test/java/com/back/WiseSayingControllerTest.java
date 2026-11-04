package com.back;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;

// 저장소 초기화: AppTestRunner.run() 이 테스트마다 new App(scanner) 를 만들고,
// App 생성자가 저장소를 새로 만든다. 그래서 어떤 순서로 돌려도 번호는 1번부터 시작한다.
// - CRUD 테스트: 샘플 없이 빈 저장소로 시작한다. (runWithoutSample)
// - 검색·페이징 테스트: 앱 시작 시 샘플 10개(명언 1~10, 작자미상 1~10)가 만들어진다. (AppTestRunner.run)
class WiseSayingControllerTest {

    private static String runWithoutSample(String input) {
        return AppTestRunner.run(input, scanner -> new App(scanner, false).run());
    }

    @Test
    @DisplayName("종료")
    void t1() {
        String out = runWithoutSample("""
                종료
                """);
        assertThat(out).contains("== 명언 앱 ==", "프로그램을 종료합니다.");
    }

    @Test
    @DisplayName("등록")
    void t3() {
        String out = runWithoutSample("""
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
        String out = runWithoutSample("""
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
        String out = runWithoutSample("""
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
        String out = runWithoutSample("""
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
        String out = runWithoutSample("""
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
        String out = runWithoutSample("""
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
        String out = runWithoutSample("""
                수정?id=3
                종료
                """);
        assertThat(out)
                .contains("3번 명언은 존재하지 않습니다.")
                .doesNotContain("명언(기존) :");
    }

    // ── 13단계: 검색 ──

    @Test
    @DisplayName("목록?keywordType=content&keyword=과거")
    void t10() {
        String out = runWithoutSample("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                목록?keywordType=content&keyword=과거
                종료
                """);
        assertThat(out)
                .containsSubsequence(
                        "----------------------",
                        "검색타입 : content",
                        "검색어 : 과거",
                        "----------------------",
                        "번호 / 작가 / 명언",
                        "----------------------",
                        "2 / 작자미상 / 과거에 집착하지 마라."
                )
                .doesNotContain("1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    @DisplayName("목록?keywordType=author&keyword=작자")
    void t11() {
        String out = runWithoutSample("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                홍길동
                목록?keywordType=author&keyword=작자
                종료
                """);
        assertThat(out)
                .contains("검색타입 : author", "검색어 : 작자", "1 / 작자미상 / 현재를 사랑하라.")
                .doesNotContain("2 / 홍길동 / 과거에 집착하지 마라.");
    }

    // ── 14단계: 페이징 ──

    @Test
    @DisplayName("샘플 10개: 목록은 1페이지(10~6번)를 최신순으로 보여준다")
    void t12() {
        String out = AppTestRunner.run("""
                목록
                종료
                """);
        assertThat(out)
                .containsSubsequence(
                        "10 / 작자미상 10 / 명언 10",
                        "9 / 작자미상 9 / 명언 9",
                        "8 / 작자미상 8 / 명언 8",
                        "7 / 작자미상 7 / 명언 7",
                        "6 / 작자미상 6 / 명언 6",
                        "페이지 : [1] / 2"
                )
                .doesNotContain("5 / 작자미상 5 / 명언 5");
    }

    @Test
    @DisplayName("샘플 10개: 목록?page=2 는 5~1번")
    void t13() {
        String out = AppTestRunner.run("""
                목록?page=2
                종료
                """);
        assertThat(out)
                .containsSubsequence(
                        "5 / 작자미상 5 / 명언 5",
                        "4 / 작자미상 4 / 명언 4",
                        "3 / 작자미상 3 / 명언 3",
                        "2 / 작자미상 2 / 명언 2",
                        "1 / 작자미상 1 / 명언 1",
                        "페이지 : 1 / [2]"
                )
                .doesNotContain("6 / 작자미상 6 / 명언 6");
    }

    @Test
    @DisplayName("샘플 10개: 내용에 1 검색 → 10번·1번, 1페이지")
    void t14() {
        String out = AppTestRunner.run("""
                목록?keywordType=content&keyword=1
                종료
                """);
        assertThat(out)
                .contains("검색타입 : content", "검색어 : 1", "페이지 : [1]")
                .containsSubsequence(
                        "10 / 작자미상 10 / 명언 10",
                        "1 / 작자미상 1 / 명언 1"
                )
                .doesNotContain("2 / 작자미상 2 / 명언 2", "9 / 작자미상 9 / 명언 9", "[1] / 2");
    }

    @Test
    @DisplayName("검색 결과를 기준으로 페이징한다")
    void t15() {
        String out = AppTestRunner.run("""
                등록
                과거에 집착하지 마라.
                작자미상
                목록?keywordType=content&keyword=명언&page=2
                종료
                """);
        // 명언 1~10 만 검색되고(11번은 제외) 2페이지는 5~1번
        assertThat(out)
                .containsSubsequence(
                        "5 / 작자미상 5 / 명언 5",
                        "1 / 작자미상 1 / 명언 1",
                        "페이지 : 1 / [2]"
                )
                .doesNotContain("6 / 작자미상 6 / 명언 6", "11 / 작자미상 / 과거에 집착하지 마라.");
    }
}
