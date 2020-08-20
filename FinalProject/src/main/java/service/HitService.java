package service;

import vo.HitVo;

public interface HitService {

	public HitVo selectOne(int c_idx);
	
	public int insert(int c_idx);
	
	public int update(HitVo vo);
	
}
