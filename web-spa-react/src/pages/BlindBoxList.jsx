import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import blindboxService from '../services/blindboxService';

const BlindBoxList = () => {
  const [blindboxes, setBlindboxes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchBlindBoxes();
  }, []);

  const fetchBlindBoxes = async () => {
    try {
      const response = await blindboxService.getAll();
      setBlindboxes(response.data || []);
    } catch (err) {
      setError('Failed to load blind boxes: ' + err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this BlindBox?')) {
      return;
    }

    try {
      await blindboxService.delete(id);
      setBlindboxes(blindboxes.filter((box) => box.id !== id));
    } catch (err) {
      alert('Failed to delete blind box: ' + err.message);
    }
  };

  const getRarityColor = (rarity) => {
    switch (rarity) {
      case 'Rare':
        return 'bg-yellow-200 text-yellow-800';
      case 'Epic':
        return 'bg-purple-200 text-purple-800';
      case 'Legendary':
        return 'bg-red-200 text-red-800';
      default:
        return 'bg-gray-200 text-gray-800';
    }
  };

  if (loading) {
    return (
      <>
        <Navbar />
        <div className="min-h-screen flex items-center justify-center">
          <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-primary-600"></div>
        </div>
      </>
    );
  }

  return (
    <>
      <Navbar />
      <div className="bg-gray-50 min-h-screen">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          {/* Header */}
          <div className="flex justify-between items-center mb-8">
            <h1 className="text-3xl font-bold text-gray-900">BlindBox Inventory</h1>
            <Link
              to="/create"
              className="bg-primary-600 hover:bg-primary-700 text-white font-bold py-3 px-6 rounded-lg transition duration-200 transform hover:scale-105 shadow-lg flex items-center"
            >
              <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 4v16m8-8H4"></path>
              </svg>
              Add New BlindBox
            </Link>
          </div>

          {/* Error Message */}
          {error && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
              {error}
            </div>
          )}

          {/* BlindBox Grid */}
          {blindboxes.length === 0 ? (
            <div className="text-center py-16">
              <svg className="mx-auto h-24 w-24 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4"></path>
              </svg>
              <h3 className="mt-4 text-lg font-medium text-gray-900">No BlindBoxes</h3>
              <p className="mt-2 text-gray-500">Get started by creating a new BlindBox.</p>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {blindboxes.map((box) => (
                <div key={box.id} className="bg-white rounded-lg shadow-md hover:shadow-xl transition duration-300 overflow-hidden">
                  <div className="bg-gradient-to-r from-primary-500 to-blue-500 p-4">
                    <h3 className="text-white text-xl font-bold truncate">{box.name}</h3>
                  </div>
                  <div className="p-6">
                    <div className="space-y-3">
                      <div className="flex justify-between items-center">
                        <span className="text-gray-600">Rarity:</span>
                        <span className={`px-3 py-1 rounded-full text-sm font-semibold ${getRarityColor(box.rarity)}`}>
                          {box.rarity}
                        </span>
                      </div>
                      <div className="flex justify-between items-center">
                        <span className="text-gray-600">Price:</span>
                        <span className="text-lg font-bold text-green-600">${box.price}</span>
                      </div>
                      <div className="flex justify-between items-center">
                        <span className="text-gray-600">Stock:</span>
                        <span className="font-semibold">{box.stock}</span>
                      </div>
                      {box.categoryName && (
                        <div className="flex justify-between items-center">
                          <span className="text-gray-600">Category:</span>
                          <span className="font-semibold">{box.categoryName}</span>
                        </div>
                      )}
                      {box.brandName && (
                        <div className="flex justify-between items-center">
                          <span className="text-gray-600">Brand:</span>
                          <span className="font-semibold">{box.brandName}</span>
                        </div>
                      )}
                    </div>

                    <div className="mt-6 flex space-x-3">
                      <Link
                        to={`/edit/${box.id}`}
                        className="flex-1 bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded-lg text-center transition duration-200"
                      >
                        Edit
                      </Link>
                      <button
                        onClick={() => handleDelete(box.id)}
                        className="flex-1 bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded-lg text-center transition duration-200"
                      >
                        Delete
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </>
  );
};

export default BlindBoxList;
