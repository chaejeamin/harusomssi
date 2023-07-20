package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.model.dao.MemberDao;
import com.project.model.mail.RegisterMail;
import com.project.model.service.MemberService;
import com.project.model.vo.MemberVO;

import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {
	@Autowired private MemberService service;
	@Autowired MemberDao memberDAO;
	
	@RequestMapping(value="/member/qna.do")
	public String loginMembermyPage(HttpSession session, Model model) {
	    // 아이디 세션 얻기
	    String member_id = (String) session.getAttribute("member_id");
	    System.out.println("마이페이지 세션: "+member_id);
	    if (member_id != null) {
	        // 회원정보 조회
	        MemberVO vo = service.selectOne(member_id);
	        model.addAttribute("member", vo);
	        System.out.println("회원정보 조회:"+vo);

	        return "qna";
	    } else {
	        // 로그인되지 않은 상태일 경우 처리할 내용
	        return "redirect:/login"; // 로그인 페이지로 리다이렉트 또는 다른 처리를 수행
	    }
	}
}
