package com.project.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.project.model.vo.ClassVO;

//클래스로 표시함
//@Repository 어노테이션이 부여된 클래스는 데이터 액세스 계층의 구현체로 사용되며, 스프링에서 일관된 방식으로 데이터베이스 액세스를 처리할 수 있도록 도와줌
@Repository 
@Primary
public class ClassDaoImpl implements ClassDao{
   @Autowired
   private SqlSessionTemplate sqlSession;
   
   @Override
   public List<ClassVO> categoryList(String category) {
      List<ClassVO> list = new ArrayList<ClassVO>();

      try {
         HashMap<String,Object> paramMap = new HashMap<>();
         paramMap.put("category",category);
         list = sqlSession.selectList("categoryList",paramMap);

      } catch (Exception e) {
         e.printStackTrace();
      }

      return list;
   }

   @Override
   public List<ClassVO> classList() {
      List<ClassVO> list = new ArrayList<ClassVO>();
      
      try {
         list = sqlSession.selectList("classList");
      } catch (Exception e) {
         e.printStackTrace();
      }

      return list;
   }

	@Override
	public ClassVO classDetails(int class_no) {
		ClassVO classvo = new ClassVO();
		
		try {
	         classvo = sqlSession.selectOne("classDetails", class_no);
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
		
		return classvo;
	}
}
