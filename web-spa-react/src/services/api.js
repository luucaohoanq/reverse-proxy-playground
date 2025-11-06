import axios from "axios";

// Get API base URL from environment variable
// Development (npm run dev): /api (Vite proxy handles it)
// Production (Docker): /api (nginx proxy handles it)
// For direct access without proxy: http://localhost:4000/api
const baseURL = import.meta.env.VITE_API_BASE_URL || "/api";

// Create axios instance with base URL pointing to the API Gateway
const api = axios.create({
  baseURL: baseURL,
  headers: {
    "Content-Type": "application/json",
    "X-From-Gateway": "true", // Required header for gateway
  },
});

// Request interceptor to add JWT token to all requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle errors and token expiration
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expired or invalid, clear storage and redirect to login
      localStorage.removeItem("accessToken");
      localStorage.removeItem("user");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export default api;
