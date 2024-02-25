package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.request.MemberRequest;
import nextstep.member.application.response.MemberResponse;
import org.springframework.http.MediaType;

public class MemberSteps {

    public static ExtractableResponse<Response> 회원_생성_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = MemberRequest.of(email, password, age);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all().extract();
    }

    public static MemberResponse 회원_생성됨(String email, String password, Integer age) {
        return 회원_생성_요청(email, password, age).as(MemberResponse.class);
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(String accessToken, Long id) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", id)
                .when().get("/members/{id}")
                .then().log().all()
                .extract();
    }

    public static MemberResponse 회원_정보_조회됨(String accessToken, Long id) {
        return 회원_정보_조회_요청(accessToken, id).as(MemberResponse.class);
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(String accessToken, Long memberId, String email, String password, Integer age) {
        MemberRequest memberRequest = MemberRequest.of(email, password, age);

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .pathParam("id", memberId)
                .when().put("/members/{id}")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(Long memberId, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .pathParam("id", memberId)
                .when().delete("/members/{id}")
                .then().log().all().extract();
    }

}