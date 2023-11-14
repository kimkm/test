package egovframework.datas.service;

public class DataVO {
	private int id;
	private int page=1;	
	private float t;
	private float h;
	private float c;
	private String d;
	private String i;
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getPage() {
		return page;
	}


	public void setPage(int page) {
		this.page = page;
	}


	public float getT() {
		return t;
	}


	public void setT(float t) {
		this.t = t;
	}


	public float getH() {
		return h;
	}


	public void setH(float h) {
		this.h = h;
	}


	public float getC() {
		return c;
	}


	public void setC(float c) {
		this.c = c;
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


	public DataVO(int id, int page, float t, int h, int c, String d, String i) {
		super();
		this.id = id;
		this.page = page;
		this.t = t;
		this.h = h;
		this.c = c;
		this.d = d;
		this.i = i;
	}


	public DataVO() {
		super();
	}
	
	
}

