package com.project.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.model.dao.ClassDao;
import com.project.model.vo.ClassVO;

@Service
public class ClassServiceImpl implements ClassService{
	
	@Autowired
	private ClassDao c_dao;

	@Override
	public List<ClassVO> categoryList(String category) {
		System.out.println("classservice에서의 카테고리 확인: " + category);		
		return c_dao.categoryList(category);
	}
	
	@Override
	public List<ClassVO> classList() {
		return c_dao.classList();
	}

	@Override
	public ClassVO classDetails(int class_no) {
		return c_dao.classDetails(class_no);
	}

	

}
