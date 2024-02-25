package nextstep.member.ui;

import nextstep.member.application.MemberService;
import nextstep.member.application.request.MemberRequest;
import nextstep.member.application.response.MemberResponse;
import nextstep.member.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponse> findMember(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        MemberResponse member = memberService.findMember(loginMember, id);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id, @RequestBody MemberRequest param) {
        memberService.updateMember(loginMember, id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        memberService.deleteMember(loginMember, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        MemberResponse memberResponse = memberService.findMe(loginMember);
        return ResponseEntity.ok().body(memberResponse);
    }
}
