package org.zerock.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.zerock.domain.MemberVO;
import org.zerock.web.LoginController;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		//get 방식 필요 없음
		if (request.getMethod().equals("GET")) {
			return;
		}
		
		Object result = modelAndView.getModel().get("result");
		
		
		//로그인 실패
		if (result ==null) {
			
			response.sendRedirect("/login?error=fail");
			return;
		}
		//로그인 성공
		request.getSession().setAttribute("member", result);
		
		//로그인 쿠키 생성

		Cookie loginCookie = new Cookie("loginCookie", request.getSession().getId());
		loginCookie.setMaxAge(60*60);
		response.addCookie(loginCookie);
		
		HandlerMethod hmethod = (HandlerMethod)handler;
		LoginController controller = (LoginController)hmethod.getBean();
		
		controller.getMap().put(request.getSession().getId(), (MemberVO)result);
		
		Object dest = request.getSession().getAttribute("dest");
		if (dest==null) {
			response.sendRedirect("/");
			
		}else{
			
			response.sendRedirect((String)dest);
		}
		
		
	}
	
	

}
