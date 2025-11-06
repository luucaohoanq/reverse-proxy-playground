import api from "./api";

export const blindboxService = {
  // Get all blindboxes
  getAll: async () => {
    const response = await api.get("/blindboxes");
    return response.data;
  },

  // Get blindbox by ID
  getById: async (id) => {
    const response = await api.get(`/blindboxes/${id}`);
    return response.data;
  },

  // Create blindbox
  create: async (blindboxData) => {
    const response = await api.post("/blindboxes", blindboxData);
    return response.data;
  },

  // Update blindbox
  update: async (id, blindboxData) => {
    const response = await api.put(`/blindboxes/${id}`, blindboxData);
    return response.data;
  },

  // Delete blindbox
  delete: async (id) => {
    const response = await api.delete(`/blindboxes/${id}`);
    return response.data;
  },

  // Get categories
  getCategories: async () => {
    const response = await api.get("/blindboxes/categories");
    return response.data;
  },

  // Get brands
  getBrands: async () => {
    const response = await api.get("/blindboxes/brands");
    return response.data;
  },
};

export default blindboxService;
