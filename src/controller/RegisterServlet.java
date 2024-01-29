package controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/user/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle GET request
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/user/register.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle POST request
        // Your existing code goes here
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String name = request.getParameter("name");
        String icNo = request.getParameter("icNo");
        String address = request.getParameter("address");
        String hpNo = request.getParameter("hpNo");
        String dob = request.getParameter("dob");
        String status = request.getParameter("status");
        String homeType = request.getParameter("homeType");

        Double electricityBill = null;
        String electricityBillParam = request.getParameter("electricityBill");
        if (electricityBillParam != null && !electricityBillParam.isEmpty()) {
            electricityBill = Double.parseDouble(electricityBillParam);
        }

        Double waterBill = null;
        String waterBillParam = request.getParameter("waterBill");
        if (waterBillParam != null && !waterBillParam.isEmpty()) {
            waterBill = Double.parseDouble(waterBillParam);
        }

        Double recycleWaste = null;
        String recycleWasteParam = request.getParameter("recycleWaste");
        if (recycleWasteParam != null && !recycleWasteParam.isEmpty()) {
            recycleWaste = Double.parseDouble(recycleWasteParam);
        }

        UserController userController = new UserController();
        try {
            userController.registerUser(email, password, confirmPassword, name, icNo, address, hpNo, dob, status, homeType, electricityBill, waterBill, recycleWaste);
            response.sendRedirect(request.getContextPath() + "/user/login");
            request.getSession().setAttribute("message", "Register successful, please login with the registered details");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/user/register.jsp");
            dispatcher.forward(request, response);
        }
    }
}