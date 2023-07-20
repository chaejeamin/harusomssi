package com.project.model.service;


import java.util.HashMap;

import com.project.model.vo.MemberVO;


public interface MemberService {
//	//db 연결테스트
//	public List<MemberVO> selectList();
	
	// 회원 가입-동림테스트
	public void register(MemberVO vo) throws Exception;
	
	// 아이디 유효성 검사
	public MemberVO checkId(String member_id);
	
	//아이디 중복 확인-동림테스트
	public int idChk(MemberVO vo) throws Exception;
	
	// 로그인
	public MemberVO login(MemberVO vo);
	
	// 회원 정보 조회
	public MemberVO selectOne(String member_id);
	
	// 카카오톡 로그인 api - code값을 이용해 토큰 가져오기
	public String getAccessToken(String authorize_code) throws Throwable;

	// 카카오톡 로그인 api - 토큰을 받은 후, 카카오에서 가져올 수 있는 정보를 가져옴
	public HashMap<String, Object> getUserInfo(String access_Token) throws Throwable;

	// 카카오톡 로그인 api - 카카오톡이메일, DB이메일 체크
	public MemberVO kakaoEmailChk(String kakaoEmail);
	
	// 카카오톡 로그인 api - 로그아웃
	public void kakaologout(String access_token);
	
	// 카카오톡 로그인 api - 연결끊기(회원탈퇴)
	public void kakaounlink(String attribute);
}
