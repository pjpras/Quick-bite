import React, { useState, useEffect } from 'react';
import './AddProducts.css';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import api from '../../../config/api';
import AdminLayout from '../../../components/Admin/AdminLayout/AdminLayout';

const AddProduct = () => {
  const navigate = useNavigate();
  
  const [formData, setFormData] = useState({
    name: '',
    image: '',
    price: '',
    description: '',
    category: ''
  });
  
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState({});
  const [categories, setCategories] = useState([]);


  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await api.get('/app2/api/v1/category');
        console.log('Fetched categories raw response:', response);
        console.log('Fetched categories data:', response.data);
        console.log('Categories array:', response.data);
        
        if (Array.isArray(response.data)) {
          setCategories(response.data);
        } else {
          console.error('Response data is not an array:', response.data);
        }
      } catch (err) {
        console.error('Failed to fetch categories:', err);
        console.error('Error details:', err.response);
      }
    };
    
    fetchCategories();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };


  const validateForm = () => {
    const newErrors = {};
    
    if (!formData.name.trim()) newErrors.name = 'Product name is required';
    if (!formData.image.trim()) newErrors.image = 'Image URL is required';
    if (!formData.price || formData.price <= 0) newErrors.price = 'Valid price is required';
    if (!formData.description.trim()) newErrors.description = 'Description is required';
    if (!formData.category.trim()) newErrors.category = 'Category is required';
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

 
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) return;
    
    setLoading(true);
    
    try {
     
      const foodData = {
        name: formData.name.trim(),
        img: formData.image.trim(),
        price: parseFloat(formData.price),
        description: formData.description.trim(),
        status: true, 
        categoryName: formData.category.trim()
      };
      
      console.log('Sending food data:', foodData);
      
      const response = await api.post('/app2/api/v1/food/register', foodData);
      
      if (response.status === 201) {
        toast.success('Food item added successfully!');
        handleReset();
        navigate('/admin/menumanagement');
      }
    } catch (err) {
      console.error('Failed to add food:', err);
      console.error('Error response:', err.response);
      toast.error(err.response?.data?.message || 'Failed to add food item. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  
  const handleReset = () => {
    setFormData({
      name: '',
      image: '',
      price: '',
      description: '',
      category: ''
    });
    setErrors({});
  };

  return (
    <AdminLayout>
      <div className="add-product">
        <div className="add-product-header">
          <div className="header-content">
            
            
        </div>
        <button 
          onClick={() => navigate('/admin/menumanagement')} 
          className="back-btn"
        >
          ← Back to Menu
        </button>
      </div>

      <div className="add-product-form-container">
        <form onSubmit={handleSubmit} className="add-product-form">
        
          <div className="form-group">
            <label htmlFor="name">Product Name *</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              placeholder="Enter product name"
              className={errors.name ? 'error' : ''}
            />
            {errors.name && <span className="error-message">{errors.name}</span>}
          </div>

         
          <div className="form-group">
            <label htmlFor="image">Image  *</label>
            <input
              type="text"
              id="image"
              name="image"
              value={formData.image}
              onChange={handleChange}
              placeholder="Give image url"
              className={errors.image ? 'error' : ''}
            />
            {errors.image && <span className="error-message">{errors.image}</span>}
            <small className="form-help"></small>
          </div>

         
          <div className="form-group">
            <label htmlFor="price">Price (₹) *</label>
            <input
              type="number"
              id="price"
              name="price"
              value={formData.price}
              onChange={handleChange}
              placeholder="55"
              min="1"
              className={errors.price ? 'error' : ''}
            />
            {errors.price && <span className="error-message">{errors.price}</span>}
          </div>

         
          <div className="form-group">
            <label htmlFor="category">Category *</label>
            <select
              id="category"
              name="category"
              value={formData.category}
              onChange={handleChange}
              className={errors.category ? 'error' : ''}
            >
              <option value="">Select a category</option>
              {categories.length > 0 ? (
                categories.map((cat) => {
                  const catName = cat.categoryName || cat.name;
                  console.log('Rendering category:', cat, 'Name:', catName);
                  return (
                    <option key={cat.id} value={catName}>
                      {catName}
                    </option>
                  );
                })
              ) : (
                <option value="" disabled>No categories available</option>
              )}
            </select>
            {errors.category && <span className="error-message">{errors.category}</span>}
            <small className="form-help">{categories.length} categories loaded</small>
          </div>

        
          <div className="form-group">
            <label htmlFor="description">Description *</label>
            <textarea
              id="description"
              name="description"
              value={formData.description}
              onChange={handleChange}
              placeholder="Enter product description"
              rows="4"
              className={errors.description ? 'error' : ''}
            />
            {errors.description && <span className="error-message">{errors.description}</span>}
          </div>

        
          <div className="form-actions">
            <button 
              type="button" 
              onClick={handleReset} 
              className="reset-btn"
              disabled={loading}
            >
              Reset
            </button>
            <button 
              type="submit" 
              className="submit-btn" 
              disabled={loading}
            >
              {loading ? 'Adding Product...' : 'Add Product'}
            </button>
          </div>
        </form>

       
   
      </div>
    </div>
    </AdminLayout>
  );
};

export default AddProduct;