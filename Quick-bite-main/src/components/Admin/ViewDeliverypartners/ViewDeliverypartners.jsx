

import React, { useState, useEffect } from 'react';
import './ViewDeliverypartners.css';
import AdminLayout from '../AdminLayout/AdminLayout';
import api from '../../../config/api';

const ViewDeliverypartners = () => {
  const [partners, setPartners] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [isSearching, setIsSearching] = useState(false);

  useEffect(() => {
    fetchAllPartners();
  }, []);

  const fetchAllPartners = async () => {
    try {
     
      const res = await api.get('/app1/api/v1/users/deliverypartners/active');
      console.log('Fetched active delivery partners:', res.data);
      setPartners(res.data);
    } catch (err) {
      console.error('Failed to fetch delivery partners', err);
    }
  };

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      
      fetchAllPartners();
      return;
    }

    setIsSearching(true);
    try {
      
      const res = await api.get(`/app1/api/v1/users/deliverypartners/search?name=${searchTerm}`);
      console.log('Search results:', res.data);
      setPartners(res.data);
    } catch (err) {
      console.error('Failed to search delivery partners', err);
    
    } finally {
      setIsSearching(false);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  const deletePartner = async (partnerId, partnerName) => {
    if (window.confirm(`Are you sure you want to delete delivery partner "${partnerName}"?`)) {
      try {
        
        const res = await api.patch(`/app1/api/v1/users/status/${partnerId}?status=false`);
        if (res.status === 200) {
         
          setPartners(partners.filter(partner => partner.id !== partnerId));
       
        }
      } catch (err) {
        console.error('Failed to delete delivery partner', err);
    
      }
    }
  };

  return (
    <AdminLayout>
      <div className="view-customers">
        <div className="customers-header">
          <h1>Delivery Partner Management</h1>
          <p>View and manage registered Delivery Partner</p>
        </div>



      <div className="search-section">
        <div className="search-bar">
          <input
            type="text"
            placeholder="Search delivery partners by name..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            onKeyPress={handleKeyPress}
            className="search-input"
          />
          <button 
            onClick={handleSearch} 
            className="search-button"
            disabled={isSearching}
          >
            {isSearching ? 'Searching...' : 'Search'}
          </button>
          {searchTerm && (
            <button 
              onClick={() => {
                setSearchTerm('');
                fetchAllPartners();
              }} 
              className="clear-button"
            >
              Clear
            </button>
          )}
        </div>
      </div>

      <div className="customers-section">
        <div className="section-header">
          <h2>All Delivery Partners ({partners.length})</h2>
        </div>

        {partners.length === 0 ? (
          <div className="no-customers">
            <p>No delivery partners found matching your search criteria.</p>
          </div>
        ) : (
          <div className="customers-grid">
            {partners.map(partner => (
              <div key={partner.id} className="customer-card">
                <div className="customer-header">
                  <div className="customer-avatar">
                    {partner.name.charAt(0).toUpperCase()}
                  </div>
                  <div className="customer-basic-info">
                    <h3>{partner.name}</h3>
                    <span className="customer-id">ID: #{partner.id}</span>
                  </div>
                  {partner.availabilityStatus !== null && (
                    <span className={`availability-badge ${partner.availabilityStatus ? 'available' : 'unavailable'}`}>
                      {partner.availabilityStatus ? 'Available' : 'Unavailable'}
                    </span>
                  )}
                </div>

                <div className="customer-details">
                  <div className="detail-row">
                    <span className="detail-label">Email:</span>
                    <span className="detail-value">{partner.email}</span>
                  </div>
                  <div className="detail-row">
                    <span className="detail-label">Phone:</span>
                    <span className="detail-value">{partner.phno || 'N/A'}</span>
                  </div>
                  <div className="detail-row">
                    <span className="detail-label">Location:</span>
                    <span className="detail-value">{partner.location || 'N/A'}</span>
                  </div>
                  <div className="detail-row">
                    <span className="detail-label">Total Orders:</span>
                    <span className="detail-value total-orders">{partner.totalOrders || 0}</span>
                  </div>
                </div>

                <div className="customer-actions">
                  <button 
                    className="action-btn delete-btn"
                    onClick={() => deletePartner(partner.id, partner.name)}
                  >
                    Delete
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
    </AdminLayout>
  );
};

export default ViewDeliverypartners; 