package controller;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.mindrot.jbcrypt.BCrypt;

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
}