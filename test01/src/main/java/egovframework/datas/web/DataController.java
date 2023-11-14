package egovframework.datas.web;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.ModelMap;
import egovframework.datas.service.DataService;
import egovframework.datas.service.DataVO;


@Controller
public class DataController {
	
	@Resource(name ="dataService")
	private DataService dataService;
	
	@RequestMapping(value="/write.do")
	public String writes() {
		return "write";
	}
	
	@PostMapping(value="/datainput.do")
	public String insertData(DataVO vo) throws Exception{
		String result = dataService.insertDatas(vo);
		if(result == null) {
			System.out.println("저장성공");
		}
		return "redirect:write.do";		
	}
	
	@RequestMapping(value="/list.do")
	public String listData(DataVO vo, ModelMap model) throws Exception{
		List<?> list = dataService.selectDatas(vo);
		int total = (dataService.totalDatas()/10)+1;
		System.out.println(vo.getPage());
		System.out.println(total);
		System.out.println(list);
		model.addAttribute("resultList", list);
		model.addAttribute("page",vo.getPage());
		model.addAttribute("total",total);
		return "dataList";		
	}
	
	@RequestMapping(value = "dataDelete.do")
	public String delFile(DataVO vo) throws Exception {
		System.out.println(vo.getId());
		int result = dataService.deleteDatas(vo.getId());
		if (result == 1) {
				System.out.println("DB 삭제성공!");
		}
		return "redirect:list.do";
	}
	
	@RequestMapping(value="ci.png")
	@ResponseBody
	public String ciString() {
		return "ci01.png";
	}
	
	public String subDate(String d) {
		if(d.length()==14) {
		String output = d.substring(0, 4) + "-"
                + d.substring(4, 6) + "-"
                + d.substring(6, 8) + " "
                + d.substring(8, 10) + ":"
                + d.substring(10, 12) +":"
                + d.substring(12, 14);
		return output;
		}
		return d;
	}

}
