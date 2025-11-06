import api from "./api";

export const authService = {
  // Login
  login: async (email, password) => {
    const response = await api.post("/auth/login", { email, password });
    if (response.data?.data?.user.role !== "ADMIN")
      throw new Error("Access denied. Admins only.");

    if (response.data?.data?.accessToken) {
      localStorage.setItem("accessToken", response.data.data.accessToken);
      localStorage.setItem("user", email);
    }
    return response.data;
  },

  // Logout
  logout: () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("user");
  },

  // Check if user is authenticated
  isAuthenticated: () => {
    return !!localStorage.getItem("accessToken");
  },

  // Get current user
  getCurrentUser: () => {
    return localStorage.getItem("user");
  },
};

export default authService;
