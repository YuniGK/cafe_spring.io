package controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dao.MemberDao;
import dao.ReviewDao;
import vo.MemberVo;
import vo.ReviewVo;

@Controller
public class ReviewController {
	@Autowired
	ServletContext application;

	@Autowired
	HttpSession session;
		
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	MemberDao member_dao;
	
	@Autowired
	ReviewDao review_dao;
	
	//삭제
	@RequestMapping("/mypage_review_delete.do")
	public String delete(int r_idx, String url) {
		int res = review_dao.delete(r_idx);
		
		//System.out.println(url);
		
		//model.addAttribute("url", url);
		
		return "redirect:"+url;
	}
	
	//등록
	@RequestMapping("/review_insert.do")
	public String insert(ReviewVo vo, int c_idx, Model model) {
		String r_ip = request.getRemoteAddr();
		
		vo.setR_ip(r_ip);
		
		//로그인 사용자 정보 구하기 --------------------------------
		HttpSession session = request.getSession();
		
		MemberVo user = (MemberVo) session.getAttribute("user");
		
		int m_idx = user.getM_idx();
		
		vo.setM_idx(m_idx);
		
		int review_writer = review_dao.insert(vo);
		
		model.addAttribute("c_idx", c_idx);
		
		return "redirect:cafe_detail.do";
	}
	
	//목록 조회
	@RequestMapping("/my_write.do")
	public String list(int m_idx, Model model) {
		HttpSession session = request.getSession();
		
		MemberVo user = (MemberVo) session.getAttribute("user");
		
		//내가 쓴 리뷰 목록
		List<ReviewVo> my_review = review_dao.select(m_idx);
		
		//내가 쓴 글 
		int myReview = review_dao.my_review(m_idx);
		
		//System.out.println(" 1 "+ m_idx);
		
		model.addAttribute("my_review", my_review);
		model.addAttribute("myReview", myReview);
		
		return "content/mypage/my_reivew";
	}
	
	//수정 폼 이동
	@RequestMapping("/mypage_review_update_form.do")
	public String update_form(int r_idx, Model model) {
		
		ReviewVo vo = review_dao.selectOne(r_idx);
		
		model.addAttribute("vo", vo);
		
		return "content/cafe/review";
	}
	
	//수정
	@RequestMapping("/mypage_review_update.do")
	public String update(String url, int r_idx, int c_idx,ReviewVo vo) {
		String r_ip = request.getRemoteAddr();
		
		//로그인 사용자 정보 구하기 --------------------------------
		HttpSession session = request.getSession();
		
		MemberVo user = (MemberVo) session.getAttribute("user");
		
		int m_idx = user.getM_idx();
		
		vo.setM_idx(m_idx);
		vo.setR_ip(r_ip);
		vo.setR_idx(r_idx);
		vo.setC_idx(c_idx);
		
		int res = review_dao.update(vo);
		
		return "redirect:"+url;
		
	}
	
}
