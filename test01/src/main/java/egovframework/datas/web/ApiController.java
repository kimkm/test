package egovframework.datas.web;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
//org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import egovframework.datas.service.DataService;
import egovframework.datas.service.DataVO;
import egovframework.datas.service.FileService;
import egovframework.datas.service.FileVO;

@RestController
@RequestMapping("/api")
public class ApiController {
	private String paths = "C:/egovframework/";// "/home/atoz/upload/";//
	
	@Resource(name ="dataService")
	private DataService dataService;
	
	@Resource(name="fileService")
	private FileService fileService;
	
	@RequestMapping(value="/datainput.do")
	public String apiInput(@RequestBody DataVO vo) throws Exception{
		String result = dataService.insertDatas(vo);
		if(result == null) {
			System.out.println(vo);
			return "OK" ;		
		}
		return "FAIL";
	}
	
	@GetMapping(value="/datainput.do")
	public String apiInputs(@RequestParam Map<String,String> map) throws Exception{
		DataVO vo = new DataVO();
		vo.setC(Float.parseFloat(map.get("c")));
		vo.setH(Float.parseFloat(map.get("h")));
		vo.setT(Float.parseFloat(map.get("t")));
		vo.setI(map.get("i"));
		vo.setD(map.get("d"));
		String result = dataService.insertDatas(vo);
		if(result == null) {
			System.out.println(vo);
			return "OK" ;		
		}
		return "FAIL";
	}
	
	
	@RequestMapping(value = "/fileUpload.do")
	public String write(@ModelAttribute("fileVO") FileVO fileVO) throws Exception {
		String fileName = null;
		MultipartFile uploadFile = fileVO.getAwfile();
		if (!uploadFile.isEmpty()) {
			String originalFileName = uploadFile.getOriginalFilename();
			String ext = FilenameUtils.getExtension(originalFileName);
			UUID uuid = UUID.randomUUID();
			fileName = uuid + "." + ext;
			// 파일저장
			uploadFile.transferTo(new File(paths + fileName));
			fileVO.setFilename(fileName);
			System.out.println(fileVO.getFilename());
			// DB저장
			String result = fileService.insertFiles(fileVO);
			if (result == null) {
				System.out.println("저장성공!");
				return "OK";
			}
		}
		return "FAIL";
	}
	

}
