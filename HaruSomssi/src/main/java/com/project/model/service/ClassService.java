package com.project.model.service;

import java.util.List;

import com.project.model.vo.ClassVO;

public interface ClassService {

	public List<ClassVO> categoryList(String category);
	
	public List<ClassVO> classList();
	
	public ClassVO classDetails(int class_no);
}
