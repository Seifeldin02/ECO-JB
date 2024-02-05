package controller;

import model.User;
import model.Staff;
import model.Application;
import model.Carbon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.ui.Model;
import java.util.List;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    


    // Assume you have a SessionFactory bean
    @Autowired
    private SessionFactory sessionFactory;
    

    @RequestMapping("user/dashboard")
    public String userDashboard(HttpServletRequest request) {
        // Get the session
        HttpSession httpSession = request.getSession(false);

        // Check if the httpSession exists
        if (httpSession != null) {
            User user = (User) httpSession.getAttribute("user");
            if (user != null) {
                // Get a new Hibernate session
                Session session = sessionFactory.openSession();
                // Fetch the latest user data from the database
                User updatedUser = session.get(User.class, user.getId());
                // Update the user in the httpSession
                httpSession.setAttribute("user", updatedUser);
                // Close the Hibernate session
                session.close();
            }
        }

        // Check if the user is authenticated
        if (httpSession == null || httpSession.getAttribute("user") == null) {
            // If the user is not authenticated, redirect them to the login page
            return "redirect:/user/login";
        }

        // If the user is authenticated, return the dashboard view
        return "user/dashboard";
    }

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping("user/join")
    public String userJoin(HttpServletRequest request) {
        // Get the session
        HttpSession session = request.getSession(false);

        // Check if the session exists
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                // Get a new Hibernate session
                Session hibernateSession = sessionFactory.openSession();
                // Fetch the latest user data from the database
                User updatedUser = hibernateSession.get(User.class, user.getId());
                // Update the user in the session
                session.setAttribute("user", updatedUser);
                // Close the Hibernate session
                hibernateSession.close();
            }
        }

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
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            User user = (User) httpSession.getAttribute("user");
            if (user != null) {
                Session session = sessionFactory.openSession();
                User updatedUser = session.get(User.class, user.getId());
                httpSession.setAttribute("user", updatedUser);
                session.close();
            }
        }

        if (httpSession == null || httpSession.getAttribute("user") == null) {
            return "redirect:/user/login";
        }

        return "user/profile";
    }

    @RequestMapping("user/calendar")
    public String userCalendar(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            User user = (User) httpSession.getAttribute("user");
            if (user != null) {
                Session session = sessionFactory.openSession();
                User updatedUser = session.get(User.class, user.getId());
                httpSession.setAttribute("user", updatedUser);
                session.close();
            }
        }

        if (httpSession == null || httpSession.getAttribute("user") == null) {
            return "redirect:/user/login";
        }

        User user = (User) httpSession.getAttribute("user");
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
    public String userCarbon(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            User user = (User) httpSession.getAttribute("user");
            if (user != null) {
                Session session = sessionFactory.openSession();
                User updatedUser = session.get(User.class, user.getId());
                httpSession.setAttribute("user", updatedUser);

                // Fetch all Carbon data from the database
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery<Carbon> cq = cb.createQuery(Carbon.class);
                Root<Carbon> root = cq.from(Carbon.class);
                cq.select(root);
                List<Carbon> carbonData = session.createQuery(cq).getResultList();

                // Fetch Carbon data for the current user from the database
                cq.where(cb.equal(root.get("user"), user.getId())); // Add a where clause to fetch data for the current user only
                List<Carbon> currentUserCarbonData = session.createQuery(cq).getResultList();

                // Calculate averages
                double averageElectricUsage = carbonData.stream().mapToDouble(Carbon::getElectricUsage).average().orElse(0.0);
                double averageWaterUsage = carbonData.stream().mapToDouble(Carbon::getWaterUsage).average().orElse(0.0);
                double averageRecycleUsage = carbonData.stream().mapToDouble(Carbon::getRecycleUsage).average().orElse(0.0);

                // Create a new Carbon object to hold the average data
                Carbon averageCarbonData = new Carbon();
                averageCarbonData.setElectricUsage(averageElectricUsage);
                averageCarbonData.setWaterUsage(averageWaterUsage);
                averageCarbonData.setRecycleUsage(averageRecycleUsage);

                // Convert the Carbon data to a JSON string
                ObjectMapper mapper = new ObjectMapper();
                String jsonCarbonData = "";
                String jsonCurrentUserCarbonData = "";
                String jsonAverageCarbonData = "";
                try {
                    jsonCarbonData = mapper.writeValueAsString(carbonData);
                    jsonCurrentUserCarbonData = mapper.writeValueAsString(currentUserCarbonData);
                    jsonAverageCarbonData = mapper.writeValueAsString(averageCarbonData);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Add the JSON strings to the model
                model.addAttribute("jsonCarbonData", jsonCarbonData);
                model.addAttribute("jsonCurrentUserCarbonData", jsonCurrentUserCarbonData);
                model.addAttribute("jsonAverageCarbonData", jsonAverageCarbonData);

                session.close();
            }
        }

        if (httpSession == null || httpSession.getAttribute("user") == null) {
            return "redirect:/user/login";
        }

        User user = (User) httpSession.getAttribute("user");
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
    public String userDetails(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            User user = (User) httpSession.getAttribute("user");
            if (user != null) {
                Session session = sessionFactory.openSession();
                User updatedUser = session.get(User.class, user.getId());

                // Fetch the Carbon data related to the user
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery<Carbon> criteria = cb.createQuery(Carbon.class);
                Root<Carbon> carbonRoot = criteria.from(Carbon.class);
                criteria.select(carbonRoot);
                criteria.where(cb.equal(carbonRoot.get("user"), updatedUser));

                Carbon userCarbon = session.createQuery(criteria).uniqueResult();                

                // Add the Carbon data to the model
                model.addAttribute("carbon", userCarbon);

                httpSession.setAttribute("user", updatedUser);
                session.close();
            }
        }

        if (httpSession == null || httpSession.getAttribute("user") == null) {
            return "redirect:/user/login";
        }

        User user = (User) httpSession.getAttribute("user");
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
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            Staff staff = (Staff) httpSession.getAttribute("staff");
            if (staff != null) {
                Session session = sessionFactory.openSession();
                Staff updatedStaff = session.get(Staff.class, staff.getId());
                httpSession.setAttribute("staff", updatedStaff);
                session.close();
            }
        }
        return httpSession != null && httpSession.getAttribute("staff") != null;
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
        if (isStaffLoggedIn(request)) {
            return "redirect:/staff/dashboard";
        }
        return "staff/register";
    }

    @RequestMapping("staff/dashboard")
    public String staffDashboard(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            Staff staff = (Staff) httpSession.getAttribute("staff");
            if (staff != null) {
                Session session = sessionFactory.openSession();
                Staff updatedStaff = session.get(Staff.class, staff.getId());
                httpSession.setAttribute("staff", updatedStaff);
                session.close();
            }
        }

        if (httpSession == null || httpSession.getAttribute("staff") == null) {
            return "redirect:/staff/login";
        }

        Staff staff = (Staff) httpSession.getAttribute("staff");
        if (!"Verified".equals(staff.getStatus())) {
            httpSession.invalidate();
            return "redirect:/staff/login";
        }

        return "staff/dashboard";
    }


@RequestMapping("staff/forgotPassword")
public String staffForgot(HttpServletRequest request) {
    if (isStaffLoggedIn(request)) {
        return "redirect:/staff/dashboard";
    }
    return "staff/forgot";
}

@RequestMapping("staff/setNew")
public String staffNew(HttpServletRequest request) {
    if (isStaffLoggedIn(request)) {
        return "redirect:/staff/dashboard";
    }
    return "staff/setNew";
}
@RequestMapping("staff/profile")
public String staffProfile(HttpServletRequest request) {
    HttpSession httpSession = request.getSession(false);
    if (httpSession != null) {
        Staff staff = (Staff) httpSession.getAttribute("staff");
        if (staff != null) {
            Session session = sessionFactory.openSession();
            Staff updatedStaff = session.get(Staff.class, staff.getId());
            httpSession.setAttribute("staff", updatedStaff);
            session.close();
        }
    }

    if (httpSession == null || httpSession.getAttribute("staff") == null) {
        return "redirect:/staff/login";
    }

    Staff staff = (Staff) httpSession.getAttribute("staff");
    if (!"Verified".equals(staff.getStatus())) {
        httpSession.invalidate();
        return "redirect:/staff/login";
    }

    return "staff/profile";
}

@RequestMapping("staff/applications")
@Transactional
public String staffApplications(HttpServletRequest request, Model model) {
    HttpSession httpSession = request.getSession(false);
    if (httpSession != null) {
        Staff staff = (Staff) httpSession.getAttribute("staff");
        if (staff != null) {
            Session session = sessionFactory.openSession();
            Staff updatedStaff = session.get(Staff.class, staff.getId());
            httpSession.setAttribute("staff", updatedStaff);
            session.close();
        }
    }

    if (httpSession == null || httpSession.getAttribute("staff") == null) {
        return "redirect:/staff/login";
    }

    Staff staff = (Staff) httpSession.getAttribute("staff");
    if (!"Verified".equals(staff.getStatus())) {
        httpSession.invalidate();
        return "redirect:/staff/login";
    }

    List<Application> applications = entityManager.createQuery("SELECT a FROM Application a", Application.class).getResultList();
    String webAppRoot = "D:\\UTM\\Year3\\Semester1\\Internet Programming\\Codes\\ECO-JB\\WebContent";
    for (Application application : applications) {
        if (application.getFile1() != null) {
            application.setFile1(application.getFile1().replace(webAppRoot, ""));
        }
        if (application.getFile2() != null) {
            application.setFile2(application.getFile2().replace(webAppRoot, ""));
        }
        if (application.getFile3() != null) {
            application.setFile3(application.getFile3().replace(webAppRoot, ""));
        }
    }

    model.addAttribute("applications", applications);

    return "staff/applications";
}


}