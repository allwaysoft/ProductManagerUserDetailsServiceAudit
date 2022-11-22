package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import org.springframework.security.core.userdetails.UserDetails;

@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginLogRepository loginLogRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.getByUsername(username);
        if (user.getFailedAttempt() > 0) {
            userLoginService.resetFailedAttempts(user.getUsername());
        }
        System.out.println(request.getRemoteAddr());
        System.out.println(request.getSession().getId());
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setDescription("登录成功");
        loginLog.setIp(request.getRemoteAddr());
        loginLog.setEventtime(new Date());
        loginLog.setSessionid(request.getSession().getId());
        loginLogRepository.save(loginLog);
        System.out.println(request.getContextPath() + user.getHomepage());
        response.sendRedirect(request.getContextPath() + user.getHomepage());
        super.onAuthenticationSuccess(request, response, authentication);

    }
}
