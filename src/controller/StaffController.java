package controller;
import controller.UserController;
import model.User;
import model.Staff;
import model.Application;
import org.springframework.stereotype.Controller;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.*;
import org.springframework.security.core.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import service.FileUploadService;
import org.hibernate.query.Query;
import java.util.UUID;

import service.MailService;
import javax.servlet.http.HttpSession;
import repository.UserRepository;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.persistence.*;
@Controller 
public class StaffController{
	
    @Autowired
    private static SessionFactory sessionFactory;

    static {
        // Initialize the SessionFactory during class initialization
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    @PostMapping("/staff/register")
    public String registerStaff(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam("name") String name,
            @RequestParam(value = "icNo", required = false) String icNo,
            @RequestParam("address") String address,
            @RequestParam(value = "hpNo", required = false) String hpNo,
            @RequestParam("dob") String dob,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "homeType", required = false)String homeType,
            @RequestParam(value = "electricityBill", required = false) Double electricityBill,
            @RequestParam(value = "waterBill", required = false) Double waterBill,
            @RequestParam(value = "recycleWaste", required = false) Double recycleWaste,
            RedirectAttributes redirectAttributes) {

        try {
            // Check if the passwords match
            if (!password.equals(confirmPassword)) {
                throw new Exception("Passwords do not match");
            }

            // Check if the email is valid
            String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (!email.matches(emailRegex)) {
                throw new Exception("Invalid email format");
            }

            // Check if password length is between 8 and 20
            if (password.length() < 8 || password.length() > 20) {
                throw new Exception("Password must be between 8 and 20 characters");
            }

            // Check if password contains at least one letter and one number
            if (!password.matches(".*[A-Za-z].*") || !password.matches(".*[0-9].*")) {
                throw new Exception("Password must contain at least one letter and one number");
            }

            // Hash the password
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Create a new staff object
            Staff staff = new Staff();
            staff.setEmail(email);
            staff.setPassword(hashedPassword);
            staff.setName(name);
            staff.setIcNo(icNo);
            staff.setAddress(address);
            staff.setHpNo(hpNo);
            staff.setDob(dob);
            staff.setStatus(status);
            staff.setHomeType(homeType);
            staff.setElectricityBill(electricityBill);
            staff.setWaterBill(waterBill);
            staff.setRecycleWaste(recycleWaste);

            Session session = null;
            Transaction transaction = null;

            try {
                // Get a new Hibernate session
                session = sessionFactory.openSession();

                // Start a new transaction
                transaction = session.beginTransaction();

                // Save the staff object to the database
                session.save(staff);

                // Commit the transaction
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace(); // More accurate error handling
                throw new Exception("Error registering staff", e);
            } finally {
                if (session != null) {
                    session.close();
                }
            }

            // Redirect to the login page with a success message
            redirectAttributes.addFlashAttribute("message", "Registration successful. Please wait for verification from admin before log in.");
            return "redirect:/staff/login";
        } catch (Exception e) {
            // Redirect back to the registration page with an error message
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/staff/register";
        }
    }
    
    
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @RequestMapping("staff/authenticate")
    public String login(@RequestParam String email, @RequestParam String password, @RequestParam(required = false) String rememberMe, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            // Fetch the staff details from the database using Hibernate
            Session session = sessionFactory.openSession();
            Query<?> query = session.createQuery("FROM Staff WHERE email = :email");
            query.setParameter("email", email);
            Staff staff = (Staff) query.uniqueResult();
            session.close();

            // Check if the staff exists
            if (staff != null) {
                // Check if the staff status is null
                if (staff.getStatus() == null) {
                    redirectAttributes.addFlashAttribute("error", "Wait for verification from admin before log in");
                    return "redirect:/staff/login";
                }

                // Check if the staff status is "Verified"
                if ("Verified".equals(staff.getStatus())) {
                	 try {
                         Authentication authentication = authenticationManager.authenticate(
                                 new UsernamePasswordAuthenticationToken(email, password)
                         );
                         SecurityContextHolder.getContext().setAuthentication(authentication);
                     } catch (AuthenticationException e) {
                         redirectAttributes.addFlashAttribute("error", "Invalid email or password");
                         return "redirect:/staff/login";
                     }

                    // Create a new session and store the authenticated staff's details in it
                    HttpSession httpSession = request.getSession();

                    // If the "Remember me" checkbox is checked, set the session timeout to a large value
                    // If the checkbox is not checked, set the session timeout to 30 minutes
                    if ("on".equals(rememberMe)) {
                        httpSession.setMaxInactiveInterval(Integer.MAX_VALUE);
                    } else {
                        httpSession.setMaxInactiveInterval(30 * 60);
                    }

                    // Store the staff details in the session
                    httpSession.setAttribute("staff", staff);

                    return "redirect:/staff/dashboard";
                }
            }

            redirectAttributes.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/staff/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while trying to authenticate");
            return "redirect:/staff/login";
        }
    }
   
    
    @Autowired
    private MailService mailService;

    @PostMapping("staff/sendPasswordReset")
    public String sendPasswordReset(@RequestParam("email") String email, HttpServletRequest request) {
        Session session = null;
        Transaction transaction = null;

        try {
            // Get a new Hibernate session
            session = sessionFactory.openSession();

            // Start a new transaction
            transaction = session.beginTransaction();

            // Check if a staff with the given email exists
            Staff staff = (Staff) session.createQuery("FROM Staff WHERE email = :email")
                    .setParameter("email", email)
                    .uniqueResult();

            // If the staff exists, send the password reset email
            if (staff != null) {
                // Generate a unique token for this password reset request
                String token = UUID.randomUUID().toString();

                // Save the token and the staff's email in the session
                request.getSession().setAttribute("resetToken", token);
                request.getSession().setAttribute("resetEmail", email);

                // Create the password reset link
                String resetLink = request.getRequestURL().toString().replace("sendPasswordReset", "setNew") + "?token=" + token;

                // Send the password reset email
                mailService.sendPasswordResetEmail(email, resetLink);
            }

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // More accurate error handling
        } finally {
            if (session != null) {
                session.close();
            }
        }

        request.getSession().setAttribute("message", "If this is a valid email, we'll send a password reset link to this email");
        return "redirect:/staff/forgotPassword";
    }

    
    
    @PostMapping("staff/resetPassword")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword, HttpServletRequest request) {
        // Check if the token is valid
        if (!token.equals(request.getSession().getAttribute("resetToken"))) {
            request.getSession().setAttribute("error", "Invalid token");
            return "redirect:/staff/setNew?token=" + token;
        }

        // Check if the passwords match
        if (!password.equals(confirmPassword)) {
            request.getSession().setAttribute("error", "Passwords do not match");
            return "redirect:/staff/setNew?token=" + token;
        }

        // Check if password length is between 8 and 20
        if (password.length() < 8 || password.length() > 20) {
            request.getSession().setAttribute("error", "Password length should be between 8 and 20");
            return "redirect:/staff/setNew?token=" + token;
        }

        // Check if password contains at least one letter and one number
        if (!password.matches(".*[A-Za-z].*") || !password.matches(".*[0-9].*")) {
            request.getSession().setAttribute("error", "Password must contain at least 1 letter and 1 number");
            return "redirect:/staff/setNew?token=" + token;
        }


        // Hash the new password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        Session session = null;
        Transaction transaction = null;

        try {
            // Get a new Hibernate session
            session = sessionFactory.openSession();

            // Start a new transaction
            transaction = session.beginTransaction();

            // Get the staff's email from the session
            String email = (String) request.getSession().getAttribute("resetEmail");

            // Get the staff from the database
            Staff staff = (Staff) session.createQuery("FROM Staff WHERE email = :email")
                    .setParameter("email", email)
                    .uniqueResult();

            // Update the staff's password
            staff.setPassword(hashedPassword);

            // Save the updated staff object to the database
            session.update(staff);
            // Commit the transaction
            transaction.commit();
            //Disable the token after successfull password updating
            request.getSession().removeAttribute("resetToken");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // More accurate error handling
            request.getSession().setAttribute("error", "Error resetting password");
            return "redirect:/staff/setNew";
        } finally {
            if (session != null) {
                session.close();
            }
        }

        request.getSession().setAttribute("message", "Password reset successful");
        return "redirect:/staff/login";
    }
    
    
    @GetMapping("/staff/logout")
    public String logout(HttpServletRequest request) {
        // Get the session and invalidate it
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Clear the security context
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }
    
    

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/staff/editProfile")
    public ModelAndView editProfile(@RequestParam String password, @ModelAttribute Staff staff, BindingResult bindingResult, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Get the staff from the session
        Staff sessionstaff = (Staff) request.getSession().getAttribute("staff");

        if (!passwordEncoder.matches(password, sessionstaff.getPassword())) {
            // If not, add an error message and redirect back to the profile page
            redirectAttributes.addFlashAttribute("errorMessage", "Incorrect password!");
            return new ModelAndView("redirect:/staff/profile");
        }

        // Get the form data
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String icNo = request.getParameter("icno");
        String hpNo = request.getParameter("hpno");
        String address = request.getParameter("address");
        String dob = request.getParameter("dob");
        String homeType = request.getParameter("hometype");
        String currentPassword = request.getParameter("currentPassword");
        String status = null;

        // Check the current password
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(sessionstaff.getEmail(), currentPassword)
            );
        } catch (BadCredentialsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Incorrect password!");
            return new ModelAndView("redirect:/staff/profile");
        }

        // Update the staff's details
        sessionstaff.setName(name);
        sessionstaff.setEmail(email);
        sessionstaff.setIcNo(icNo);
        sessionstaff.setHpNo(hpNo);
        sessionstaff.setAddress(address);
        sessionstaff.setDob(dob);
        sessionstaff.setHomeType(homeType);
        sessionstaff.setStatus(status);

        Session session = null;
        Transaction transaction = null;

        try {
            // Get a new Hibernate session
            session = sessionFactory.openSession();

            // Start a new transaction
            transaction = session.beginTransaction();

            // Update the staff object in the database
            session.update(sessionstaff);

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // More accurate error handling
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating profile");
            return new ModelAndView("redirect:/staff/profile");
        } finally {
            if (session != null) {
                session.close();
            }
        }

        // Save the updated staff back to the session
        request.getSession().setAttribute("staff", sessionstaff);
        redirectAttributes.addFlashAttribute("successMessage", "Profile edited successfully!");

        // Redirect to the profile page
        return new ModelAndView("redirect:/staff/profile");
    }
    
    @PostMapping("/staff/declineApplication")
    public ModelAndView declineApplication(@RequestParam long id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Get the staff from the session
        Staff staff = (Staff) request.getSession().getAttribute("staff");

        // Get the HttpSession from the HttpServletRequest
        HttpSession httpSession = request.getSession();

        Session session = null;
        Transaction transaction = null;

        try {
            // Get a new Hibernate session
            session = sessionFactory.openSession();

            // Start a new transaction
            transaction = session.beginTransaction();
            Query<Application> selectQuery = session.createQuery("from Application where id = :applicationId", Application.class);
            selectQuery.setParameter("applicationId", id);
            Application application = selectQuery.uniqueResult();

            // Get the application from the database
            // Delete the files
            if (application.getFile1() != null) {
                Files.deleteIfExists(Paths.get(application.getFile1()));
            }
            if (application.getFile2() != null) {
                Files.deleteIfExists(Paths.get(application.getFile2()));
            }
            if (application.getFile3() != null) {
                Files.deleteIfExists(Paths.get(application.getFile3()));
            }

            // Delete the application
            Query<?> deleteQuery = session.createQuery("delete from Application where id = :applicationId");
            deleteQuery.setParameter("applicationId", id);
            deleteQuery.executeUpdate();

            // Update the user's status
            User user = session.get(User.class, application.getUser().getId());
            user.setStatus(null);
            session.update(user);

            // Remove the application from the HttpSession
            httpSession.removeAttribute("application");

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // More accurate error handling
            redirectAttributes.addFlashAttribute("errorMessage", "Error approving application");
            return new ModelAndView("redirect:/staff/applications");
        } finally {
            if (session != null) {
                session.close();
            }
        }

        // Redirect back to the applications page
        return new ModelAndView("redirect:/staff/applications");
    }
    
    
    @PostMapping("/staff/approveApplication")
    public ModelAndView approveApplication(@RequestParam long id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Get the staff from the session
        Staff staff = (Staff) request.getSession().getAttribute("staff");

        // Get the HttpSession from the HttpServletRequest
        HttpSession httpSession = request.getSession();

        Session session = null;
        Transaction transaction = null;

        try {
            // Get a new Hibernate session
            session = sessionFactory.openSession();

            // Start a new transaction
            transaction = session.beginTransaction();
            Query<Application> selectQuery = session.createQuery("from Application where id = :applicationId", Application.class);
            selectQuery.setParameter("applicationId", id);
            Application application = selectQuery.uniqueResult();

            // Get the application from the database
            // Delete the files
            if (application.getFile1() != null) {
                Files.deleteIfExists(Paths.get(application.getFile1()));
            }
            if (application.getFile2() != null) {
                Files.deleteIfExists(Paths.get(application.getFile2()));
            }
            if (application.getFile3() != null) {
                Files.deleteIfExists(Paths.get(application.getFile3()));
            }

            // Delete the application
            Query<?> deleteQuery = session.createQuery("delete from Application where id = :applicationId");
            deleteQuery.setParameter("applicationId", id);
            deleteQuery.executeUpdate();

            // Update the user's status
            User user = session.get(User.class, application.getUser().getId());
            user.setStatus("Verified");
            session.update(user);

            // Remove the application from the HttpSession
            httpSession.removeAttribute("application");

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // More accurate error handling
            redirectAttributes.addFlashAttribute("errorMessage", "Error approving application");
            return new ModelAndView("redirect:/staff/applications");
        } finally {
            if (session != null) {
                session.close();
            }
        }

        // Redirect back to the applications page
        return new ModelAndView("redirect:/staff/applications");
    }
    
    
}