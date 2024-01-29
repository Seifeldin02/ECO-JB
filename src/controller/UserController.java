package controller;

import model.User;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.authentication.BadCredentialsException;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import service.MailService;
import javax.servlet.http.HttpSession;

@Controller 
public class UserController {

    private static SessionFactory sessionFactory;

    static {
        // Initialize the SessionFactory during class initialization
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public void registerUser(String email, String password, String confirmPassword, String name, String icNo, String address, String hpNo, String dob, String status, String homeType, Double electricityBill, Double waterBill, Double recycleWaste) throws Exception {
        // Check if the passwords match
        if (!password.equals(confirmPassword)) {
            throw new Exception("Passwords do not match");
        }

        // Check if the email is valid
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            throw new Exception("Invalid email format");
        }
        if (!password.equals(confirmPassword)) {
            throw new Exception("Passwords do not match");
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

        // Create a new User object
        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setName(name);
        user.setIcNo(icNo);
        user.setAddress(address);
        user.setHpNo(hpNo);
        System.out.println("User hpNo: " + user.getHpNo());
        user.setDob(dob);
        user.setStatus(status);
        user.setHomeType(homeType);
        user.setElectricityBill(electricityBill);
        user.setWaterBill(waterBill);
        user.setRecycleWaste(recycleWaste);

        Session session = null;
        Transaction transaction = null;

        try {
            // Get a new Hibernate session
            session = sessionFactory.openSession();

            // Start a new transaction
            transaction = session.beginTransaction();

            // Save the User object to the database
            session.save(user);

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // More accurate error handling
            throw new Exception("Error registering user", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    @Autowired
    @Qualifier("authenticationManagerBean")
    @Lazy
    private AuthenticationManager authenticationManager;
    @RequestMapping("user/authenticate")
    public String login(@RequestParam String email, @RequestParam String password, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Create a new session and store the authenticated user's details in it
            HttpSession session = request.getSession();
            session.setAttribute("username", email); 

            return "redirect:/user/dashboard";
        } catch (BadCredentialsException e) {
            redirectAttributes.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/user/login";
        }
    }
    @Autowired
    private MailService mailService;

    @PostMapping("user/sendPasswordReset")
    public String sendPasswordReset(@RequestParam("email") String email, HttpServletRequest request) {
        Session session = null;
        Transaction transaction = null;

        try {
            // Get a new Hibernate session
            session = sessionFactory.openSession();

            // Start a new transaction
            transaction = session.beginTransaction();

            // Check if a user with the given email exists
            User user = (User) session.createQuery("FROM User WHERE email = :email")
                    .setParameter("email", email)
                    .uniqueResult();

            // If the user exists, send the password reset email
            if (user != null) {
                // Generate a unique token for this password reset request
                String token = UUID.randomUUID().toString();

                // Save the token and the user's email in the session
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
        return "redirect:/user/forgotPassword";
    }
    @PostMapping("user/resetPassword")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword, HttpServletRequest request) {
        // Check if the token is valid
        if (!token.equals(request.getSession().getAttribute("resetToken"))) {
            request.getSession().setAttribute("error", "Invalid token");
            return "redirect:/user/setNew";
        }

        // Check if the passwords match
        if (!password.equals(confirmPassword)) {
            request.getSession().setAttribute("error", "Passwords do not match");
            return "redirect:/user/setNew";
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

            // Get the user's email from the session
            String email = (String) request.getSession().getAttribute("resetEmail");

            // Get the user from the database
            User user = (User) session.createQuery("FROM User WHERE email = :email")
                    .setParameter("email", email)
                    .uniqueResult();

            // Update the user's password
            user.setPassword(hashedPassword);

            // Save the updated User object to the database
            session.update(user);

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // More accurate error handling
            request.getSession().setAttribute("error", "Error resetting password");
            return "redirect:/user/setNew";
        } finally {
            if (session != null) {
                session.close();
            }
        }

        request.getSession().setAttribute("message", "Password reset successful");
        return "redirect:/user/login";
    }
    @GetMapping("/user/logout")
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


    
}