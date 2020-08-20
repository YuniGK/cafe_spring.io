package service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.CafeDao;
import dao.HitDao;
import dao.MemberDao;
import dao.ReviewDao;
import vo.CafeVo;

@Service
public class CafeServiceImpl implements CafeService{

	@Autowired
	private CafeDao dao;

	public int insert(CafeVo vo) {
		return dao.insert(vo);
	}

	public List<CafeVo> selectList(Map map) {
		//System.out.println("서비스 map"+map);
		
		List<CafeVo> list = dao.selectList(map);
		return list;
	}

	public List<CafeVo> selectList() {
		List<CafeVo> list = dao.selectList();
		return list;
	}

	public List<CafeVo> selectListSize(Map sizeMap) {
		
		//System.out.println("서비스 map 크기"+sizeMap);
		
		List<CafeVo> list = dao.selectListSize(sizeMap);
		return list;
	}

	public CafeVo selectOne(int c_idx) {
		CafeVo vo = dao.selectOne(c_idx);
		return vo;
	}

	public int delete(int c_idx) {
		return dao.delete(c_idx);
	}

	public int update(CafeVo vo) {
		return dao.update(vo);
	}

	public int update_img(CafeVo vo) {
		return dao.update_img(vo);
	}
	
	
	
}
