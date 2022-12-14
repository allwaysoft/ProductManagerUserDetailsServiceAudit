package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private LoginLogRepository loginLogRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        String username = request.getParameter("username");
        User user = userRepository.getByUsername(username);
        if (user != null) {
            LoginLog loginLog = new LoginLog();
            loginLog.setUsername(username);
            loginLog.setDescription("登录失败");
            loginLog.setIp(request.getRemoteAddr());
            loginLog.setEventtime(new Date());
            loginLog.setSessionid(request.getSession().getId());
            loginLogRepository.save(loginLog);
            if (user.getEnabled() && user.getAccountNonLocked()) {
                if (user.getFailedAttempt() < userLoginService.MAX_FAILED_ATTEMPTS - 1) {
                    System.out.println("user.getFailedAttempt()=" + user.getFailedAttempt());
                    userLoginService.increaseFailedAttempts(user);
                } else {

                    userLoginService.lock(user);
                    exception = new LockedException("your account has been locked due to 3 failed attempt"
                            + " It will be unclocked after 15 minutes");
                    System.out.println(exception);
                    System.out.println("userLoginService.lock(user)");
                }
            } else if (!user.getAccountNonLocked()) {
                if (userLoginService.unlockWhenTimeExpired(user)) {
                    exception = new LockedException("your account has been unclock ."
                            + " please try to login again");
                    System.out.println(exception);
                }
            }
        }

        super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
