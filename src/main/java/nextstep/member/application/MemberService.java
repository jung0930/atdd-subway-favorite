package nextstep.member.application;

import nextstep.exception.NotFoundUserException;
import nextstep.member.AuthenticationException;
import nextstep.member.application.request.MemberRequest;
import nextstep.member.application.response.MemberResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(Member.of(request.getEmail(), request.getPassword(), request.getAge()));
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(LoginMember loginMember, Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        if (!member.getMemberId().equals(id) && !loginMember.getEmail().equals(member.getEmail())) {
            throw new AuthenticationException();
        }
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(LoginMember loginMember, Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        if (!member.getMemberId().equals(id) && !loginMember.getEmail().equals(member.getEmail())) {
            throw new AuthenticationException();
        }
        member.update(param.toMember());
    }

    @Transactional
    public void deleteMember(LoginMember loginMember, Long id) {
        Member member = findMemberByEmail(loginMember.getEmail());
        if (!member.getMemberId().equals(id)) {
            throw new AuthenticationException();
        }
        memberRepository.deleteById(id);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(NotFoundUserException::new);
    }

    public MemberResponse findMe(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail())
                .map(it -> MemberResponse.of(it))
                .orElseThrow(RuntimeException::new);
    }

}