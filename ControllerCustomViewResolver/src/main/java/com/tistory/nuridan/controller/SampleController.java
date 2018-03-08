package com.tistory.nuridan.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Controller
public class SampleController {


    @Autowired
    private InternalResourceViewResolver viewResolver;
    
	@RequestMapping("test")
	@ResponseBody
	public Map<String, Object> test(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// jsp에서 참조할 bean파라미터들.
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("listData", null);
		param.put("popupData", null);
		
		String listHtml = renderView(param, request, response, "list"); // 2에서 생성한 렌더링 메소드에서 list.jsp의 컴파일된 Response Html을 받아옴
		String popupHtml = renderView(param, request, response, "popup"); // 2에서 생성한 렌더링 메소드에서 popup.jsp의 컴파일된 Response Html을 받아옴
		
		Map<String, Object> returnValue = new HashMap<String, Object>();
		returnValue.put("listHtml", listHtml);
		returnValue.put("popupHtml", popupHtml);
		
		return returnValue;
	}
	
    
	private String renderView(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response, final String viewName) {
		
		final StringWriter html = new StringWriter();
		View view = new View() {
			
			@Override
			public String getContentType() {
				return "text/html";
			}
			
			@Override
			public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

				HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(response) {
					@Override
					public PrintWriter getWriter() throws IOException {
						return new PrintWriter(html);
					}
				};
				
				View realView = viewResolver.resolveViewName(viewName, Locale.KOREA);
				
				realView.render(model, request, wrapper);
			}
		};
		
		try {
			view.render(map, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html.toString();
	}
	
}
