package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.model.service.ClassService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ClassController {
	@Autowired
	private ClassService c_service;
	
	@RequestMapping(value="/category.do")
	public String class_category(String category, Integer nowPage, HttpSession session, Model model) {
		System.out.println("nowPage: " + category + " / nowPage: " + nowPage);
		// 아이디 세션 얻기
		model.addAttribute("memberInfo", session.getAttribute("memberInfo"));// 세션에서 가져온 member_id를 모델에 추가
		String member_grade = (String) session.getAttribute("memberRole");
		boolean isLogOn = session.getAttribute("isLogOn") != null ? (boolean) session.getAttribute("isLogOn") : false;
		

		
		 System.out.println("클래스 세션: "+model);
		 System.out.println("클래스 세션: "+member_grade);
		 System.out.println("클래스 세션"+isLogOn);
	    
		if(model.getAttribute("List")!=null) {
			System.out.println("classVO 못가져옴");
		}else {
			System.out.println("classVO 가져옴");
		}
		
		model.addAttribute("list", c_service.categoryList(category));
		
		
		return "onedayClass.html";
	}
	
	@RequestMapping(value="/classList.do")
	public String class_list(int nowPage, Model model, HttpSession session) {
		
		model.addAttribute("list", c_service.classList());
		model.addAttribute("memberInfo", session.getAttribute("memberInfo"));
		
		return "onedayClass";
	}
	
	@RequestMapping(value = "/class_details.do", method = RequestMethod.GET)
	public String classDetails(@RequestParam("class_no") int class_no, Model model, HttpSession session) {
	    System.out.println("memberInfo: " + session.getAttribute("memberInfo"));
	    System.out.println("class_no: " + class_no);
	    
	    model.addAttribute("list", c_service.classDetails(class_no));
	    model.addAttribute("memberInfo", session.getAttribute("memberInfo"));
	    
	    return "product-details"; // 적절한 뷰 이름 반환
	}
}
