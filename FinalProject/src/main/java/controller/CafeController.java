package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import dao.CafeDao;
import dao.HitDao;
import dao.MemberDao;
import dao.ReviewDao;
import service.CafeService;
import service.HitService;
import service.ReviewService;
import vo.CafeVo;
import vo.HitVo;
import vo.ReviewVo;

@Controller
public class CafeController {

	@Autowired
	ServletContext application;

	@Autowired
	HttpSession session;
		
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	CafeService cafe;
	
	@Autowired
	HitService hit;
	
	@Autowired
	ReviewService review;

	// index 목록
	@RequestMapping("/index.do")
	public String list(Model model) {
		// 목록 가져오기
		List<CafeVo> main_cafe_list = cafe.selectList();

		model.addAttribute("main_cafe_list", main_cafe_list);

		// Dispatcher(forward)
		return "index";
	}

	// 카테고리 클릭 후 카페목록
	@RequestMapping("/cafe_list.do")
	public String cafeList(String addrs, int page_num, String c_tag, Model model) {

		// xml if문에서는 String형이 사용이 불가하다. map으로 싸서 보내줘야 한다.
		Map map = new HashMap();
		Map sizeMap = new HashMap();

		if (addrs != null) {
			map.put("addrs", addrs);
			sizeMap.put("addrs", addrs);
		}
		
		if (c_tag != null) {
			map.put("c_tag", c_tag);
			sizeMap.put("c_tag", c_tag);
		}

		map.put("page_num", page_num);

		// 목록 가져오기
		List<CafeVo> product_cafe_list = cafe.selectList(map);

		model.addAttribute("product_cafe_list", product_cafe_list);

		// 사이즈
		List<CafeVo> cafe_size = cafe.selectListSize(sizeMap);

		if (cafe_size.size() != 0) {

			int size = (int) Math.ceil(cafe_size.size() / 5.0);

			model.addAttribute("size", size);

		}
		
		if (product_cafe_list.size() == 0) {
			return "redirect:index.do";
		} else {
			return "content/cafe/cafe_list";
		}

	}
	
	//카페 삭제
	@RequestMapping("/mypage_cafe_delete.do")
	public String delete(int c_idx) {
		
		CafeVo vo = cafe.selectOne(c_idx);
		
		//저장위치 -------------------------------------------
		String wep_path = "/resources/upload";
		
		//절대경로
		String abs_path = application.getRealPath(wep_path);
		
		//System.out.printf("절대경로 %s\n", abs_path);
		
		String c_photo = vo.getC_photo();
		
		//System.out.println("이미지명 "+c_photo);
		
		//업로드된 파일이 있으면
		if (!c_photo.isEmpty()) {
		//if (photo.isEmpty()==false) {	
			
			File f = new File(abs_path, c_photo);
			
			f.delete();
		}
		
		int res = cafe.delete(c_idx);
		
		return "redirect:user_mypage.do";
		
	}
	
	//카페 등록 폼 이동
	@RequestMapping("/cafe_insert_form.do")
	public String insert_form() {
		return "content/cafe/cafe_write";
	}
	
	//카페 등록
	@RequestMapping("/cafe_insert.do")
	public String insert(CafeVo vo, 
			@RequestParam("cc_photo") List<MultipartFile> cc_photo, Model model) throws Exception {
		
		//저장위치 -------------------------------------------
		String wep_path = "/resources/upload";
		
		//절대경로
		String abs_path = application.getRealPath(wep_path);
		
		List<String> photo_list = new ArrayList<String> ();
		
		String photo = "cafe.jpg";
		String c_photo = photo;
		
		//업로드된 파일이 있으면
		if (cc_photo != null && cc_photo.size() > 0) {
			
			for (MultipartFile mf : cc_photo) {
				
				//업로드된 파일명
				photo = mf.getOriginalFilename();
				
				File f = new File(abs_path, photo);
				
				if (f.exists()) {
					//현재 시간을 구한다.
					long t = System.currentTimeMillis();
					
					//동일한 파일이 있을 경우 파일이름 앞에 시간을 붙인다.
					photo = String.format("%d_%s", t, photo);
					
					f = new File(abs_path, photo);
				}
				
				//임시저장된 파일을 지정된 위치로 복사
				mf.transferTo(f);
				
				photo_list.add(photo);
			}
			
			StringBuilder sb = new StringBuilder();
			
			for (String pl: photo_list) {
				sb.append(pl).append(",");
			}
			
			c_photo = sb.toString();
			
		}
		
		vo.setC_photo(c_photo);
		
		int res = cafe.insert(vo);
		
		return "redirect:user_mypage.do";
	}
	
	//카페 상세페이지
	@RequestMapping("/cafe_detail.do")
	public String detail(int c_idx, Model model) {
		
		//목록 가져오기
		CafeVo cafe_detail = cafe.selectOne(c_idx);
		
		//리뷰목록 가져오기
		List<ReviewVo> review_detail = review.selectList(c_idx);
		
		//리뷰 평점 가져오기
		ReviewVo review_avg = review.select_review_avg(c_idx);
		
		//리뷰 총수
		ReviewVo review_total = review.select_review_total(c_idx);
		
		//조회수
		HitVo resVo = hit.selectOne(c_idx);
		
		model.addAttribute("cafe_detail", cafe_detail);
		model.addAttribute("review_detail", review_detail);
		model.addAttribute("review_avg", review_avg);
		model.addAttribute("review_total", review_total);
		model.addAttribute("resVo", resVo);
		
		return "content/cafe/cafe_detail";
	}

	//조회수
	@RequestMapping("/hit_insert.do")
	public String hit(int c_idx, Model model) {
		
		HitVo vo = hit.selectOne(c_idx);
		
		if (vo == null) {
			int res = hit.insert(c_idx);
		}else {
			int s_num = vo.getS_num() + 1;
			
			vo.setS_num(s_num);
			
			int up = hit.update(vo);
		}
		
		model.addAttribute("c_idx", c_idx);
		
		return "redirect:cafe_detail.do";
	}
	
	//수정 폼 이동
	@RequestMapping("/cafe_updateForm.do")
	public String update_form(int c_idx, Model model) {
		
		HitVo vo = hit.selectOne(c_idx);
		
		CafeVo cafe_update = cafe.selectOne(c_idx);
		
		model.addAttribute("cafe_update", cafe_update);
		
		return "content/cafe/cafe_edit";
	}
	
	//수정
	@RequestMapping("/cafe_img_update.do")
	public String update_img(CafeVo vo, 
			@RequestParam("cc_photo") List<MultipartFile> cc_photo, Model model) throws Exception {
		
		CafeVo select = cafe.selectOne(vo.getC_idx());
		
		//저장위치 -------------------------------------------
		String wep_path = "/resources/upload";
		
		//절대경로
		String abs_path = application.getRealPath(wep_path);
		
		List<String> photo_list = new ArrayList<String> ();
		
		String photo = "cafe.jpg";
		String c_photo = photo;
		
		String old_photo = select.getC_photo();
		
		File old_f = new File(abs_path, old_photo);
		
		//이전 이미지 삭제
		old_f.delete();
		
		//업로드된 파일이 있으면
		if (cc_photo != null && cc_photo.size() > 0) {
			
			for (MultipartFile mf : cc_photo) {
				
				//업로드된 파일명
				photo = mf.getOriginalFilename();
				
				File f = new File(abs_path, photo);
				
				if (f.exists()) {
					//현재 시간을 구한다.
					long t = System.currentTimeMillis();
					
					//동일한 파일이 있을 경우 파일이름 앞에 시간을 붙인다.
					photo = String.format("%d_%s", t, photo);
					
					f = new File(abs_path, photo);
				}
				
				//임시저장된 파일을 지정된 위치로 복사
				mf.transferTo(f);
				
				photo_list.add(photo);
			}
			
			StringBuilder sb = new StringBuilder();
			
			for (String pl: photo_list) {
				sb.append(pl).append(",");
			}
			
			c_photo = sb.toString();
			
		}
				
		vo.setC_photo(c_photo);
				
		int res = cafe.update_img(vo);
		
		return "redirect:user_mypage.do";
	}

	//수정
	@RequestMapping("/cafe_update.do")
	public String update(CafeVo vo, Model model) {
		
		CafeVo select = cafe.selectOne(vo.getC_idx());
		
		String c_photo = select.getC_photo();
		
		vo.setC_photo(c_photo);
		
		int res = cafe.update(vo);
		
		return "redirect:user_mypage.do";
	}

}
