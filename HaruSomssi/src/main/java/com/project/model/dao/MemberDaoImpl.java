package com.project.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.project.model.vo.MemberVO;

// 클래스로 표시함
// @Repository 어노테이션이 부여된 클래스는 데이터 액세스 계층의 구현체로 사용되며, 스프링에서 일관된 방식으로 데이터베이스 액세스를 처리할 수 있도록 도와줌
@Repository 
@Primary
public class MemberDaoImpl implements MemberDao{
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	
	//동림테스트
	@Override
	public void register(MemberVO vo) {
		sqlSession.insert("insert", vo);
	}
	
	
	
	@Override
	public MemberVO checkId(String member_id) {
		MemberVO vo = null;

		try {
			vo = sqlSession.selectOne("checkId", member_id);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return vo;
	}

	@Override
	public MemberVO login(MemberVO vo) {
		MemberVO user = null;
		try {
			// sqlSession.selectOne() 메서드는 MyBatis에서 제공하는 메서드임
			// 첫 번째 인자로 실행할 SQL 쿼리문의 ID를 지정하고, 두 번째 인자로는 SQL 쿼리문에 필요한 매개변수를 전달함
			// 즉, NAMESPACE + "login"은 MyBatis 매퍼 파일에서 login이라는 ID를 가진 SQL 쿼리문을 찾는 것을 의미 : member-mapper.xml ㄱㄱ
			user = sqlSession.selectOne("login", vo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}
	
	@Override
	public MemberVO selectOne(String member_id) {
		MemberVO vo = null;

		try {
			vo = sqlSession.selectOne("selectOne", member_id);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return vo;
	}

	//id중복확인 - 동림테스트
	@Override
	public int idChk(MemberVO vo) throws Exception {
		int result=sqlSession.selectOne("idChk",vo);
		System.out.println("idChk"+vo);
		return result;
	}
	
	@Override
	public MemberVO kakaoEmailChk(String kakaoEmail) {
		MemberVO vo = null;
		
		try {
			vo = sqlSession.selectOne("kakaoEmailChk", kakaoEmail);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return vo;
	}

}
