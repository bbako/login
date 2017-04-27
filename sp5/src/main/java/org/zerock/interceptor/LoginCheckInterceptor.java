package org.zerock.interceptor;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;
import org.zerock.domain.MemberVO;
import org.zerock.web.LoginController;

public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

	@Inject
	private LoginController loginController;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		Cookie loginCookie = WebUtils.getCookie(request, "loginCookie");

		if (loginCookie != null && request.getSession().getAttribute("member") == null) {

			String sid = loginCookie.getValue();

			MemberVO vo = loginController.getMap().get(sid);
			
			request.getSession().setAttribute("member", vo);

			return true;
		}

		HttpSession session = request.getSession();

		if (session.getAttribute("member") == null) {

			System.out.println("this user is not logined !!!");

			String uri = request.getRequestURI();

			session.setAttribute("dest", uri);

			response.sendRedirect("/login");

			return false;

		}

		return true;
	}

}
