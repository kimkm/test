package egovframework.datas.service;

import org.springframework.web.multipart.MultipartFile;

public class FileVO {
	private int id;
	private String filename;
	private MultipartFile awfile;
	private String d;
	private String i;
	private int page=1;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public MultipartFile getAwfile() {
		return awfile;
	}
	public void setAwfile(MultipartFile awfile) {
		this.awfile = awfile;
	}
	public String getD() {
		return d;
	}
	public void setD(String d) {
		this.d = d;
	}
	public String getI() {
		return i;
	}
	public void setI(String i) {
		this.i = i;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	

	
	public FileVO(int id, String filename, MultipartFile awfile, String d, String i, int page) {
		super();
		this.id = id;
		this.filename = filename;
		this.awfile = awfile;
		this.d = d;
		this.i = i;
		this.page = page;
	}
	public FileVO() {
		super();
	}
	
	

}
