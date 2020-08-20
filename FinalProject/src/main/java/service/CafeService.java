package service;

import java.util.List;
import java.util.Map;

import dao.CafeDao;
import vo.CafeVo;

public interface CafeService {

	//카페 등록	
	public int insert(CafeVo vo);
	
	//카페 목록 조회	
	public List<CafeVo> selectList(Map map);
		
	public List<CafeVo> selectList();
	
	//지역별 카페 수 조회
	public List<CafeVo> selectListSize(Map sizeMap);
	
	//카페글 하나만 조회
	public CafeVo selectOne(int c_idx);
	
	//삭제
	public int delete(int c_idx);
	
	//수정
	public int update(CafeVo vo);
	
	//수정
	public int update_img(CafeVo vo);
	
}
