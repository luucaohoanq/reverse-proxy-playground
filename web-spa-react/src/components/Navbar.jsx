import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Navbar = () => {
  const { user, logout } = useAuth();

  return (
    <nav className="bg-primary-600 shadow-lg">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex items-center">
            <Link to="/" className="text-white text-2xl font-bold flex items-center">
              <span className="mr-2">🎁</span>
              BlindBox Manager
            </Link>
          </div>
          <div className="flex items-center space-x-4">
            <span className="text-white">Welcome, {user}</span>
            <button
              onClick={logout}
              className="bg-white text-primary-600 px-4 py-2 rounded-lg hover:bg-gray-100 transition duration-200 font-medium"
            >
              Logout
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
