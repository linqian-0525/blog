package com.lq.blog.interceptor;

import com.lq.blog.service.CNotificationService;
import com.lq.blog.service.MNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private CNotificationService cservice;
    @Autowired
    private MNotificationService mservice;
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        Long nRead = cservice.unreadCount();
        Long mRead = mservice.unreadCount();
        Long unread = nRead + mRead;
        request.getSession().setAttribute("unread",unread);
        if (request.getSession().getAttribute("user") == null){
            response.sendRedirect("/admin");

            return false;
        }
        return true;
    }
}
