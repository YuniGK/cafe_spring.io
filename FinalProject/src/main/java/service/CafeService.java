package service;

import java.util.List;
import java.util.Map;

import dao.CafeDao;
import vo.CafeVo;

public interface CafeService {

	//ī�� ���	
	public int insert(CafeVo vo);
	
	//ī�� ��� ��ȸ	
	public List<CafeVo> selectList(Map map);
		
	public List<CafeVo> selectList();
	
	//������ ī�� �� ��ȸ
	public List<CafeVo> selectListSize(Map sizeMap);
	
	//ī��� �ϳ��� ��ȸ
	public CafeVo selectOne(int c_idx);
	
	//����
	public int delete(int c_idx);
	
	//����
	public int update(CafeVo vo);
	
	//����
	public int update_img(CafeVo vo);
	
}
