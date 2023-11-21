package egovframework.datas.service;

import java.util.List;

public interface FileService {
	public String insertFiles(FileVO vo) throws Exception;
	public List<?> selectFiles(FileVO vo) throws Exception;
	public int totalFiles() throws Exception;
	public int deleteFiles(int id) throws Exception;
	public boolean filterFiles(FileVO vo,String path) throws Exception;
}