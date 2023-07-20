package com.project.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {
		@GetMapping("/index.html")
	   public void main(Model model) {
	   }
	
	   @GetMapping("/login.html")
	   public void login(Model model) {
	   }
	   
	   @GetMapping("/onedayClass.html")
	   public void onedayClass(Model model) {
	   }
	   
	   @GetMapping("/qna.html")
	   public void qna(Model model) {
	   }
	   
	   @GetMapping("/checkout.html")
	   public void checkout(Model model) {
	   }
	   
	   @GetMapping("/product-details.html")
	   public void class_details(Model model) {
	   }
	   
		/** 회원 수정하기 전 비밀번호 확인 **/
	    @GetMapping("/checkPwd.html")
	    public String checkPwdView(){
	        return "check-pwd";
	    }
	   

		
	   
	   
	  
}
