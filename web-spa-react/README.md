# BlindBox Web SPA - React Application

A modern single-page application (SPA) built with React, Vite, and Tailwind CSS for managing BlindBox inventory. This application provides user authentication and full CRUD operations for BlindBox items.

## 🚀 Features

- **Authentication**: Secure JWT-based login system
- **CRUD Operations**: Create, Read, Update, and Delete BlindBox items
- **Responsive UI**: Modern, mobile-friendly interface with Tailwind CSS
- **API Gateway Integration**: All requests routed through API Gateway with automatic JWT token injection
- **Real-time Validation**: Form validation and error handling
- **Category Management**: Support for categorizing BlindBox items

## 📋 Prerequisites

- Node.js (v16 or higher)
- npm or yarn
- API Gateway running on `localhost:4000`
- Backend microservices (MSAccount, MSBlindBox) running

## 🛠️ Installation

1. Navigate to the project directory:
```bash
cd web-spa-react
```

2. Install dependencies:
```bash
npm install
```

## 🏃 Running the Application

### Development Mode

Start the development server with hot module replacement:

```bash
npm run dev
```

The application will be available at: `http://localhost:3000`

### Production Build

Build the application for production:

```bash
npm run build
```

Preview the production build:

```bash
npm run preview
```

## 🔑 Demo Credentials

Use these predefined accounts to login:

| Email | Password | Role |
|-------|----------|------|
| admin@blindboxes.vn | @2 | Admin |
| john@blindboxes.vn | @2 | User |
| michel@blindboxes.vn | @2 | User |

## 🏗️ Project Structure

```
web-spa-react/
├── src/
│   ├── components/         # Reusable components
│   │   ├── Navbar.jsx      # Navigation bar with user info
│   │   └── PrivateRoute.jsx # Protected route wrapper
│   ├── context/            # React Context providers
│   │   └── AuthContext.jsx # Authentication state management
│   ├── pages/              # Page components
│   │   ├── Login.jsx       # Login page
│   │   ├── BlindBoxList.jsx # List all BlindBox items
│   │   ├── BlindBoxForm.jsx # Create new BlindBox
│   │   └── BlindBoxEdit.jsx # Edit existing BlindBox
│   ├── services/           # API service layer
│   │   ├── api.js          # Axios configuration
│   │   ├── authService.js  # Authentication APIs
│   │   └── blindboxService.js # BlindBox CRUD APIs
│   ├── App.jsx             # Main app with routing
│   ├── main.jsx            # Application entry point
│   └── index.css           # Global styles (Tailwind)
├── index.html              # HTML template
├── package.json            # Dependencies and scripts
├── vite.config.js          # Vite configuration
├── tailwind.config.js      # Tailwind CSS configuration
└── postcss.config.js       # PostCSS configuration
```

## 🔐 Authentication Flow

1. User enters email and password on `/login`
2. Credentials sent to `/api/auth/login` via API Gateway
3. JWT token received and stored in `localStorage`
4. Token automatically included in all subsequent API requests
5. User redirected to BlindBox list page
6. Logout clears token and redirects to login

## 🌐 API Integration

All API calls are routed through the API Gateway at `http://localhost:4000/api`

### Key Features:
- **Automatic Token Injection**: JWT token from localStorage automatically added to request headers
- **Gateway Header**: `X-From-Gateway: true` header added to all requests
- **Error Handling**: Automatic logout on 401 Unauthorized responses
- **Request/Response Interceptors**: Centralized handling via Axios

### Available Endpoints:
- `POST /api/auth/login` - User authentication
- `GET /api/blindboxes` - Get all BlindBox items
- `POST /api/blindboxes` - Create new BlindBox
- `PUT /api/blindboxes/:id` - Update existing BlindBox
- `DELETE /api/blindboxes/:id` - Delete BlindBox
- `GET /api/categories` - Get all categories

## 🎨 UI Components

### BlindBox List Page
- Grid layout displaying all BlindBox items
- Rarity color coding (Common, Rare, Epic, Legendary)
- Edit and Delete actions for each item
- "Add New BlindBox" button

### BlindBox Form (Create/Edit)
- Name input field
- Rarity dropdown (Common, Rare, Epic, Legendary)
- Price input with decimal support
- Stock quantity input
- Category selection dropdown
- Form validation with error messages

### Navigation Bar
- Purple gradient header
- Current user email display
- Logout button

## 🔧 Configuration

### API Gateway URL

Edit `src/services/api.js` to change the API Gateway URL:

```javascript
const api = axios.create({
  baseURL: 'http://localhost:4000/api', // Change this URL
  headers: {
    'Content-Type': 'application/json',
    'X-From-Gateway': 'true',
  },
});
```

### Vite Proxy

The Vite dev server proxies `/api` requests to the gateway. Edit `vite.config.js` if needed:

```javascript
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:4000',
      changeOrigin: true,
    },
  },
},
```

## 🎨 Customizing Styles

### Tailwind Colors

Edit `tailwind.config.js` to customize the primary color palette:

```javascript
theme: {
  extend: {
    colors: {
      primary: {
        50: '#faf5ff',
        // ... more shades
        900: '#4a1d96',
      },
    },
  },
},
```

## 🐛 Troubleshooting

### "Cannot connect to API Gateway"
- Ensure API Gateway is running on port 4000
- Check that backend microservices are running
- Verify network connectivity

### "401 Unauthorized" errors
- Check that you're logged in
- Verify JWT token is stored in localStorage
- Try logging out and logging back in

### "Login failed"
- Verify credentials are correct
- Check that MSAccount service is running
- Check browser console for detailed error messages

### Tailwind styles not working
- Run `npm install` to ensure PostCSS and Tailwind are installed
- Check that `index.css` is imported in `main.jsx`
- Clear browser cache and rebuild: `npm run build`

## 📦 Dependencies

### Core
- **React** 18.2.0 - UI library
- **React Router DOM** 6.20.0 - Client-side routing
- **Axios** 1.6.2 - HTTP client

### Development
- **Vite** 5.0.8 - Build tool and dev server
- **Tailwind CSS** 3.3.6 - Utility-first CSS framework
- **PostCSS** 8.4.32 - CSS transformation tool
- **Autoprefixer** 10.4.16 - CSS vendor prefixing

## 🚀 Deployment

### Build for Production

```bash
npm run build
```

The optimized files will be in the `dist/` directory.

### Deploy to Web Server

Copy the contents of `dist/` to your web server. Ensure your server is configured to:
1. Serve `index.html` for all routes (SPA routing)
2. Proxy `/api` requests to your API Gateway

### Example Nginx Configuration

```nginx
server {
    listen 80;
    server_name your-domain.com;
    root /path/to/dist;
    index index.html;

    # SPA routing
    location / {
        try_files $uri $uri/ /index.html;
    }

    # API proxy
    location /api/ {
        proxy_pass http://localhost:4000/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 📝 License

This project is part of the MSS301 course at FPT University.

## 👨‍💻 Author

SE181513

## 🤝 Contributing

This is a university project. For questions or issues, please contact the course instructor.
