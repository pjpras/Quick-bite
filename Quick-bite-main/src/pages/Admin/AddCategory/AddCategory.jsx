import React, { useState } from 'react';
import './AddCategory.css';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import api from '../../../config/api';
import AdminLayout from '../../../components/Admin/AdminLayout/AdminLayout';

const AddCategory = () => {
  const navigate = useNavigate();
  
  const [formData, setFormData] = useState({
    categoryName: '',
    categoryImage: ''
  });
  
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState({});

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
    
    if (!formData.categoryName.trim()) {
      newErrors.categoryName = 'Category name is required';
    }
    
    if (!formData.categoryImage.trim()) {
      newErrors.categoryImage = 'Category image URL is required';
    } else if (!formData.categoryImage.match(/^https?:\/\/.+/i)) {
      newErrors.categoryImage = 'Please enter a valid URL starting with http:// or https://';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) return;
    
    setLoading(true);
    
    try {
      const categoryData = {
        name: formData.categoryName.trim(),
        img: formData.categoryImage.trim()
      };
      
      console.log('Sending category data:', categoryData);
      
      const response = await api.post('/app2/api/v1/category/register', categoryData);
      
      if (response.status === 201 || response.status === 200) {
        toast.success('Category added successfully!');
        handleReset();
        navigate('/admin/menumanagement');
      }
    } catch (err) {
      console.error('Failed to add category:', err);
      console.error('Error response:', err.response);
      toast.error(err.response?.data?.message || 'Failed to add category. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setFormData({
      categoryName: '',
      categoryImage: ''
    });
    setErrors({});
  };

  return (
    <AdminLayout>
      <div className="add-category">
        <div className="add-category-header">
        
          <button 
            onClick={() => navigate('/admin/menumanagement')} 
            className="back-btn"
          >
            ← Back to Menu
          </button>
        </div>

        <div className="add-category-form-container">
          <form onSubmit={handleSubmit} className="add-category-form">
            
            <div className="form-group">
              <label htmlFor="categoryName">Category Name *</label>
              <input
                type="text"
                id="categoryName"
                name="categoryName"
                value={formData.categoryName}
                onChange={handleChange}
                placeholder="Enter category name (e.g., Salad, Rolls, Deserts)"
                className={errors.categoryName ? 'error' : ''}
              />
              {errors.categoryName && <span className="error-message">{errors.categoryName}</span>}
              <small className="form-help">This will be used to group your food items</small>
            </div>

            <div className="form-group">
              <label htmlFor="categoryImage">Category Image URL *</label>
              <input
                type="text"
                id="categoryImage"
                name="categoryImage"
                value={formData.categoryImage}
                onChange={handleChange}
                placeholder="Enter image URL (e.g., https://example.com/image.jpg)"
                className={errors.categoryImage ? 'error' : ''}
              />
              {errors.categoryImage && <span className="error-message">{errors.categoryImage}</span>}
              <small className="form-help">Provide a valid URL for the category image</small>
              {formData.categoryImage && formData.categoryImage.match(/^https?:\/\/.+/i) && (
                <div className="image-preview">
                  <img 
                    src={formData.categoryImage} 
                    alt="Category preview" 
                    onError={(e) => {
                      e.target.style.display = 'none';
                    }}
                  />
                </div>
              )}
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
                {loading ? 'Adding Category...' : 'Add Category'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </AdminLayout>
  );
};

export default AddCategory;
