package egovframework.datas.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.datas.service.FileService;
import egovframework.datas.service.FileVO;

@Service("fileService")
public class FileServiceImpl implements FileService {
	
	@Resource(name="fileDAO")
	FileDAO fileDAO;

	@Override
	public String insertFiles(FileVO vo) throws Exception {
		return fileDAO.insertFiles(vo);
	}

	@Override
	public List<?> selectFiles(FileVO vo) throws Exception {
		return fileDAO.selectFiles(vo);
	}
	
	@Override
	public int totalFiles() throws Exception {
		return fileDAO.totalFiles();
	}

	@Override
	public int deleteFiles(int id) throws Exception {
		return fileDAO.deleteFiles(id);
	}

}
