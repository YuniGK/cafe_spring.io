package controller;

import java.io.File;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import dao.CafeDao;
import dao.MemberDao;
import dao.ReviewDao;
import service.CafeService;
import service.MemberService;
import service.ReviewService;
import vo.CafeVo;
import vo.MemberVo;
import vo.ReviewVo;

@Controller
public class MemberController {

	@Autowired
	ServletContext application;

	@Autowired
	HttpSession session;
		
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	MemberService member;
	
	@Autowired
	CafeService cafe;
	
	@Autowired
	ReviewService review;

	//로그인
	@RequestMapping(value = "/login.do", produces = "text/json; charset=utf-8;")
	@ResponseBody
	public String login(String m_id, String m_pwd){
		
		//System.out.printf("id %s / pwd %s", m_id, m_pwd);
		
		MemberVo user = member.selectOne(m_id);
		
		String result = "";
		
		if (user == null) {
			result = "fail_id";
		}else {
			if (user.getM_pwd().equals(m_pwd) == false) {
				result = "fail_pwd";
			}else {
				result = "success";
				
				//로그인 정보를 session에 넣는다.
				session = request.getSession();
				
				session.setAttribute("user", user);
				
			}
		}
		
		//json포장
		String json = String.format("{\"result\": \"%s\"}", result);
		
		//System.out.println("로그인 결과 "+json);
		
		return json;
		
	}
	
	//로그아웃
	@RequestMapping("/logout.do")
	public String logout() {
		HttpSession session = request.getSession();
		
		//세션 삭제
		session.removeAttribute("user");
		
		return "redirect:index.do";
	}
	
	//회원가입 수정 폼 이동
	@RequestMapping("/member_updateForm.do")
	public String join_form(int m_idx, Model model) {
		
		MemberVo vo = member.selectOne(m_idx);
		
		model.addAttribute("member_update", vo);
		
		return "content/mypage/user_edit";
	}
	
	//회원수정
	@RequestMapping("/member_update.do")
	public String join(MemberVo vo, 
			@RequestParam("mm_photo") MultipartFile mm_photo, Model model) throws Exception {
		
		MemberVo select = member.selectOne(vo.getM_idx()); 
		
		//System.out.println(vo.getM_idx());
		
		//저장위치 -------------------------------------------
		String wep_path = "/resources/upload";
		
		//절대경로
		String abs_path = application.getRealPath(wep_path);
		
		//System.out.printf("절대경로 %s\n", abs_path);
		
		String m_photo = "no_file.jpg";
		
		String old_photo = select.getM_photo();
		
		//System.out.println("이전 이미지명 "+old_photo);
		
		File old_f = new File(abs_path, old_photo);
		
		//이전 이미지 삭제
		old_f.delete();
		
		//업로드된 파일이 있으면
		if (!mm_photo.isEmpty()) {
		//if (photo.isEmpty()==false) {	
			
			//업로드된 파일명
			m_photo = mm_photo.getOriginalFilename();
			
			File f = new File(abs_path, m_photo);
			
			/* 스프링에서는 동일한 파일에 대한 이름을 재정의를 해주지 않는다.
			 * 
			 * 동일한 파일이 있는지 체크한다. */
			if (f.exists()) {
				//현재 시간을 구한다.
				long t = System.currentTimeMillis();
				
				//동일한 파일이 있을 경우 파일이름 앞에 시간을 붙인다.
				m_photo = String.format("%d_%s", t, m_photo);
				
				f = new File(abs_path, m_photo);
			}
			
			//임시저장된 파일을 지정된 위치로 복사
			mm_photo.transferTo(f);
		}
		
		String m_ip = request.getRemoteAddr();

		String grade = vo.getM_grade();
		
		if (grade == null) {
			grade = "일반";
		}
		
		String m_grade = grade.equals("일반") ? "일반" : grade;
		
		vo.setM_grade(m_grade);
		vo.setM_ip(m_ip);
		vo.setM_photo(m_photo);
		
		/* System.out.printf("m_id %s, m_pwd %s, m_name %s, m_gender %s, m_tel %s, m_email %s, m_photo %s, m_grade %s, m_ip %s \n",
				vo.getM_id(), vo.getM_pwd(), vo.getM_name(), vo.getM_gender(), vo.getM_tel(),
				vo.getM_email(), vo.getM_photo(), vo.getM_grade(), vo.getM_ip());
		*/
		
		int res = member.update(vo);
		
		//System.out.println("업데이트 "+res);
		
		return "redirect:user_mypage.do";
	}

	//마이페이지
	@RequestMapping("/user_mypage.do")
	public String mypage(Model model) {
		
		session = request.getSession();
		
		MemberVo user = (MemberVo) session.getAttribute("user");
		
		int m_idx = user.getM_idx();
		
		//회원정보 수정
		MemberVo userList = member.selectOne(m_idx);
		
		//회원 목록
		List<MemberVo> user_list = member.selectList();
		
		//카페 목록
		List<CafeVo> cafe_list = cafe.selectList();
		
		//리뷰 목록
		List<ReviewVo> review_list = review.selectList();
		
		//신규 회원수
		int todayMember = member.todayMember();
		
		//신규글
		int todayReview = review.today_review();
				
		//내가 쓴 글 
		int myReview = review.my_review(m_idx);
		
		String m_grader = userList.getM_grade();
		
		if(m_grader.equals("일반")) {
			
			model.addAttribute("m_idx", m_idx);
			return "redirect:my_write.do";
			
		}else {
			
			model.addAttribute("userOneList", userList);
			model.addAttribute("userList", user_list);
			model.addAttribute("cafe_list", cafe_list);
			model.addAttribute("review_list", review_list);
			model.addAttribute("todayMember", todayMember);
			model.addAttribute("todayReview", todayReview);
			model.addAttribute("myReview", myReview);
			
			return "content/mypage/admin_mypage";
		}
		
		//return (m_grader.equals("일반") ? "redirect:my_write.do?m_idx="+m_idx : "content/mypage/admin_mypage");
	}
	
	//회원가입 폼
	@RequestMapping("/insertForm.do")
	public String insert_form() {
		return "content/mypage/user_join";
	}
	
	//회원가입
	@RequestMapping("/insert.do")
	public String insert(MemberVo vo, 
			@RequestParam("mm_photo") MultipartFile mm_photo, Model model) throws Exception {
		
		//저장위치 -------------------------------------------
		String wep_path = "/resources/upload";
		
		//절대경로
		String abs_path = application.getRealPath(wep_path);
		
		//System.out.printf("절대경로 %s\n", abs_path);
		
		String m_photo = "no_file.jpg";
		
		//업로드된 파일이 있으면
		if (!mm_photo.isEmpty()) {
		//if (photo.isEmpty()==false) {	
			
			//업로드된 파일명
			m_photo = mm_photo.getOriginalFilename();
			
			File f = new File(abs_path, m_photo);
			
			/* 스프링에서는 동일한 파일에 대한 이름을 재정의를 해주지 않는다.
			 * 
			 * 동일한 파일이 있는지 체크한다. */
			if (f.exists()) {
				//현재 시간을 구한다.
				long t = System.currentTimeMillis();
				
				//동일한 파일이 있을 경우 파일이름 앞에 시간을 붙인다.
				m_photo = String.format("%d_%s", t, m_photo);
				
				f = new File(abs_path, m_photo);
			}
			
			//임시저장된 파일을 지정된 위치로 복사
			mm_photo.transferTo(f);
			
		}
		
		String m_ip = request.getRemoteAddr();
		String m_grade = "일반";
		
		vo.setM_grade(m_grade);
		vo.setM_ip(m_ip);
		vo.setM_photo(m_photo);
		
		/* System.out.printf("m_id %s, m_pwd %s, m_name %s, m_gender %s, m_tel %s, m_email %s, m_photo %s, m_grade %s, m_ip %s \n",
				vo.getM_id(), vo.getM_pwd(), vo.getM_name(), vo.getM_gender(), vo.getM_tel(),
				vo.getM_email(), vo.getM_photo(), vo.getM_gender(), vo.getM_ip()); */
		
		int res = member.insert(vo);
		
		//System.out.println("회원가입 "+res);
		
		return "redirect:index.do";
	}
	
	//회원 삭제
	@RequestMapping("/member_delete.do")
	public String delete(int m_idx) {
		
		MemberVo vo = member.selectOne(m_idx);
		
		//저장위치 -------------------------------------------
		String wep_path = "/resources/upload";
		
		//절대경로
		String abs_path = application.getRealPath(wep_path);
		
		//System.out.printf("절대경로 %s\n", abs_path);
		
		String m_photo = vo.getM_photo();
		
		//System.out.println("이미지명 "+vo.getM_photo());
		
		//업로드된 파일이 있으면
		if (!m_photo.isEmpty()) {
		//if (photo.isEmpty()==false) {	
			
			File f = new File(abs_path, m_photo);
			
			f.delete();
			
		}
		
		int res = member.delete(m_idx);
		
		return "redirect:user_mypage.do";
	}
	
	//체크 아이디
	@RequestMapping(value ="/check_id.do", produces = "text/json; charset=utf-8;")
	@ResponseBody
	public String check(String m_id) {
		
		MemberVo vo = member.selectOne(m_id);
		
		//DB에 등록된 정보가 없을 경우
		boolean result = (vo == null);
		
		//결과값
		String json = String.format("{\"result\": %b}", result);
		
		return json;
	}

}
