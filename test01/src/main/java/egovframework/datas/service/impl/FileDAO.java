package egovframework.datas.service.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import egovframework.datas.service.FileVO;

@Repository("fileDAO")
public class FileDAO extends EgovAbstractDAO {
	
	public String insertFiles(FileVO vo) {
		return (String) insert("fileDAO.insertFiles", vo);
	}
	
	public List<?> selectFiles(FileVO vo){
		return (List<?>) list("fileDAO.selectFiles", vo);
	}
	
	public int totalFiles() {
		return (int) select("fileDAO.totalCount");
	}

	public int deleteFiles(int id) {
		// TODO Auto-generated method stub
		return (int) delete("fileDAO.deleteFiles",id);
	}

}
