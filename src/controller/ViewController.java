package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;


import javax.servlet.http.HttpServletRequest;

@Controller
public class ViewController {
	
    private boolean isUserLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("username") != null;
    }

    @RequestMapping("user/login")
    public String userLogin(HttpServletRequest request) {
        if (isUserLoggedIn(request)) {
            return "redirect:/user/dashboard";
        }
        return "user/login";
    }
    
    @RequestMapping("user/register")
    public String userRegister(HttpServletRequest request) {
        if (isUserLoggedIn(request)) {
            return "redirect:/user/dashboard";
        }
        return "user/register";
    }
    @RequestMapping("user/dashboard")
    public String userDashboard(HttpServletRequest request) {
        // Get the session
        HttpSession session = request.getSession(false);

        // Check if the user is authenticated
        if (session == null || session.getAttribute("username") == null) {
            // If the user is not authenticated, redirect them to the login page
            return "redirect:/user/login";
        }

        // If the user is authenticated, return the dashboard view
        return "user/dashboard";
    }
    @RequestMapping("user/forgotPassword")
    public String userForgot(HttpServletRequest request) {
        if (isUserLoggedIn(request)) {
            return "redirect:/user/dashboard";
        }
        return "user/forgot";
    }
    @RequestMapping("user/setNew")
    public String userNew(HttpServletRequest request) {
        if (isUserLoggedIn(request)) {
            return "redirect:/user/dashboard";
        }
        return "user/setNew";
    }
}