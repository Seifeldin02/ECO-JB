package controller;

import model.User;
import model.Application;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import model.Carbon;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
@Controller 
public class UserController {
    @Autowired
    private static SessionFactory sessionFactory;

    static {
        // Initialize the SessionFactory during class initialization
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    @PostMapping("/user/register")
    public String registerUser(
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

            // Create a new User object
            User user = new User();
            user.setEmail(email);
            user.setPassword(hashedPassword);
            user.setName(name);
            user.setIcNo(icNo);
            user.setAddress(address);
            user.setHpNo(hpNo);
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

            // Redirect to the login page with a success message
            redirectAttributes.addFlashAttribute("message", "Registration successful. Please log in.");
            return "redirect:/user/login";
        } catch (Exception e) {
            // Redirect back to the registration page with an error message
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/register";
        }
    }

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Autowired
    @Qualifier("authenticationManagerBean")
    @Lazy
    private AuthenticationManager authenticationManager;
    @RequestMapping("user/authenticate")
    public String login(@RequestParam String email, @RequestParam String password, @RequestParam(required = false) String rememberMe, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Create a new session and store the authenticated user's details in it
            HttpSession session = request.getSession();

            // If the "Remember me" checkbox is checked, set the session timeout to a large value
            // If the checkbox is not checked, set the session timeout to 30 minutes
            if ("on".equals(rememberMe)) {
                session.setMaxInactiveInterval(Integer.MAX_VALUE);
            } else {
                session.setMaxInactiveInterval(30 * 60);
            }

            // Fetch the user details from the database using the email
            User user = userRepository.findByEmail(email);

            // Store the user details in the session
            session.setAttribute("user", user);

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
            return "redirect:/user/setNew?token=" + token;
        }

        // Check if the passwords match
        if (!password.equals(confirmPassword)) {
            request.getSession().setAttribute("error", "Passwords do not match");
            return "redirect:/user/setNew?token=" + token;
        }

        // Check if password length is between 8 and 20
        if (password.length() < 8 || password.length() > 20) {
            request.getSession().setAttribute("error", "Password length should be between 8 and 20");
            return "redirect:/user/setNew?token=" + token;
        }

        // Check if password contains at least one letter and one number
        if (!password.matches(".*[A-Za-z].*") || !password.matches(".*[0-9].*")) {
            request.getSession().setAttribute("error", "Password must contain at least 1 letter and 1 number");
            return "redirect:/user/setNew?token=" + token;
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
            request.getSession().removeAttribute("resetToken");
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
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/user/editProfile")
    public ModelAndView editProfile(@RequestParam String password, @ModelAttribute User user, BindingResult bindingResult, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Get the user from the session

        User sessionUser = (User) request.getSession().getAttribute("user");
        if (password == null) {
            System.out.println("Current password is null");
        }
        if (user == null) {
            System.out.println("User is null");
        }
        System.out.println("Current password: " + password);
        System.out.println("User: " + user);
        if (!passwordEncoder.matches(password, sessionUser.getPassword())) {
            // If not, add an error message and redirect back to the profile page
            redirectAttributes.addFlashAttribute("errorMessage", "Incorrect password!");
            return new ModelAndView("redirect:/user/profile");
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
                new UsernamePasswordAuthenticationToken(sessionUser.getEmail(), currentPassword)
            );
        } catch (BadCredentialsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Incorrect password!");
            return new ModelAndView("redirect:/user/profile");
        }

        // Update the user's details
        sessionUser.setName(name);
        sessionUser.setEmail(email);
        sessionUser.setIcNo(icNo);
        sessionUser.setHpNo(hpNo);
        sessionUser.setAddress(address);
        sessionUser.setDob(dob);
        sessionUser.setHomeType(homeType);
        sessionUser.setStatus(status);

        Session session = null;
        Transaction transaction = null;

        try {
            // Get a new Hibernate session
            session = sessionFactory.openSession();

            // Start a new transaction
            transaction = session.beginTransaction();

            // Update the User object in the database
            session.update(sessionUser);
         // Get the Application object before deleting it
            Query<Application> selectQuery = session.createQuery("from Application where user.id = :userId", Application.class);
            selectQuery.setParameter("userId", sessionUser.getId());
            Application application = selectQuery.uniqueResult();

            if (application != null) {
                // Delete the files
                try {
                    if (application.getFile1() != null) {
                        Files.deleteIfExists(Paths.get(application.getFile1()));
                    }
                    if (application.getFile2() != null) {
                        Files.deleteIfExists(Paths.get(application.getFile2()));
                    }
                    if (application.getFile3() != null) {
                        Files.deleteIfExists(Paths.get(application.getFile3()));
                    }
                } catch (IOException e) {
                    // Handle exception
                    throw new RuntimeException("Error deleting files: " + e.getMessage(), e);
                }

                // Now delete the Application
                Query<?> deleteQuery = session.createQuery("delete from Application where user.id = :userId");
                deleteQuery.setParameter("userId", sessionUser.getId());
                deleteQuery.executeUpdate();
            }

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // More accurate error handling
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating profile");
            return new ModelAndView("redirect:/user/profile");
        } finally {
            if (session != null) {
                session.close();
            }
        }

        // Save the updated user back to the session
        request.getSession().setAttribute("user", sessionUser);
        redirectAttributes.addFlashAttribute("successMessage", "Profile edited successfully! Please go to Join Us to submit the Proof for a Staff to verify your credentials!");

        // Redirect to the profile page
        return new ModelAndView("redirect:/user/profile");
    }
    
    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("user/confirm")
    public String handleFormSubmission(@RequestParam(value = "credentials", required = false) MultipartFile[] files, HttpSession session, RedirectAttributes redirectAttributes) {


        System.out.println("User from session: " + session.getAttribute("user"));
        Session hibernateSession = null;
        Transaction transaction = null;

        try {
            // Get a new Hibernate session
            hibernateSession = sessionFactory.openSession();

            // Get the current user from the session and merge it into the current Hibernate session
            User currentUser = (User) session.getAttribute("user");
            currentUser = (User) hibernateSession.merge(currentUser);
            hibernateSession.refresh(currentUser);

            // Check if the form data matches the current user's information

            // Check for existing application
            if ("Pending".equals(currentUser.getStatus())) {
                redirectAttributes.addFlashAttribute("errorMessage", "You have already submitted an application.");
                return "redirect:/user/join";
            }

            // Create a new Application and set the User
            Application application = new Application();
            application.setUser(currentUser);
            application.setName(currentUser.getName());
            application.setEmail(currentUser.getEmail());
            application.setIcNo(currentUser.getIcNo());
            application.setHpNo(currentUser.getHpNo());
            application.setAddress(currentUser.getAddress());
            application.setDob(currentUser.getDob());
            application.setHomeType(currentUser.getHomeType());

            // Save files and set file locations in the application object
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i] != null && !files[i].isEmpty()) {
                    String path = fileUploadService.saveFile(files[i]);
                    switch (i) {
                        case 0:
                            application.setFile1(path);
                            break;
                        case 1:
                            application.setFile2(path);
                            break;
                        case 2:
                            application.setFile3(path);
                            break;
                    }
                    }
                }
            }

            // Save the application object to the database and update user status
            transaction = hibernateSession.beginTransaction();
            currentUser.setStatus("Pending");
            hibernateSession.save(currentUser);
            hibernateSession.save(application);
            transaction.commit();
            session.setAttribute("user", currentUser);

            redirectAttributes.addFlashAttribute("successMessage", "Application submitted successfully.");
            return "redirect:/user/join";
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            redirectAttributes.addFlashAttribute("errorMessage", "Error processing application: " + e.getMessage());
            return "redirect:/user/join";
        } finally {
            if (hibernateSession != null) {
                hibernateSession.close();
            }
        }
    }
    
    @PostMapping("/user/deleteApplication")
    public ModelAndView deleteApplication(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Get the user from the session
        User sessionUser = (User) request.getSession().getAttribute("user");

        Session session = null;
        Transaction transaction = null;

        try {
            // Get a new Hibernate session
            session = sessionFactory.openSession();

            // Start a new transaction
            transaction = session.beginTransaction();

            // Merge the detached User entity back into the session
            sessionUser = (User) session.merge(sessionUser);

            // Set the user's status to null
            sessionUser.setStatus(null);

            // Update the User object in the database
            session.update(sessionUser);

            // Get the Application object before deleting it
            Query<Application> selectQuery = session.createQuery("from Application where user.id = :userId", Application.class);
            selectQuery.setParameter("userId", sessionUser.getId());
            Application application = selectQuery.uniqueResult();

            if (application != null) {
                // Delete the files
                try {
                    if (application.getFile1() != null) {
                        Files.deleteIfExists(Paths.get(application.getFile1()));
                    }
                    if (application.getFile2() != null) {
                        Files.deleteIfExists(Paths.get(application.getFile2()));
                    }
                    if (application.getFile3() != null) {
                        Files.deleteIfExists(Paths.get(application.getFile3()));
                    }
                } catch (IOException e) {
                    // Handle exception
                    throw new RuntimeException("Error deleting files: " + e.getMessage(), e);
                }

                // Now delete the Application
                Query<?> deleteQuery = session.createQuery("delete from Application where user.id = :userId");
                deleteQuery.setParameter("userId", sessionUser.getId());
                deleteQuery.executeUpdate();
            }

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // More accurate error handling
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting application");
            return new ModelAndView("redirect:/user/join");
        } finally {
            if (session != null) {
                session.close();
            }
        }

        // Save the updated user back to the session
        request.getSession().setAttribute("user", sessionUser);
        redirectAttributes.addFlashAttribute("successMessage", "Application deleted successfully!");

        // Redirect to the profile page
        return new ModelAndView("redirect:/user/join");
    }
    
    @PostMapping("user/submitFootprints")
    public String submitFootprints(@ModelAttribute Carbon carbon, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Session session = null;
        Transaction transaction = null;
        System.out.println(carbon);  // Add this line

        try {
            // Get a new Hibernate session
            session = sessionFactory.openSession();

            // Start a new transaction
            transaction = session.beginTransaction();

            // Get the current user from the session
            User currentUser = (User) request.getSession().getAttribute("user");
            if (currentUser == null) {
                throw new Exception("Failed to get user from session");
            }
            String email = currentUser.getEmail();
            if (email == null) {
                throw new Exception("Failed to get email from user");
            }

            // Use the email to look up the user's ID
            Query<User> userQuery = session.createQuery("from User where email = :email", User.class);
            userQuery.setParameter("email", email);
            currentUser = userQuery.uniqueResult();

            // Use Hibernate to look for any carbon submissions with that user ID
            Query<Carbon> carbonQuery = session.createQuery("from Carbon where user.id = :userId", Carbon.class);
            carbonQuery.setParameter("userId", currentUser.getId());
            Carbon existingCarbon = carbonQuery.uniqueResult();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            try {
                Date waterDate = dateFormat.parse(carbon.getWaterDate());
                Date electricDate = dateFormat.parse(carbon.getElectricDate());
                Date recycleDate = dateFormat.parse(carbon.getRecycleDate());

                // Set these Date objects on your Carbon object
                carbon.setWaterDate(carbon.getWaterDate());
                carbon.setElectricDate(carbon.getElectricDate());
                carbon.setRecycleDate(carbon.getRecycleDate());
            } catch (ParseException e) {
                e.printStackTrace();
                return "errorPage";
            }

            if (existingCarbon != null) {
                // Update the existing data
                existingCarbon.setWaterUsage(carbon.getWaterUsage());
                existingCarbon.setWaterBill(carbon.getWaterBill());
                existingCarbon.setWaterDate(carbon.getWaterDate());
                existingCarbon.setWaterDays(carbon.getWaterDays());


                existingCarbon.setElectricUsage(carbon.getElectricUsage());
                existingCarbon.setElectricBill(carbon.getElectricBill());
                existingCarbon.setElectricDate(carbon.getElectricDate());
                existingCarbon.setElectricDays(carbon.getElectricDays());


                existingCarbon.setRecycleUsage(carbon.getRecycleUsage());
                existingCarbon.setRecycleBill(carbon.getRecycleBill());
                existingCarbon.setRecycleDate(carbon.getRecycleDate());
                existingCarbon.setRecycleDays(carbon.getRecycleDays());

                session.update(existingCarbon);
            } else {
                // Create a new Carbon object and store it in the database
                carbon.setUser(currentUser);
                session.save(carbon);
            }

            // Commit the transaction
            transaction.commit();

            // Add a flash message
            redirectAttributes.addFlashAttribute("message", "Submitting new information will override old ones, are you sure you want to do this?");
        } catch (Exception e) {
            // Handle exceptions
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        // Redirect to the same page
        return "redirect:/user/join";
    }
    
}