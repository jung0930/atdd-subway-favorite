package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import nextstep.member.application.response.MemberResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.common.Constant.*;
import static nextstep.member.acceptance.AuthAcceptanceStep.로그인_성공;
import static nextstep.member.acceptance.AuthAcceptanceStep.사용자_조회됨;
import static nextstep.member.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        ExtractableResponse 회원_생성_응답 = 회원_생성_요청(임꺽정_이메일, 임꺽정_비밀번호, 임꺽정_나이);

        // then
        assertThat(회원_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("본인의 회원정보를 조회한다.")
    @Test
    void 본인의_회원정보를_조회() {
        // given
        MemberResponse 임꺽정 = 회원_생성됨(임꺽정_이메일, 임꺽정_비밀번호, 임꺽정_나이);
        String 임꺽정_토큰 = 로그인_성공(임꺽정_이메일, 임꺽정_비밀번호);

        // when
        MemberResponse 회원_정보_조회_응답 = 회원_정보_조회됨(임꺽정_토큰, 임꺽정.getId());

        // then
        assertThat(회원_정보_조회_응답.getId()).isNotNull();
        assertThat(회원_정보_조회_응답.getEmail()).isEqualTo(임꺽정_이메일);
        assertThat(회원_정보_조회_응답.getAge()).isEqualTo(임꺽정_나이);
    }

    @DisplayName("본인의 회원정보를 수정한다.")
    @Test
    void 본인의_회원정보를_수정() {
        // given
        MemberResponse 임꺽정 = 회원_생성됨(임꺽정_이메일, 임꺽정_비밀번호, 임꺽정_나이);
        String 임꺽정_토큰 = 로그인_성공(임꺽정_이메일, 임꺽정_비밀번호);

        // when
        ExtractableResponse 회원_정보_수정_응답 = 회원_정보_수정_요청(임꺽정_토큰, 임꺽정.getId(), "new" + 임꺽정_이메일, "new" + 임꺽정_비밀번호, 임꺽정_나이 + 5);
        MemberResponse 회원_정보_조회_응답 = 회원_정보_조회됨(임꺽정_토큰, 임꺽정.getId());

        // then
        assertThat(회원_정보_수정_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(회원_정보_조회_응답.getEmail()).isEqualTo("new" + 임꺽정_이메일);
        assertThat(회원_정보_조회_응답.getAge()).isEqualTo(임꺽정_나이 + 5);
    }

    @DisplayName("본인의 회원정보를 삭제한다.")
    @Test
    void 본인의_회원정보를_삭제() {
        // given
        MemberResponse 임꺽정 = 회원_생성됨(임꺽정_이메일, 임꺽정_비밀번호, 임꺽정_나이);
        String 임꺽정_토큰 = 로그인_성공(임꺽정_이메일, 임꺽정_비밀번호);

        // when
        var response = 회원_삭제_요청(임꺽정.getId(), 임꺽정_토큰);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * When 다른 사람의 정보를 삭제 시도하면
     * Then 삭제되지 않는다.
     */
    @DisplayName("다른 사람의 회원정보를 삭제 시도하면 삭제되지 않는다.")
    @Test
    void 다른_사용자의_회원정보_삭제_실패() {
        // given
        MemberResponse 임꺽정 = 회원_생성됨(임꺽정_이메일, 임꺽정_비밀번호, 임꺽정_나이);
        MemberResponse 홍길동 = 회원_생성됨(홍길동_이메일, 홍길동_비밀번호, 홍길동_나이);
        String 임꺽정_토큰 = 로그인_성공(임꺽정_이메일, 임꺽정_비밀번호);

        // when & then
        ExtractableResponse 회원_삭제_응답 = 회원_삭제_요청(홍길동.getId(), 임꺽정_토큰);
        assertThat(회원_삭제_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * When 토큰을 통해 내 정보를 조회하면
     * Then 내 정보를 조회할 수 있다
     */
    @DisplayName("내 정보를 조회한다.")
    @Test
    void 내_정보_조회_성공() {
        // given
        회원_생성_요청(임꺽정_이메일, 임꺽정_비밀번호, 임꺽정_나이);
        String accessToken = 로그인_성공(임꺽정_이메일, 임꺽정_비밀번호);

        // thwn
        MemberResponse 사용자_조회_응답 = 사용자_조회됨(accessToken);
        String 사용자_이메일 = 사용자_조회_응답.getEmail();

        // then
        assertThat(사용자_이메일).isEqualTo(임꺽정_이메일);
    }

}
