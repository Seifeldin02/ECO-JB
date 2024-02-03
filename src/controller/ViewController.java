package controller;

import model.User;
import model.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class ViewController {
    private boolean isUserLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("user") != null;
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
    
    @RequestMapping("user/dashboard")
    public String userDashboard(HttpServletRequest request) {
        // Get the session
        HttpSession session = request.getSession(false);

        // Check if the user is authenticated
        if (session == null || session.getAttribute("user") == null) {
            // If the user is not authenticated, redirect them to the login page
            return "redirect:/user/login";
        }
        

        // If the user is authenticated, return the dashboard view
        return "user/dashboard";
    }

    @Autowired
    private EntityManager entityManager;

    @RequestMapping("user/join")
    public String userJoin(HttpServletRequest request) {
        // Get the session
        HttpSession session = request.getSession(false);

        // Check if the user is authenticated
        if (session == null || session.getAttribute("user") == null) {
            // If the user is not authenticated, redirect them to the login page
            return "redirect:/user/login";
        }
        // Get the user from the session
        User user = (User) session.getAttribute("user");

        // Check the user status and return the appropriate view
        if (user.getStatus() == null) {
            return "user/join";
        } else if ("Pending".equals(user.getStatus())) {
            // Use the user id to fetch the application from the database
        	Query query = entityManager.createQuery("SELECT a FROM Application a WHERE a.user.id = :userId");
        	query.setParameter("userId", user.getId());
        	Application application = (Application) query.getSingleResult();
        	String webAppRoot = "D:\\UTM\\Year3\\Semester1\\Internet Programming\\Codes\\ECO-JB\\WebContent";
        	if (application.getFile1() != null) {
        	    application.setFile1(application.getFile1().replace(webAppRoot, ""));
        	}
        	if (application.getFile2() != null) {
        	    application.setFile2(application.getFile2().replace(webAppRoot, ""));
        	}
        	if (application.getFile3() != null) {
        	    application.setFile3(application.getFile3().replace(webAppRoot, ""));
        	}

        	// Print the application to the console
        	System.out.println(application);

        	// Store the application in the session
        	session.setAttribute("application", application);

            return "user/pending";
        } else if ("Verified".equals(user.getStatus())) {
            return "user/verified";
        }

        // If the user status is something else, return the join view as a default
        return "user/join";
    }


        @RequestMapping("user/profile")
    public String userProfile(HttpServletRequest request) {
        // Get the session
        HttpSession session = request.getSession(false);

        // Check if the user is authenticated
        if (session == null || session.getAttribute("user") == null) {
            // If the user is not authenticated, redirect them to the login page
            return "redirect:/user/login";
        }

        // If the user is authenticated, return the dashboard view
        return "user/profile";
    }

@RequestMapping("user/calendar")
public String userCalendar(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
        return "redirect:/user/login";
    }
    User user = (User) session.getAttribute("user");
    if (user.getStatus() == null) {
        redirectAttributes.addFlashAttribute("message", "Please submit an application and wait for verification before accessing these tabs");
        return "redirect:/user/dashboard";
    } else if ("Pending".equals(user.getStatus())) {
        redirectAttributes.addFlashAttribute("message", "Please wait for a staff to verify your credentials before accessing these tabs");
        return "redirect:/user/dashboard";
    }
    return "user/calendar";
}

@RequestMapping("user/carbon")
public String userCarbon(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
        return "redirect:/user/login";
    }
    User user = (User) session.getAttribute("user");
    if (user.getStatus() == null) {
        redirectAttributes.addFlashAttribute("message", "Please submit an application and wait for verification before accessing these tabs");
        return "redirect:/user/dashboard";
    } else if ("Pending".equals(user.getStatus())) {
        redirectAttributes.addFlashAttribute("message", "Please wait for a staff to verify your credentials before accessing these tabs");
        return "redirect:/user/dashboard";
    }
    return "user/carbon";
}

@RequestMapping("user/details")
public String userdetails(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
        return "redirect:/user/login";
    }
    User user = (User) session.getAttribute("user");
    if (user.getStatus() == null) {
        redirectAttributes.addFlashAttribute("message", "Please submit an application and wait for verification before accessing these tabs");
        return "redirect:/user/dashboard";
    } else if ("Pending".equals(user.getStatus())) {
        redirectAttributes.addFlashAttribute("message", "Please wait for a staff to verify your credentials before accessing these tabs");
        return "redirect:/user/dashboard";
    }
    return "user/details";
}

//STAFF MAPPINGS
//
//
//
//
//
//
//


private boolean isStaffLoggedIn(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    return session != null && session.getAttribute("staff") != null;
}


@RequestMapping("staff/login")
public String staffLogin(HttpServletRequest request) {
    if (isStaffLoggedIn(request)) {
        return "redirect:/staff/dashboard";
    }
    return "staff/login";
}

@RequestMapping("staff/register")
public String staffRegister(HttpServletRequest request) {
    if (isUserLoggedIn(request)) {
        return "redirect:/staff/dashboard";
    }
    return "staff/register";
}
}