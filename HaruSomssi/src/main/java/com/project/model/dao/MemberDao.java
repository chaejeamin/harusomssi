package com.project.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.project.model.vo.MemberVO;

@Mapper
public interface MemberDao {
	// 쿼리 매핑해줄 때 쓸거임 ex) member. 뒤에 xml에 있는 쿼리 id
	String NAMESPACE = "member.";
	
	// 회원 가입-동림테스트
	public void register(MemberVO vo);
	
	// 아이디 유효성 검사
	public MemberVO checkId(String member_id);
	
	//아이디 중복 확인-동림테스트
	public int idChk(MemberVO vo) throws Exception;
	
	// 로그인
	public MemberVO login(MemberVO vo);
	
	// 회원 정보 조회
	public MemberVO selectOne(String member_id);
	
	// 카카오 로그인 api - 카카오이메일, DB이메일 확인
	public MemberVO kakaoEmailChk(String kakaoEmail);
}
