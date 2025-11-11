import React, { useState, useEffect } from 'react';
import './DisplayMenu.css';
import FoodItem from '../FoodItem/FoodItem';
import api from '../../../config/api';

const ITEMS_PER_PAGE = 8;

const DisplayMenu = ({ category }) => {
  const [foodList, setFoodList] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchProducts();
  }, [currentPage, category]);

  const fetchProducts = async () => {
    try {
      setLoading(true);
      // Send the page number directly (1 for page 1, 2 for page 2, etc.)
      console.log(`Fetching page ${currentPage}`);
      const res = await api.get(`/app2/api/v1/food/active?page=${currentPage}`);
      console.log('API Response:', res.data);
      console.log('Content array:', res.data.content);
      console.log('Total pages:', res.data.totalPages);
      console.log('Total elements:', res.data.totalElements);
      
      // Use paginated response from backend
      setFoodList(res.data.content || []);
      setTotalPages(res.data.totalPages || 1);
    } catch (err) {
      console.error('Failed to fetch products', err);
      console.error('Error details:', err.response?.data);
      console.error('Error status:', err.response?.status);
      setFoodList([]);
      setTotalPages(1);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className='food-display' id='food-display'>
        <h2>Loading menu...</h2>
      </div>
    );
  }

  if (foodList.length === 0) {
    return (
      <div className='food-display' id='food-display'>
        <h2>No food items available</h2>
        <p>Please check back later or contact support.</p>
      </div>
    );
  }

  const handlePageChange = (page) => {
    setCurrentPage(page);
    // Scroll to top of food display
    document.getElementById('food-display')?.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <div className='food-display' id='food-display'>
      <h2>The best dishes from our restaurant</h2>
      <div className='food-display-list'>
        {foodList.map((item) => (
          <FoodItem
            key={item.id}
            id={item.id}
            name={item.name}
            price={item.price}
            description={item.description}
            image={item.img}
            avgRating={item.avgRating}
            category={item.category}
          />
        ))}
      </div>

      <div className="pagination">
        <button
          disabled={currentPage === 1}
          onClick={() => handlePageChange(currentPage - 1)}
        >
          Prev
        </button>
        {Array.from({ length: totalPages }, (_, i) => i + 1).map((pageNum) => (
          <button
            key={pageNum}
            className={currentPage === pageNum ? 'active' : ''}
            onClick={() => handlePageChange(pageNum)}
          >
            {pageNum}
          </button>
        ))}
        <button
          disabled={currentPage === totalPages}
          onClick={() => handlePageChange(currentPage + 1)}
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default DisplayMenu;