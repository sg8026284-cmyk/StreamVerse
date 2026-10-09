<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.app.db.VideoSchema" %>

<%
    String userEmail = (session.getAttribute("userEmail") != null) ? session.getAttribute("userEmail").toString() : "";
    String userName = (session.getAttribute("userName") != null) ? session.getAttribute("userName").toString() : "Explorer";
    
    @SuppressWarnings("unchecked")
    List<VideoSchema> videos = (List<VideoSchema>) request.getAttribute("videos");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="referrer" content="no-referrer">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>StreamVerse - 3D Hub</title>
    <link rel="stylesheet" type="text/css" href="HomePage.css">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>
<body>
 
    <!-- Header Navigation -->
    <header class="header">
        <div class="header-left">
            <button class="icon-btn" id="menuBtn">
                <span class="material-icons">menu</span>
            </button>
            <a href="home" class="logo">
                <span class="material-icons yt-icon">smart_display</span>
                <span class="logo-text">StreamVerse</span>
            </a>
        </div>

        <div class="header-center">
            <div class="search-box">
                <input type="text" id="searchInput" placeholder="Search universe streams..." onkeyup="searchVideos()" autocomplete="off">
            </div>
            <button class="search-btn" onclick="searchVideos()">
                <span class="material-icons">search</span>
            </button>
            <button class="icon-btn mic-btn">
                <span class="material-icons">mic</span>
            </button>
        </div>

        <div class="header-right">
            <% if (!userEmail.isEmpty()) { %>
                <button class="icon-btn"><span class="material-icons">video_call</span></button>
                <button class="icon-btn"><span class="material-icons">notifications</span></button>
                <div class="user-avatar" title="<%= userEmail %>">
                    <%= userName.substring(0, 1).toUpperCase() %>
                </div>
                <a href="LoginPage.html" class="signout-link">Sign Out</a>
            <% } else { %>
                <a href="LoginPage.html" class="signin-btn">
                    <span class="material-icons">account_circle</span> Sign In
                </a>
            <% } %>
        </div>
    </header>

    <div class="main-layout">
        <!-- 3D Glass Sidebar -->
        <aside class="sidebar">
            <a href="home" class="nav-item active">
                <span class="material-icons">home</span>
                <span>Home</span>
            </a>
            <a href="#" class="nav-item">
                <span class="material-icons">bolt</span>
                <span>Shorts</span>
            </a>
            <a href="#" class="nav-item">
                <span class="material-icons">subscriptions</span>
                <span>Subscriptions</span>
            </a>
            <hr class="divider">
            <a href="#" class="nav-item">
                <span class="material-icons">video_library</span>
                <span>Library</span>
            </a>
            <a href="#" class="nav-item">
                <span class="material-icons">history</span>
                <span>History</span>
            </a>
            <a href="#" class="nav-item">
                <span class="material-icons">explore</span>
                <span>Explore</span>
            </a>
        </aside>

        <!-- Main Video Streaming Grid -->
        <main class="content-area">
            <div class="filter-chips">
                <button class="chip active">All</button>
                <button class="chip">Trending</button>
                <button class="chip">Music</button>
                <button class="chip">Gaming</button>
                <button class="chip">Tech 2026</button>
                <button class="chip">Animation</button>
            </div>

            <div class="video-grid" id="videoGrid">
                <% 
                    if (videos != null && !videos.isEmpty()) {
                        for (VideoSchema v : videos) { 
                            String cleanTitle = v.getVideoName().replace("\"", "").replace("'", "");
                            String vId = v.getVideoID();
                            String thumb = "https://img.youtube.com/vi/" + vId + "/hqdefault.jpg";
                %>
                    <div class="video-card" onclick="openPlayer('<%= vId %>', '<%= cleanTitle %>')">
                        <div class="thumb-container">
                            <img src="<%= thumb %>" alt="<%= cleanTitle %>" loading="lazy">
                        </div>
                        <div class="video-info">
                            <div class="avatar-circle">
                                <%= (!v.getChannelName().isEmpty()) ? v.getChannelName().substring(0, 1).toUpperCase() : "S" %>
                            </div>
                            <div class="details">
                                <h3 class="title" title="<%= cleanTitle %>"><%= cleanTitle %></h3>
                                <p class="channel"><%= v.getChannelName() %></p>
                                <p class="meta"><%= v.getViewCount() %> views</p>
                            </div>
                        </div>
                    </div>
                <% 
                        } 
                    } else { 
                %>
                    <div class="empty-state">
                        <p>No streams available in this sector. Initiating auto-refresh...</p>
                    </div>
                <% } %>
            </div>
        </main>
    </div>

    <!-- 3D Glass Modal Player -->
    <div id="videoModal" class="modal">
        <div class="modal-box">
            <div class="modal-header">
                <h3 id="modalTitle">Video Title</h3>
                <button class="close-btn" onclick="closePlayer()">✕</button>
            </div>
            <div class="video-wrapper">
                <iframe id="videoIframe" 
                        src="" 
                        frameborder="0" 
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" 
                        allowfullscreen>
                </iframe>
            </div>
        </div>
    </div>

    <script>
        function openPlayer(videoId, title) {
            if (!videoId) return;
            document.getElementById("modalTitle").innerText = title;
            var embedUrl = "https://www.youtube.com/embed/" + videoId + "?autoplay=1&rel=0";
            document.getElementById("videoIframe").src = embedUrl;
            document.getElementById("videoModal").style.display = "flex";
        }

        function closePlayer() {
            document.getElementById("videoIframe").src = "";
            document.getElementById("videoModal").style.display = "none";
        }

        function searchVideos() {
            var filter = document.getElementById("searchInput").value.toLowerCase();
            var cards = document.getElementsByClassName("video-card");

            for (var i = 0; i < cards.length; i++) {
                var text = cards[i].innerText.toLowerCase();
                cards[i].style.display = (text.indexOf(filter) > -1) ? "flex" : "none";
            }
        }

        window.onclick = function(event) {
            var modal = document.getElementById("videoModal");
            if (event.target === modal) {
                closePlayer();
            }
        };
    </script>
</body>
</html>