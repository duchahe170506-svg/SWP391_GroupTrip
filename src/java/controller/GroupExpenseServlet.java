package controller;

import dal.ExpenseDAO;
import dal.GroupMembersDAO;
import dal.TripDAO;
import dal.UserDAO;
import model.Expense;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import model.Users;

@WebServlet(name = "GroupExpenseServlet", urlPatterns = {"/group/expense"})
public class GroupExpenseServlet extends HttpServlet {

    private ExpenseDAO expenseDAO = new ExpenseDAO();
    private TripDAO tripDAO = new TripDAO();
    private UserDAO userDAO = new UserDAO();
    private GroupMembersDAO memberDAO = new GroupMembersDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int groupId;

        HttpSession session = req.getSession();
        Users user = (Users) session.getAttribute("currentUser");

        if (user == null) {
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            return;
        }

        try {
            groupId = Integer.parseInt(req.getParameter("groupId"));
        } catch (Exception e) {
            resp.sendError(400, "groupId is required");
            return;
        }

        try {
            int tripId = groupId;
            List<Expense> expenses = expenseDAO.getExpensesByTripId(tripId);
            BigDecimal totalExpense = expenseDAO.getTotalExpenseByTrip(tripId);
            BigDecimal tripBudget = tripDAO.getBudgetByTripId(tripId);
            List<Users> userList = userDAO.getUsersByTrip(tripId);
            String groupRole = memberDAO.getUserRoleInGroup(groupId, user.getUser_id());

            req.setAttribute("groupRole", groupRole);
            req.setAttribute("expenses", expenses);
            req.setAttribute("totalExpense", totalExpense);
            req.setAttribute("tripBudget", tripBudget);
            req.setAttribute("tripId", tripId);
            req.setAttribute("groupId", groupId);
            req.setAttribute("userList", userList);

            req.getRequestDispatcher("/views/group-expenses.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException("Error loading expense list", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        int tripId = Integer.parseInt(req.getParameter("tripId"));
        int groupId = Integer.parseInt(req.getParameter("groupId"));

        HttpSession session = req.getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");

        if (currentUser == null) {
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            return;
        }
        
        String groupRole = memberDAO.getUserRoleInGroup(groupId, currentUser.getUser_id());
        
        try {
            BigDecimal tripBudget = tripDAO.getBudgetByTripId(tripId);
            BigDecimal totalExpense = expenseDAO.getTotalExpenseByTrip(tripId);

            switch (action) {
                case "add":
                    Expense newE = new Expense();
                    newE.setTripId(tripId);

                   
                    if ("Leader".equals(groupRole)) {
                        newE.setPaidBy(Integer.parseInt(req.getParameter("paidBy")));
                    } else {
                        newE.setPaidBy(currentUser.getUser_id()); // Người thường luôn là chính họ
                    }

                    BigDecimal amount = new BigDecimal(req.getParameter("amount"));
                    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                        req.setAttribute("errorMessage", "Số tiền phải lớn hơn 0");
                        doGet(req, resp);
                        return;
                    }
                    if (totalExpense.add(amount).compareTo(tripBudget) > 0) {
                        req.setAttribute("errorMessage", "Tổng chi phí vượt ngân sách!");
                        doGet(req, resp);
                        return;
                    }

                    newE.setAmount(amount);
                    newE.setExpenseType(req.getParameter("expenseType"));
                    newE.setDescription(req.getParameter("description"));
                    newE.setStatus("Pending");
                    expenseDAO.addExpense(newE);
                    break;

                case "update":
                    int expenseId = Integer.parseInt(req.getParameter("expenseId"));
                    Expense oldExpense = expenseDAO.getExpenseById(expenseId);
                    BigDecimal updatedAmount = new BigDecimal(req.getParameter("amount"));
                    BigDecimal adjustedTotal = totalExpense.subtract(oldExpense.getAmount()).add(updatedAmount);
                    if (adjustedTotal.compareTo(tripBudget) > 0) {
                        req.setAttribute("errorMessage", "Tổng chi phí sau khi cập nhật vượt ngân sách!");
                        doGet(req, resp);
                        return;
                    }
                    Expense upE = new Expense();
                    upE.setExpenseId(expenseId);
                    upE.setAmount(updatedAmount);
                    upE.setPaidBy(Integer.parseInt(req.getParameter("paidBy")));
                    upE.setExpenseType(req.getParameter("expenseType"));
                    upE.setDescription(req.getParameter("description"));
                    upE.setStatus(req.getParameter("status"));
                    expenseDAO.updateExpense(upE);
                    break;

                case "delete":
                    int delExpenseId = Integer.parseInt(req.getParameter("expenseId"));
                    Expense delExpense = expenseDAO.getExpenseById(delExpenseId);
                    expenseDAO.deleteExpense(delExpenseId);
                    break;
            }

        } catch (SQLException ex) {
            throw new ServletException("Error processing request", ex);
        }

        resp.sendRedirect(req.getContextPath() + "/group/expense?groupId=" + groupId);
    }
}
