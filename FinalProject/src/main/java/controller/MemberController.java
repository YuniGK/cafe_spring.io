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

	//�α���
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
				
				//�α��� ������ session�� �ִ´�.
				session = request.getSession();
				
				session.setAttribute("user", user);
				
			}
		}
		
		//json����
		String json = String.format("{\"result\": \"%s\"}", result);
		
		//System.out.println("�α��� ��� "+json);
		
		return json;
		
	}
	
	//�α׾ƿ�
	@RequestMapping("/logout.do")
	public String logout() {
		HttpSession session = request.getSession();
		
		//���� ����
		session.removeAttribute("user");
		
		return "redirect:index.do";
	}
	
	//ȸ������ ���� �� �̵�
	@RequestMapping("/member_updateForm.do")
	public String join_form(int m_idx, Model model) {
		
		MemberVo vo = member.selectOne(m_idx);
		
		model.addAttribute("member_update", vo);
		
		return "content/mypage/user_edit";
	}
	
	//ȸ������
	@RequestMapping("/member_update.do")
	public String join(MemberVo vo, 
			@RequestParam("mm_photo") MultipartFile mm_photo, Model model) throws Exception {
		
		MemberVo select = member.selectOne(vo.getM_idx()); 
		
		//System.out.println(vo.getM_idx());
		
		//������ġ -------------------------------------------
		String wep_path = "/resources/upload";
		
		//������
		String abs_path = application.getRealPath(wep_path);
		
		//System.out.printf("������ %s\n", abs_path);
		
		String m_photo = "no_file.jpg";
		
		String old_photo = select.getM_photo();
		
		//System.out.println("���� �̹����� "+old_photo);
		
		File old_f = new File(abs_path, old_photo);
		
		//���� �̹��� ����
		old_f.delete();
		
		//���ε�� ������ ������
		if (!mm_photo.isEmpty()) {
		//if (photo.isEmpty()==false) {	
			
			//���ε�� ���ϸ�
			m_photo = mm_photo.getOriginalFilename();
			
			File f = new File(abs_path, m_photo);
			
			/* ������������ ������ ���Ͽ� ���� �̸��� �����Ǹ� ������ �ʴ´�.
			 * 
			 * ������ ������ �ִ��� üũ�Ѵ�. */
			if (f.exists()) {
				//���� �ð��� ���Ѵ�.
				long t = System.currentTimeMillis();
				
				//������ ������ ���� ��� �����̸� �տ� �ð��� ���δ�.
				m_photo = String.format("%d_%s", t, m_photo);
				
				f = new File(abs_path, m_photo);
			}
			
			//�ӽ������ ������ ������ ��ġ�� ����
			mm_photo.transferTo(f);
		}
		
		String m_ip = request.getRemoteAddr();

		String grade = vo.getM_grade();
		
		if (grade == null) {
			grade = "�Ϲ�";
		}
		
		String m_grade = grade.equals("�Ϲ�") ? "�Ϲ�" : grade;
		
		vo.setM_grade(m_grade);
		vo.setM_ip(m_ip);
		vo.setM_photo(m_photo);
		
		/* System.out.printf("m_id %s, m_pwd %s, m_name %s, m_gender %s, m_tel %s, m_email %s, m_photo %s, m_grade %s, m_ip %s \n",
				vo.getM_id(), vo.getM_pwd(), vo.getM_name(), vo.getM_gender(), vo.getM_tel(),
				vo.getM_email(), vo.getM_photo(), vo.getM_grade(), vo.getM_ip());
		*/
		
		int res = member.update(vo);
		
		//System.out.println("������Ʈ "+res);
		
		return "redirect:user_mypage.do";
	}

	//����������
	@RequestMapping("/user_mypage.do")
	public String mypage(Model model) {
		
		session = request.getSession();
		
		MemberVo user = (MemberVo) session.getAttribute("user");
		
		int m_idx = user.getM_idx();
		
		//ȸ������ ����
		MemberVo userList = member.selectOne(m_idx);
		
		//ȸ�� ���
		List<MemberVo> user_list = member.selectList();
		
		//ī�� ���
		List<CafeVo> cafe_list = cafe.selectList();
		
		//���� ���
		List<ReviewVo> review_list = review.selectList();
		
		//�ű� ȸ����
		int todayMember = member.todayMember();
		
		//�űԱ�
		int todayReview = review.today_review();
				
		//���� �� �� 
		int myReview = review.my_review(m_idx);
		
		String m_grader = userList.getM_grade();
		
		if(m_grader.equals("�Ϲ�")) {
			
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
		
		//return (m_grader.equals("�Ϲ�") ? "redirect:my_write.do?m_idx="+m_idx : "content/mypage/admin_mypage");
	}
	
	//ȸ������ ��
	@RequestMapping("/insertForm.do")
	public String insert_form() {
		return "content/mypage/user_join";
	}
	
	//ȸ������
	@RequestMapping("/insert.do")
	public String insert(MemberVo vo, 
			@RequestParam("mm_photo") MultipartFile mm_photo, Model model) throws Exception {
		
		//������ġ -------------------------------------------
		String wep_path = "/resources/upload";
		
		//������
		String abs_path = application.getRealPath(wep_path);
		
		//System.out.printf("������ %s\n", abs_path);
		
		String m_photo = "no_file.jpg";
		
		//���ε�� ������ ������
		if (!mm_photo.isEmpty()) {
		//if (photo.isEmpty()==false) {	
			
			//���ε�� ���ϸ�
			m_photo = mm_photo.getOriginalFilename();
			
			File f = new File(abs_path, m_photo);
			
			/* ������������ ������ ���Ͽ� ���� �̸��� �����Ǹ� ������ �ʴ´�.
			 * 
			 * ������ ������ �ִ��� üũ�Ѵ�. */
			if (f.exists()) {
				//���� �ð��� ���Ѵ�.
				long t = System.currentTimeMillis();
				
				//������ ������ ���� ��� �����̸� �տ� �ð��� ���δ�.
				m_photo = String.format("%d_%s", t, m_photo);
				
				f = new File(abs_path, m_photo);
			}
			
			//�ӽ������ ������ ������ ��ġ�� ����
			mm_photo.transferTo(f);
			
		}
		
		String m_ip = request.getRemoteAddr();
		String m_grade = "�Ϲ�";
		
		vo.setM_grade(m_grade);
		vo.setM_ip(m_ip);
		vo.setM_photo(m_photo);
		
		/* System.out.printf("m_id %s, m_pwd %s, m_name %s, m_gender %s, m_tel %s, m_email %s, m_photo %s, m_grade %s, m_ip %s \n",
				vo.getM_id(), vo.getM_pwd(), vo.getM_name(), vo.getM_gender(), vo.getM_tel(),
				vo.getM_email(), vo.getM_photo(), vo.getM_gender(), vo.getM_ip()); */
		
		int res = member.insert(vo);
		
		//System.out.println("ȸ������ "+res);
		
		return "redirect:index.do";
	}
	
	//ȸ�� ����
	@RequestMapping("/member_delete.do")
	public String delete(int m_idx) {
		
		MemberVo vo = member.selectOne(m_idx);
		
		//������ġ -------------------------------------------
		String wep_path = "/resources/upload";
		
		//������
		String abs_path = application.getRealPath(wep_path);
		
		//System.out.printf("������ %s\n", abs_path);
		
		String m_photo = vo.getM_photo();
		
		//System.out.println("�̹����� "+vo.getM_photo());
		
		//���ε�� ������ ������
		if (!m_photo.isEmpty()) {
		//if (photo.isEmpty()==false) {	
			
			File f = new File(abs_path, m_photo);
			
			f.delete();
			
		}
		
		int res = member.delete(m_idx);
		
		return "redirect:user_mypage.do";
	}
	
	//üũ ���̵�
	@RequestMapping(value ="/check_id.do", produces = "text/json; charset=utf-8;")
	@ResponseBody
	public String check(String m_id) {
		
		MemberVo vo = member.selectOne(m_id);
		
		//DB�� ��ϵ� ������ ���� ���
		boolean result = (vo == null);
		
		//�����
		String json = String.format("{\"result\": %b}", result);
		
		return json;
	}

}
