package controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import service.FileUploadService;
import org.hibernate.query.Query;
import java.util.UUID;
import service.MailService;
import javax.servlet.http.HttpSession;
import repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            redirectAttributes.addFlashAttribute("message", "Registration successful. Please log in.");
            return "redirect:/staff/login";
        } catch (Exception e) {
            // Redirect back to the registration page with an error message
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/staff/register";
        }
    }
	
	
}