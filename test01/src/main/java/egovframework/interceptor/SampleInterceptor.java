package egovframework.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


public class SampleInterceptor implements AsyncHandlerInterceptor {



	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		System.out.println("pre===================================================");
		System.out.println("handler:"+handler);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
			System.out.println("post+++++++++++++++++++++++++++++++++++++++++++++++++");
			//List<?> list = (List)modelAndView.getModel().get("resultList");
			//System.out.println("list.size: "+ list.size());
	}
}
