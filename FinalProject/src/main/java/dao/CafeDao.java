package dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import vo.CafeVo;

public class CafeDao{

	@Autowired
	private SqlSession sqlSession;
	
	//ī�� ���	
	public int insert(CafeVo vo) {
		int res = sqlSession.insert("cafe.cafe_insert", vo);
		
		//���н� 0	
		return res;
	}
	
	//ī�� ��� ��ȸ	
	public List<CafeVo> selectList(Map map) {

		//System.out.println("dao map "+map);
			
		List<CafeVo> list = sqlSession.selectList("cafe.cafe_list", map);
		
		return list;
	}
		
	public List<CafeVo> selectList() {

		List<CafeVo> list = sqlSession.selectList("cafe.cafe_list_only");

		return list;
	}
	
	//������ ī�� �� ��ȸ
	public List<CafeVo> selectListSize(Map sizeMap) {
		
		//System.out.println("dao sizeMap "+sizeMap);

		List<CafeVo> list = sqlSession.selectList("cafe.cafe_list_size", sizeMap);

		return list;
	}
	
	//ī��� �ϳ��� ��ȸ
	public CafeVo selectOne(int c_idx) {

		CafeVo vo = sqlSession.selectOne("cafe.cafe_one_list", c_idx);
		
		return vo;
	}
	
	//����
	public int delete(int c_idx) {
		
		int res = sqlSession.delete("cafe.cafe_delete", c_idx);
		
		//���н� 0	
		return res;
	}
	
	//����
	public int update(CafeVo vo) {
		
		int res = sqlSession.update("cafe.cafe_update", vo);
		
		//���н� 0	
		return res;
	}
	
	//����
	public int update_img(CafeVo vo) {
		
		int res = sqlSession.update("cafe.cafe_update_img", vo);
		
		//���н� 0	
		return res;
	}
		
}
