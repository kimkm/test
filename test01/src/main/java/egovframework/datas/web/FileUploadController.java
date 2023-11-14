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

import java.util.List;

@Controller
public class FileUploadController {
	private String paths = "/home/atoz/upload/";// "C:/egovframework/";//

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
			File file = new File(paths + vo.getFilename());
			System.out.println("DB 삭제성공!");
			if (file.delete()) {
				System.out.println("파일 삭제성공!");
			}

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