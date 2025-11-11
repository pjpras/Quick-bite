

import React, { useState, useEffect } from 'react';
import './ViewCustomers.css';
import axios from 'axios';
import AdminLayout from '../../../components/Admin/AdminLayout/AdminLayout';
import api from '../../../config/api';

const ViewCustomers = () => {
  const [customers, setCustomers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [isSearching, setIsSearching] = useState(false);

  useEffect(() => {
    fetchAllCustomers();
  }, []);

  const fetchAllCustomers = async () => {
    try {
     
      const res = await api.get('/app1/api/v1/users/customers/active');
      console.log('Fetched active customers:', res.data);
      setCustomers(res.data);
    } catch (err) {
      console.error('Failed to fetch customers', err);
    }
  };

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
    
      fetchAllCustomers();
      return;
    }

    setIsSearching(true);
    try {
      
      const res = await api.get(`/app1/api/v1/users/customers/search?name=${searchTerm}`);
      console.log('Search results:', res.data);
      setCustomers(res.data);
    } catch (err) {
      console.error('Failed to search customers', err);
      
    } finally {
      setIsSearching(false);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  const deleteCustomer = async (customerId, customerName) => {
    if (window.confirm(`Are you sure you want to delete customer "${customerName}"?`)) {
      try {
       
        const res = await api.patch(`/app1/api/v1/users/status/${customerId}?status=false`);
        if (res.status === 200) {
         
          setCustomers(customers.filter(customer => customer.id !== customerId));
       
        }
      } catch (err) {
        console.error('Failed to delete customer', err);
      
      }
    }
  };

  return (
    <AdminLayout>
      <div className="view-customers">
        <div className="customers-header">
          <h1>Customer Management</h1>
          <p>View and manage registered customers</p>
        </div>



      <div className="search-section">
        <div className="search-bar">
          <input
            type="text"
            placeholder="Search customers by name..."
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
                fetchAllCustomers();
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
          <h2>All Customers ({customers.length})</h2>
        </div>

        {customers.length === 0 ? (
          <div className="no-customers">
            <p>No customers found matching your search criteria.</p>
          </div>
        ) : (
          <div className="customers-grid">
            {customers.map(customer => (
              <div key={customer.id} className="customer-card">
                <div className="customer-header">
                  <div className="customer-avatar">
                   {customer?.name && customer.name.trim() ? customer.name.charAt(0).toUpperCase() : ''}
                  </div>
                  <div className="customer-basic-info">
                    <h3>{customer?.name || 'N/A'}</h3>
                    <span className="customer-id">ID: #{customer.id}</span>
                  </div>
                 
                </div>

                <div className="customer-details">
                  <div className="detail-row">
                    <span className="detail-label">Email:</span>
                    <span className="detail-value">{customer.email}</span>
                  </div>
                  <div className="detail-row">
                    <span className="detail-label">Phone:</span>
                    <span className="detail-value">{customer.phno || 'N/A'}</span>
                  </div>
                  <div className="detail-row">
                    <span className="detail-label">Location:</span>
                    <span className="detail-value">{customer.location || 'N/A'}</span>
                  </div>
                  <div className="detail-row">
                    <span className="detail-label">Total Orders:</span>
                    <span className="detail-value total-orders">{customer.totalOrders || 0}</span>
                  </div>
                 
                </div>

                <div className="customer-actions">
                  
                  <button 
                    className="action-btn delete-btn"
                    onClick={() => deleteCustomer(customer.id, customer?.name || 'Unknown Customer')}
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

export default ViewCustomers; 