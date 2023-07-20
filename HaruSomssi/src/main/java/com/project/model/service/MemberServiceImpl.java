package com.project.model.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.dao.MemberDao;
import com.project.model.vo.MemberVO;


@Service
public class MemberServiceImpl implements MemberService{
	@Autowired
	private MemberDao dao;
	
	// 회원가입
	@Override
	public void register(MemberVO vo) throws Exception{
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		vo.setMember_pw(passwordEncoder.encode(vo.getMember_pw()));
		dao.register(vo);
	}
	
	// 아이디중복확인
	@Override
	public int idChk(MemberVO vo) throws Exception {
		int result=dao.idChk(vo);
		System.out.println("dao결과 반환:"+result);
		return result;
	}
	
	// 로그인
	@Override
	public MemberVO login(MemberVO vo) {
		MemberVO user = dao.login(vo);
		return user;
	}
	
	// 로그인 화면에 입력한 id로 db에 존재하는 회원조회
	@Override
	public MemberVO selectOne(String member_id) {
		MemberVO user = dao.selectOne(member_id);
		return user;
	}
	
	// 로그인 checkId - 아이디 중복확인이랑 다름
	@Override
	public MemberVO checkId(String member_id) {
		return dao.checkId(member_id);
	}
	
//  카카오톡 로그인 api
	@Override
	public String getAccessToken(String authorize_code) throws Exception {
		String access_Token = "";
		String refresh_Token = "";
		String reqURL = "https://kauth.kakao.com/oauth/token";

		try {
			URL url = new URL(reqURL);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			// POST 요청을 위해 기본값이 false인 setDoOutput을 true로
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			
			// POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			StringBuilder sb = new StringBuilder();
			sb.append("grant_type=authorization_code");

			sb.append("&client_id=948700c95c3135eea12ca080fe0968e6"); // REST_API키 본인이 발급받은 key 넣어주기
			sb.append("&redirect_uri=http://localhost:9090/kakaologin"); // REDIRECT_URI 본인이 설정한 주소 넣어주기

			sb.append("&code=" + authorize_code);
			bw.write(sb.toString());
			bw.flush();

			// 결과 코드가 200이라면 성공
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);

			// 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			String result = "";

			while ((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println("response body : " + result);

			// jackson objectmapper 객체 생성
			ObjectMapper objectMapper = new ObjectMapper();
			// JSON String -> Map
			Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
			});

			// 사용자 정보 가져오기와 같은 카카오 API를 호출할 수 있는 토큰
			//토큰 정보 보기로 액세스 토큰 유효성 검증 후, 사용자 정보 가져오기를 요청해 필요한 사용자 정보를 받아 서비스 회원 가입 및 로그인을 완료
			access_Token = jsonMap.get("access_token").toString();
			
			//토큰 발급 시 응답으로 받은 refresh_token
			// Access Token을 갱신하기 위해 사용
			refresh_Token = jsonMap.get("refresh_token").toString();

			System.out.println("access_token : " + access_Token);
			System.out.println("refresh_token : " + refresh_Token);

			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return access_Token;
	}
	
	
	// 카카오톡 로그인 api
	@Override
	public HashMap<String, Object> getUserInfo(String access_Token) throws Throwable {
		// 요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
				HashMap<String, Object> userInfo = new HashMap<String, Object>();
				String reqURL = "https://kapi.kakao.com/v2/user/me";

				try {
					URL url = new URL(reqURL);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET"); //GET?

					// 요청에 필요한 Header에 포함될 내용
					conn.setRequestProperty("Authorization", "Bearer " + access_Token);

					int responseCode = conn.getResponseCode();
					System.out.println("responseCode : " + responseCode);

					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

					String line = "";
					String result = "";

					while ((line = br.readLine()) != null) {
						result += line;
					}
					System.out.println("response body : " + result);
					System.out.println("result type: " + result.getClass().getName()); // java.lang.String

					try {
						// jackson objectmapper 객체 생성
						ObjectMapper objectMapper = new ObjectMapper();
						// JSON String -> Map
						Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
						});

						System.out.println("properties: " + jsonMap.get("properties"));
						System.out.println("kakao_account: " + jsonMap.get("kakao_account"));
						

						Map<String, Object> properties = (Map<String, Object>) jsonMap.get("properties");
						Map<String, Object> kakao_account = (Map<String, Object>) jsonMap.get("kakao_account");

						System.out.println(properties.get("nickname"));
						System.out.println(kakao_account.get("email"));

						String nickname = properties.get("nickname").toString();
						String email = kakao_account.get("email").toString();

						userInfo.put("nickname", nickname);
						userInfo.put("email", email);

					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
				return userInfo;
	}
	
	
	// 카카오톡 api - 로그아웃
	@Override
	public void kakaologout(String access_Token) {
	    String reqURL = "https://kapi.kakao.com/v1/user/logout";
	    try {
	        URL url = new URL(reqURL);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        
	        System.out.println("%%%kakaologout access_Token:" + access_Token);
	        
	        conn.setRequestProperty("Authorization", "Bearer " + access_Token); // 여기서 문제가 되는듯 access_Token이 만료되서 그런가?
	        
	        int responseCode = conn.getResponseCode();
	        System.out.println("responseCode : " + responseCode); // 결과가 401 오류임
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        
	        String result = "";
	        String line = "";
	        
	        while ((line = br.readLine()) != null) {
	            result += line;
	        }
	        System.out.println(result);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	//카카오톡 api - 연결끊기(회원탈퇴)
	@Override
	public void kakaounlink(String access_Token) {
	    String reqURL = "https://kapi.kakao.com/v1/user/unlink";
	    try {
	        URL url = new URL(reqURL);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Authorization", "Bearer " + access_Token);
	        
	        int responseCode = conn.getResponseCode();
	        System.out.println("responseCode : " + responseCode);
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        
	        String result = "";
	        String line = "";
	        
	        while ((line = br.readLine()) != null) {
	            result += line;
	        }
	        System.out.println(result);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public MemberVO kakaoEmailChk(String kakaoEmail) {
		MemberVO user = dao.kakaoEmailChk(kakaoEmail);
		return user;
	}
	
	
	// 암복화 하는 로그인방법
//	@Override
//	public MemberVO checkId(String member_id) {
//		try {
//			member_id = AES256.encrypt(member_id);
//		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
//			e.printStackTrace();
//		}
//		return dao.checkId(member_id);
//	}
//	
//	@Override
//	public MemberVO login(MemberVO vo) {
//		AES256 aes = null;
//		try {
//			aes = new AES256(); // 이렇게 new로 인스턴스화를 안 해주니까 생성자가 실행이 안되더라고
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} 
//		
//		
//		try {
//			System.out.println(">>>test:" + vo.getMember_pw());
//			
//			//AES256를 이용한 패스워드 암호화 - 나중에 우리는 ISMS도 고려하여 DB를 관리하고 있다고 하면 가산점이 될 듯
//			vo.setMember_pw(aes.encrypt(vo.getMember_pw()));
//		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
//			e.printStackTrace();
//		}
//		
//		System.out.println(">>>암호화 완료 getMember_pw:" + vo.getMember_pw());
//		
//		MemberVO user = dao.login(vo);
//		
//		if (user != null) {
//			try {
//				// 복호화해서 객체에 저장(컨트롤러로 반환 할 객체임)
//				user.setMember_pw(aes.decrypt(user.getMember_pw()));
//				user.setMember_name(aes.decrypt(user.getMember_name()));
//				user.setMember_email(aes.decrypt(user.getMember_email()));
//			} catch (NoSuchAlgorithmException e) {
//				e.printStackTrace();
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			} catch (GeneralSecurityException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		return user;
//	}
//
//	@Override
//	public MemberVO selectOne(String member_id) {
//		MemberVO user = dao.selectOne(member_id);
//		if (user != null) {
//			try {
//				user.setMember_pw(AES256.decrypt(user.getMember_pw()));
//				user.setMember_name(AES256.decrypt(user.getMember_name()));
//				user.setMember_email(AES256.decrypt(user.getMember_email()));
//			} catch (UnsupportedEncodingException | GeneralSecurityException e) {
//				e.printStackTrace();
//			}
//		}
//		return user;
//	}



//	//db 연결테스트
//	@Override
//	public List<MemberVO> selectList() {
//		return dao.selectList();
//	}

}
