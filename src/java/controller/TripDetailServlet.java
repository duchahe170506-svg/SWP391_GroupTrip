// src/main/java/controller/TripDetailServlet.java
package controller;

import dal.TripDAO;
import dal.TripImagesDAO;
import model.Trips;
import model.TripImages;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "TripDetailServlet", urlPatterns = {"/trip/detail"})
public class TripDetailServlet extends HttpServlet {

    private final TripDAO tripDAO = new TripDAO();
    private final TripImagesDAO imagesDAO = new TripImagesDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/trips");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Trips trip = tripDAO.getTripById(id);
            if (trip == null) {
                resp.sendRedirect(req.getContextPath() + "/trips?notfound=1");
                return;
            }

            List<TripImages> images = imagesDAO.getImagesByTrip(id);

            req.setAttribute("trip", trip);
            req.setAttribute("images", images);

            req.getRequestDispatcher("/views/trip_detail.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/trips");
        }
    }
}
