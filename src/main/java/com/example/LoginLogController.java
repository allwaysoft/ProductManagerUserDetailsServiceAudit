package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginLogController {

    @Autowired
    private LoginLogRepository loginLogRepository;

    @RequestMapping("/loginlog")
    public String indexPermissions(Model model) {
        List<LoginLog> listLoginLogs = loginLogRepository.findAllByOrderByIdDesc();
        model.addAttribute("listLoginLogs", listLoginLogs);

        return "loginlog/list_loginlog";
    }

}
