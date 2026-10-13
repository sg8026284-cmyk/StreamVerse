package com.app.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import com.app.db.SpotifyDatabase;
import com.app.db.VideoSchema;
import com.app.helpers.YTDataApi;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userEmail") == null) {
            response.sendRedirect("LoginPage.html");
            return;
        }

        List<VideoSchema> videos = SpotifyDatabase.getvideos();

        // If DB is empty, pull 24 items from YouTube Data API
        if (videos == null || videos.isEmpty()) {
            videos = YTDataApi.getSongs("popular");
            if (videos != null && !videos.isEmpty()) {
                SpotifyDatabase.saveVideos(videos);
            }
        }

        request.setAttribute("videos", videos);
        request.getRequestDispatcher("HomePage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}