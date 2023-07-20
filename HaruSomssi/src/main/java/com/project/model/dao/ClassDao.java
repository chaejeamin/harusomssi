package com.project.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.model.vo.ClassVO;

@Mapper
public interface ClassDao {
	// 쿼리 매핑해줄 때 쓸거임 ex) member. 뒤에 xml에 있는 쿼리 id
	String NAMESPACE = "class.";

	public List<ClassVO> categoryList(String category);
	public List<ClassVO> classList();
	public ClassVO classDetails(int class_no);
}
