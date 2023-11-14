package egovframework.datas.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.datas.service.DataService;
import egovframework.datas.service.DataVO;

@Service("dataService")
public class DataServiceImpl implements DataService{
	
	@Resource(name="dataDAO")
	private DataDAO dataDAO;

	@Override
	public String insertDatas(DataVO vo) throws Exception {
		// TODO Auto-generated method stub
		return dataDAO.insertDatas(vo);
	}

	@Override
	public List<?> selectDatas(DataVO vo) throws Exception {
		// TODO Auto-generated method stub
		return dataDAO.selectDatas(vo);
	}

	@Override
	public int totalDatas() throws Exception {
		// TODO Auto-generated method stub
		return dataDAO.totalDatas();
	}

	@Override
	public int deleteDatas(int id) throws Exception {
		// TODO Auto-generated method stub
		return dataDAO.deleteDatas(id);
	}

}
