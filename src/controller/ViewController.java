package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ViewController {

    @RequestMapping("user/login")
    public String userLogin() {
        return "user/login";
    }
    @RequestMapping("user/register")
    public String userRegister() {
        return "user/register";
    }
}