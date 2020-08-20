package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.HitDao;
import vo.HitVo;

@Service
public class HitServiceImpl implements HitService {

	@Autowired
	private HitDao dao;
	
	public HitVo selectOne(int c_idx) {
		
		HitVo vo = dao.selectOne(c_idx);
		
		return vo;
	}

	public int insert(int c_idx) {
		return dao.insert(c_idx);
	}

	public int update(HitVo vo) {
		return dao.update(vo);
	}

}
