import React, { useState, useEffect } from 'react';
import './MenuManagement.css';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import api from '../../../config/api';
import { assets } from '../../../assets/assets';
import AdminLayout from '../../../components/Admin/AdminLayout/AdminLayout';

const MenuManagement = () => {
  const navigate = useNavigate();
  const [foodlist,setFoodList]=useState([]);
  const [categories,setCategory]=useState([]);

 useEffect(() => {
    const fetchProducts = async () => {
      try {
        const res = await api.get('/app2/api/v1/food');
        console.log('Fetched food items:', res.data);
        setFoodList(res.data);
      } catch (err) {
        console.error('Failed to fetch food items', err);
        toast.error('Failed to load food items');
      }
    };
    
    fetchProducts();
    
  }, []);

   useEffect(() => {
    const fetchCategory = async () => {
      try {
        const res = await api.get('/app2/api/v1/category');
        console.log('Fetched categories:', res.data);
        setCategory(res.data);
      } catch (err) {
        console.error('Failed to fetch categories', err);
       
      }
    };
    
    fetchCategory();
    
  }, []);


const handledelete = async (id) => {
  try {
    const confirmDelete = window.confirm('Are you sure you want to mark this food item as out of stock?');
    if (!confirmDelete) return;

   
    const response = await api.patch(`/app2/api/v1/food/update/status?id=${id}&status=false`);
    
    if (response.status === 200) {
    
      setFoodList(foodlist.map(product => 
        product.id === id ? { ...product, status: false } : product
      ));
      toast.success('Food item marked as out of stock!');
    }
  } catch (err) {
    console.error('Failed to update food status:', err);
    toast.error('Failed to update food status. Please try again.');
  }
};

const handleRestock = async (id) => {
  try {
    const confirmRestock = window.confirm('Are you sure you want to mark this food item as in stock?');
    if (!confirmRestock) return;

    const response = await api.patch(`/app2/api/v1/food/update/status?id=${id}&status=true`);
    
    if (response.status === 200) {
   
      setFoodList(foodlist.map(product => 
        product.id === id ? { ...product, status: true } : product
      ));
      toast.success('Food item marked as in stock!');
    }
  } catch (err) {
    console.error('Failed to update food status:', err);
    toast.error('Failed to update food status. Please try again.');
  }
};


  const [selectedCategory, setSelectedCategory] = useState('All');

  

  const filteredProducts = foodlist.filter(product => {
    if (selectedCategory === 'All') return true;
    const categoryName = product.category?.categoryName || product.category?.name;
    return categoryName === selectedCategory;
  });

  return (
    <AdminLayout>
      <div className="view-products">
        <div className="products-header">
          <h1>Menu Management</h1>
          
          <p>Manage your restaurant menu and food items</p>
        </div>

      <div className="products-filters">
        <div className="filter-section">
          <label>Category:</label>
          <select
            value={selectedCategory}
            onChange={(e) => setSelectedCategory(e.target.value)}
            className="filter-select"
          >
            <option key="all" value="All">All</option>
            {categories.map((category, index) => {
              const catName = category.categoryName || category.name;
              return (
                <option key={`${catName}-${index}`} value={catName}>
                  {catName}
                </option>
              );
            })}
          </select>
        </div>

        <button
          onClick={() => navigate('/admin/add-product')}
          className="add-menu-btn"
        >
          Add Menu
        </button>

        <button
          onClick={() => navigate('/admin/add-category')}
          className="add-menu-btn"
        >
          Add Category
        </button>

        <div className="products-count">
          <span>{filteredProducts.length} items found</span>
        </div>
      </div>

      <div className="products-grid">
        {filteredProducts.map(product => (
          <div key={product.id} className="product-card">
            <div className="product-image-container">
              <img 
                src={product.img} 
                alt={product.name}
                className="product-image"
                onError={(e) => {
                  if (!e.target.dataset.errorHandled) {
                    e.target.dataset.errorHandled = 'true';
                    e.target.src = 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="300" height="200"%3E%3Crect width="300" height="200" fill="%23f0f0f0"/%3E%3Ctext x="50%25" y="50%25" dominant-baseline="middle" text-anchor="middle" font-family="Arial" font-size="16" fill="%23999"%3ENo Image%3C/text%3E%3C/svg%3E';
                  }
                }}
              />
            </div>

            <div className="product-content">
              <div className="product-header">
                <h3 className="product-name">{product.name}</h3>
                {product.status ? (
                  <span className="status-badge available">In Stock</span>
                ) : (
                  <span className="status-badge unavailable">Out of Stock</span>
                )}
              </div>

              <p className="product-description">{product.description}</p>

              <div className="product-details">
                <div className="product-category">
                  <span className="category-tag">{product.category?.categoryName || product.category?.name || 'No Category'}</span>
                </div>
                <div className="product-rating">
                  <span className="rating-icon">⭐</span>
                  <span>{product.avgRating || 0}/5</span>
                </div>
              </div>

              <div className="product-footer">
                <div className="product-price">
                  <span className="price">{product.price}</span>
                </div>
                <div className="product-actions">
                  <button onClick={() => navigate('/admin/edit-product', { state: { product } })} className="action-btn edit-btn">Edit</button>
                  {product.status ? (
                    <button onClick={() => handledelete(product.id)} className="action-btn delete-btn">Out of Stock</button>
                  ) : (
                    <button onClick={() => handleRestock(product.id)} className="action-btn restock-btn">In Stock</button>
                  )}
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>

      {filteredProducts.length === 0 && (
        <div className="no-products">
          <h3>No products found</h3>
          <p>Try adjusting your filters or add new products to your menu</p>
        </div>
      )}
    </div>
    </AdminLayout>
  );
};

export default MenuManagement;