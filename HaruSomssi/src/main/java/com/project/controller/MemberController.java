package com.project.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.project.model.dao.MemberDao;
import com.project.model.mail.RegisterMail;
import com.project.model.service.MemberService;
import com.project.model.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MemberController {
	@Autowired
	private MemberService service;
	@Autowired
	private RegisterMail registerMail;
	@Autowired
	MemberDao memberDAO;
	
	
	
	//회원가입 Post
	@ResponseBody
	@RequestMapping(value="/member/register.do",method=RequestMethod.POST)
	public String postRegister(@RequestBody MemberVO vo) throws Exception{
		System.out.println("post register");
		
		System.out.println("vo 확인: " + vo.getMember_id());
		
		int result=service.idChk(vo);
		
		System.out.println("result값: " + result);
		try {
			if(result==1) {
				return "redirect:/";
			}else if(result==0) {
				service.register(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/loginform.do";
	}
	
	// 회원가입 인증메일
	@PostMapping("login/mailConfirm")
	@ResponseBody
	public ResponseEntity<Map<String, String>> mailConfirm(@RequestParam("email") String email) {
		   try {
		       String code = registerMail.sendSimpleMessage(email);
		       System.out.println("인증코드: " + code);
		       // JSON 형식으로 응답을 반환
		       Map<String, String> responseObject = new HashMap<>();
		       responseObject.put("code", code);
		       return ResponseEntity.ok().body(responseObject);
		   } catch (Exception e) {
		       e.printStackTrace();
		       // 예외가 발생한 경우 오류 메시지를 반환
		       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		   }
		}
	
	//아이디 중복확인
	@ResponseBody
	@RequestMapping(value="/member/idChk.do", method=RequestMethod.POST)
	public int idChk(MemberVO vo) throws Exception{
		int result=service.idChk(vo);
		return result;
	}

	@RequestMapping("/loginform.do")
	public String loginForm() {
		return "login";
	}
	
	//마이페이지
	
	/** 회원 수정 전 비밀번호 확인 **/
   /* @GetMapping(value="/member/checkPwd")
    public boolean checkPassword(@RequestParam String checkPassword,
                                Model model){

        log.info("checkPwd 진입");
        MemberVO vo;
        String member_id = vo.getMember_id();

        return memberService.checkPassword(member_id, checkPassword);
    }*/

    
	@RequestMapping(value="/member/myPage.do")
	public String loginMembermyPage(HttpServletRequest request, Model model) {
	    // 아이디 세션 얻기
		HttpSession session = request.getSession();
	    String member_id = (String) session.getAttribute("memberInfo");
	    String member_grade = (String) session.getAttribute("member_grade");
	    System.out.println("마이페이지 세션: "+member_id);
	    
	    if (member_id != null) {
	        // 회원정보 조회
	        MemberVO vo = service.selectOne(member_id);
	        model.addAttribute("member", vo);
	        System.out.println("회원정보 조회:"+vo);

	        model.addAttribute("memberInfo", member_id); // 세션에서 가져온 member_id를 모델에 추가
	        return "myPage";
	    } else {
	        String alertMessage="로그인 상태가 아닙니다.";
	        model.addAttribute("alertMessage", alertMessage);
	        
	        return "redirect:/login"; // 로그인 페이지로 리다이렉트 또는 다른 처리를 수행
	    }
	}
	
	@ResponseBody 
	// 컨트롤러 메서드가 반환하는 데이터를 HTTP 응답 body에 직접 쓰도록 지정
	// 일반적으로 스프링 MVC에서 컨트롤러 메서드는 데이터를 반환하면 View를 찾아 해당 데이터를 사용하여 HTML 페이지를 생성하고, 이를 HTTP 응답으로 전송 
	// but, @ResponseBody 어노테이션을 사용하면 컨트롤러 메서드의 반환 값이 직접 HTTP 응답 body에 쓰여짐
	// 즉, 데이터 자체를 반환하는 것이 아니라 데이터를 포함한 응답 body가 반환됨
	// 이를 통해 API 개발이나 AJAX 요청에 대한 응답 처리 등에서 유용하게 활용할 수 있음
	@RequestMapping(value="/member/login.do",method=RequestMethod.POST)
	public Map<String, String> ajaxLogin(@RequestBody MemberVO vo, HttpSession session){
	// 메서드의 매개변수로 @RequestBody 어노테이션을 사용하여 요청의 HTTP body에 포함된 JSON 데이터를 MemberDto 객체(클라이언트에서 전달한 로그인 정보를 담는 객체)로 변환함
		
		// logger.info("[Controller] ajaxlogin.do");
		// 로그인 결과를 담을 Map 객체
		Map<String, String> map = new HashMap<String, String>();
		
		MemberVO dbVo = service.selectOne(vo.getMember_id());
		String rawPw = "";
        String encodePw = "";
		
        if(dbVo != null) { // 화면에서 입력한 id와 같은 회원이 있으면
        	// BCryptPasswordEncoder는 Spring Security에서 제공하는 비밀번호 암호화를 수행하는 클래스
        	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        	
        	rawPw = vo.getMember_pw(); // 화면에서 가져온 비밀번호
        	encodePw = dbVo.getMember_pw(); // 데이터베이스 암호화된 비밀번호
        	
        	if(true == passwordEncoder.matches(rawPw, encodePw)) {
        		vo.setMember_pw(encodePw);        		
        		MemberVO mVo = service.login(vo);       		
        		String chk = "false";        		
        		System.out.println("mVo 객체 확인 ㄱㄱ: " + mVo.getMember_id() + mVo.getMember_pw());
        		      		
//        		if(mDto != null && mDto.getMember_grade() != "관리자") {
        		if(mVo != null) {
        			// 정상 로그인 처리
        			chk = "true"; // 로그인 검증결과

        			String member_id=mVo.getMember_id();
        			String member_grade=mVo.getMember_grade();
        			
        			//세션값 저장(아이디, 권한) 
        			session.setAttribute("memberInfo", member_id);
        			session.setAttribute("memberRole", member_grade);
        			//타임리프사용 할 때 세션
        			session.setAttribute("isLogOn", true);
        			
        			System.out.println("로그인 세션: "+ member_id);
        			System.out.println("로그인 세션: "+ member_grade);
        			System.out.println("세션확인: "+session);
       			
        		}
        		map.put("chk", chk);
        	}       	
        }
		
		return map;
        // return "login";
		
	}	
	
	//동림테스트
	@RequestMapping(value="/main.do")
	public String main(Model model, HttpSession session) {
		// 아이디 세션 얻기
		model.addAttribute("memberInfo", session.getAttribute("memberInfo"));// 세션에서 가져온 member_id를 모델에 추가
		model.addAttribute("memberRole", session.getAttribute("memberRole"));
		boolean isLogOn = session.getAttribute("isLogOn") != null ? (boolean) session.getAttribute("isLogOn") : false;
		
		 
		 System.out.println("메인 세션: "+model);
		 System.out.println("메인 세션"+isLogOn);
	    

		return "index";
	}
	
	//로그아웃
	@RequestMapping(value = "/logout.do", method = RequestMethod.GET)
	public String logout(HttpServletRequest request) {
	    HttpSession session = request.getSession(true);
	    session.invalidate();
	    log.info("로그아웃 성공");    
	    return "redirect:/main.do";
	}
	
	@RequestMapping(value = "/kakaologin", method = RequestMethod.GET)
	public String kakaoLogin(@RequestParam(value = "code", required = false) String code, HttpSession session, Model model) throws Throwable {
		// 반환할 map - 혹시 몰라서
		Map<String, String> map = new HashMap<String, String>();
		// 반환할 modelandview
		// ModelAndView mav = new ModelAndView();
		
		System.out.println("code:" + code);
		// 인증코드 요청
		String access_token = service.getAccessToken(code);
		System.out.println("###access_token#### : " + access_token);
		
		// 인증코드로 토큰 요청
		HashMap<String, Object> userInfo = service.getUserInfo(access_token);
		System.out.println("###nickname#### : " + userInfo.get("nickname"));
		System.out.println("###email#### : " + userInfo.get("email"));
		
		String kakaoEmail = (String) userInfo.get("email");
		MemberVO dbVO = service.kakaoEmailChk(kakaoEmail);		
		
		if(dbVO != null) {
			model.addAttribute("result", "kakaosuccess");
			model.addAttribute("member_id",dbVO.getMember_id());
			model.addAttribute("member_name",dbVO.getMember_name());
			model.addAttribute("member_email",dbVO.getMember_email());
			model.addAttribute("member_grade",dbVO.getMember_grade());
			model.addAttribute("access_token", access_token);
			
			session.setAttribute("access_token", access_token);
		}else {
			model.addAttribute("result", "kakaofail");
			model.addAttribute("access_token", access_token);
			
			return "login"; //login.html에 model 바인딩(확인완료)
		}
		
		return "index"; //index.html에 model 바인딩(확인완료)
	}
	
	// 카카오톡 로그아웃 - 제민(조금 불안정함. 급한 것 부터 먼저하고 나중에 수정해가면서 재구현 할거임, 일단 주석처리)
	@RequestMapping(value="/kakaologout", method = RequestMethod.GET)
	public String kakaologout() {
		// System.out.println("@@@kakaologout access_token:" + access_token); // @RequestBody String access_token 매개변수에 해줘야함
		
		// service.kakaologout(access_token);
		// session.invalidate();
		return "login";
	}
	
	// 카카오톡 연결끊기(회원탈퇴)
	@RequestMapping(value="/kakaounlink", method = RequestMethod.POST)
	public String kakaounlink(@RequestBody String access_token, HttpSession session) {
		service.kakaounlink(access_token);
		session.invalidate();
		return "redirect:/loginform.do";
		//return "redirect:/member/loginform.do";
	}
	
	
}
