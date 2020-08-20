package dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import vo.CafeVo;

public class CafeDao{

	@Autowired
	private SqlSession sqlSession;
	
	//카페 등록	
	public int insert(CafeVo vo) {
		int res = sqlSession.insert("cafe.cafe_insert", vo);
		
		//실패시 0	
		return res;
	}
	
	//카페 목록 조회	
	public List<CafeVo> selectList(Map map) {

		//System.out.println("dao map "+map);
			
		List<CafeVo> list = sqlSession.selectList("cafe.cafe_list", map);
		
		return list;
	}
		
	public List<CafeVo> selectList() {

		List<CafeVo> list = sqlSession.selectList("cafe.cafe_list_only");

		return list;
	}
	
	//지역별 카페 수 조회
	public List<CafeVo> selectListSize(Map sizeMap) {
		
		//System.out.println("dao sizeMap "+sizeMap);

		List<CafeVo> list = sqlSession.selectList("cafe.cafe_list_size", sizeMap);

		return list;
	}
	
	//카페글 하나만 조회
	public CafeVo selectOne(int c_idx) {

		CafeVo vo = sqlSession.selectOne("cafe.cafe_one_list", c_idx);
		
		return vo;
	}
	
	//삭제
	public int delete(int c_idx) {
		
		int res = sqlSession.delete("cafe.cafe_delete", c_idx);
		
		//실패시 0	
		return res;
	}
	
	//수정
	public int update(CafeVo vo) {
		
		int res = sqlSession.update("cafe.cafe_update", vo);
		
		//실패시 0	
		return res;
	}
	
	//수정
	public int update_img(CafeVo vo) {
		
		int res = sqlSession.update("cafe.cafe_update_img", vo);
		
		//실패시 0	
		return res;
	}
		
}
