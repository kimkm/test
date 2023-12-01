package egovframework.datas.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;


import egovframework.datas.service.FileService;
import egovframework.datas.service.FileVO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

import java.util.List;

@Controller
public class FileUploadController {
	private String paths = "/home/atoz/upload/";

	@Resource(name = "fileService")
	private FileService fileService;

	// 파일 등록화면
	@RequestMapping(value = "/fileWrite.do")
	public String fileWrite() {
		return "filewrite";
	}

	// 파일 업로드
	@PostMapping(value = "/fileUpload.do")
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
			// WAV 파일 정보 추출
			if (ext.equalsIgnoreCase("wav")) {
				File wavFile = new File(paths + fileName);
				try {
					AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(wavFile);
					long fileSize = wavFile.length() /1024;
					double duration = (double) fileFormat.getFrameLength() / fileFormat.getFormat().getFrameRate();
					fileVO.setKb((int)fileSize);
					fileVO.setSe((int)duration);
					System.out.println("파일 용량: " + fileSize + "KB");
					System.out.println("재생 시간: " + (int)duration + "초");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// DB저장
			String result = fileService.insertFiles(fileVO);
			if (result == null) {
				System.out.println("저장성공!");
			}
		}
		return "redirect:fileWrite.do";
	}

	// 파일 리스트
	@RequestMapping(value = "/fileList.do")
	public String listFile(FileVO vo, ModelMap model) throws Exception {
		List<?> list = fileService.selectFiles(vo);
		int total = (fileService.totalFiles() / 10) + 1;
		System.out.println(list);
		model.addAttribute("resultList", list);
		model.addAttribute("page", vo.getPage());
		model.addAttribute("total", total);
		return "fileList";
	}

	// 파일 삭제
	@RequestMapping(value = "fileDelete.do")
	public String delFile(FileVO vo) throws Exception {
		System.out.println(vo.getId());
		System.out.println(vo.getFilename());
		int result = fileService.deleteFiles(vo.getId());
		if (result == 1) {
			System.out.println("DB 삭제성공!");
			File file = new File(paths + vo.getFilename());
			if (file.delete()) {
				System.out.println("파일 삭제성공!");
			}
			if(vo.getFilename().substring(0, 2).equals("F_")) {
				//System.out.println(vo.getFilename().substring(0, 2));
				File file2 = new File(paths + vo.getFilename().substring(2));
				if (file2.delete()) {
					//System.out.println(vo.getFilename().substring(2));
					System.out.println("원본 파일 삭제성공!");
				}
			}

		}
		return "redirect:fileList.do";
	}
	
	//LowPassFilter 필터
	@RequestMapping(value="fileFilter.do")
	public String fileFiltering(FileVO vo)throws Exception{
		System.out.println(vo.getFilename());
		fileService.filterFiles(vo, paths);
		return "redirect:fileList.do";
	}
	
	//NoiseFilter
	@RequestMapping(value="noisefileFilter.do")
	public String noiseFiltering(FileVO vo)throws Exception{
		System.out.println(vo.getFilename());
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win")) {
			System.out.println("윈도우서버 실행중");
			fileService.noisefilterFiles2(vo, paths);
		}else {
			System.out.println("리눅스서버 실행중");
			fileService.noisefilterFiles3(vo, paths);			
		}
		return "redirect:fileList.do";
	}
	
	// 파일 반환
	@RequestMapping(value = "fileDownload.do")
	public void fileDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String filename = request.getParameter("fileName");
		String realFilename = "";
		System.out.println(filename);

		try {
			String browser = request.getHeader("User-Agent");
			// 파일 인코딩
			if (browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")) {
				filename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
			} else {
				filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println("UnsupportedEncodingException 발생");
		}

		realFilename = paths + filename;
		System.out.println(realFilename);
		File file = new File(realFilename);
		if (!file.exists()) {
			return;
		}

		// 파일명 지정
		response.setContentType("application/octer-stream");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

		try {
			OutputStream os = response.getOutputStream();
			FileInputStream fis = new FileInputStream(realFilename);
			int cnt = 0;
			byte[] bytes = new byte[512];
			while ((cnt = fis.read(bytes)) != -1) {
				os.write(bytes, 0, cnt);
			}
			fis.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
