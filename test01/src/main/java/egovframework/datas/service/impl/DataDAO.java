package egovframework.datas.service.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import egovframework.datas.service.DataVO;

@Repository("dataDAO")
public class DataDAO  extends EgovAbstractDAO {
	
	public String insertDatas(DataVO vo) {
		return (String) insert("dataDAO.insertDatas", vo);
	}
	
	public List<?> selectDatas(DataVO vo){
		return (List<?>) list("dataDAO.selectDatas", vo);
	}
	
	public int totalDatas() {
		return (int) select("dataDAO.totalCount");
	}
	
	public int deleteDatas(int id) {
		return (int) delete("dataDAO.deleteDatas",id);
	}
}
