package egovframework.datas.service;

import java.util.List;


public interface DataService {
	public String insertDatas(DataVO vo) throws Exception;
	public List<?> selectDatas(DataVO vo) throws Exception;
	public int totalDatas() throws Exception;
	public int deleteDatas(int id) throws Exception;
}
