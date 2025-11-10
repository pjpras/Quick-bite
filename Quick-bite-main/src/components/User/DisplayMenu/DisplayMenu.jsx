import React, { useState, useEffect } from 'react';
import './DisplayMenu.css';
import FoodItem from '../FoodItem/FoodItem';
import api from '../../../config/api';

const ITEMS_PER_PAGE = 8;

const DisplayMenu = ({ category }) => {
  const [foodList, setFoodList] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        setLoading(true);
       
        const res = await api.get('/app2/api/v1/food/active');
        console.log('Fetched active food items:', res.data);
        console.log('Number of items:', res.data.length);
        console.log('Sample item structure:', res.data[0]);
        setFoodList(res.data);
      } catch (err) {
        console.error('Failed to fetch products', err);
        console.error('Error details:', err.response?.data);
        console.error('Error status:', err.response?.status);
      } finally {
        setLoading(false);
      }
    };
    fetchProducts();
  }, []);

  const filteredList = foodList.filter(item => 
    category === "all" || category === item.category?.name
  );

  const totalPages = Math.ceil(filteredList.length / ITEMS_PER_PAGE);
  const startIdx = (currentPage - 1) * ITEMS_PER_PAGE;
  const endIdx = startIdx + ITEMS_PER_PAGE;
  const paginatedItems = filteredList.slice(startIdx, endIdx);

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

  return (
    <div className='food-display' id='food-display'>
      <h2>The best dishes from our restaurant</h2>
      <div className='food-display-list'>
        {paginatedItems.map((item) => (
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

      {totalPages > 1 && (
        <div className="pagination">
          <button
            disabled={currentPage === 1}
            onClick={() => setCurrentPage(currentPage - 1)}
          >
            Prev
          </button>
          {[...Array(totalPages)].map((_, idx) => (
            <button
              key={idx + 1}
              className={currentPage === idx + 1 ? 'active' : ''}
              onClick={() => setCurrentPage(idx + 1)}
            >
              {idx + 1}
            </button>
          ))}
          <button
            disabled={currentPage === totalPages}
            onClick={() => setCurrentPage(currentPage + 1)}
          >
            Next
          </button>
        </div>
      )}
    </div>
  );
};

export default DisplayMenu;