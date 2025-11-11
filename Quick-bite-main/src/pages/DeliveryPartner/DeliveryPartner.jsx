import React, { useState, useEffect } from 'react';
import './DeliveryPartner.css';
import axios from 'axios';
import api from '../../config/api';
import { useNavigate } from 'react-router-dom';

const DeliveryPartner = () => {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('orders');
  const [orders, setOrders] = useState([]);
  const [partnerData, setPartnerData] = useState(null);
  const [isAvailable, setIsAvailable] = useState(false);
  const [otpInput, setOtpInput] = useState('');
  const [showOtpModal, setShowOtpModal] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [updateFormData, setUpdateFormData] = useState({
    id: '',
    email: '',
    password: '',
    name: '',
    phno: '',
    location: ''
  });

  const [partnerId, setPartnerId] = useState(() => {
    // Try to get userId from localStorage
    const storedUserId = localStorage.getItem('userId');
    if (storedUserId) {
      return storedUserId;
    }
    
    // Fallback: try to get from currentUser object
    const currentUser = localStorage.getItem('currentUser');
    if (currentUser) {
      try {
        const user = JSON.parse(currentUser);
        return user.id?.toString() || null;
      } catch (e) {
        console.error('Error parsing currentUser:', e);
      }
    }
    
    // If still no ID, log error
    console.error('No userId found in localStorage! User needs to login.');
    return null;
  }); 

  useEffect(() => {
    console.log('DeliveryPartner component mounted with partnerId:', partnerId);
    console.log('Initial isAvailable state:', isAvailable);
    
    if (!partnerId) {
      console.error('No partnerId available, redirecting to login');
      navigate('/login');
      return;
    }
    
    fetchPartnerData();
    fetchAssignedOrders();
  }, []);

  useEffect(() => {
    console.log('isAvailable state changed to:', isAvailable);
  }, [isAvailable]);

  const fetchPartnerData = async () => {
    try {
      const token = localStorage.getItem('jwtToken');
      console.log(`Making API call to: /app1/api/v1/users/${partnerId}`);
      console.log('Using token:', token ? 'Token exists' : 'No token found');
      
      const res = await api.get(`/app1/api/v1/users/${partnerId}`);
      
      console.log('Full API Response:', res);
      console.log('Response Data:', res.data);
      console.log('Phone field (phno):', res.data.phno);
      console.log('Phone field (phone):', res.data.phone);
      console.log('Phone field (phoneNumber):', res.data.phoneNumber);
      console.log('Location field (location):', res.data.location);
      console.log('Location field (address):', res.data.address);
      console.log('availabilityStatus from API:', res.data.availabilityStatus);
      console.log('Type of availabilityStatus:', typeof res.data.availabilityStatus);
      
      // Normalize the data to handle different field names from backend
      const normalizedData = {
        ...res.data,
        phno: res.data.phno || res.data.phone || res.data.phoneNumber,
        location: res.data.location || res.data.address
      };
      
      console.log('Normalized partner data:', normalizedData);
      setPartnerData(normalizedData);
      
     
      if (res.data.id && res.data.id.toString() !== partnerId) {
        console.log(`Updating partnerId from ${partnerId} to ${res.data.id}`);
        setPartnerId(res.data.id.toString());
        localStorage.setItem('userId', res.data.id.toString());
      }
      
      const isAvailableValue = res.data.availabilityStatus === true;
      console.log('Setting isAvailable to:', isAvailableValue);
      setIsAvailable(isAvailableValue);
    } catch (err) {
      console.error('API Error Details:', err);
      console.error('Error Response:', err.response);
      console.error('Error Message:', err.message);
      console.error('Error Status:', err.response?.status);
      if (err.response?.status === 403) {
        
        console.warn('Authentication warning - but staying logged in');
      }
    }
  };

  const fetchAssignedOrders = async () => {
    try {
      const res = await api.get('/app2/api/v1/orders/all');
      console.log('Assigned orders from backend:', res.data);
      setOrders(res.data);
    } catch (err) {
      console.error('Failed to fetch orders', err);
    }
  };

  const toggleAvailability = async () => {
    const newAvailability = !isAvailable;
    try {
      await api.put(`/app1/api/v1/users/availability/${partnerId}?available=${newAvailability}`);
      setIsAvailable(newAvailability);
      setPartnerData(prev => ({ ...prev, availabilityStatus: newAvailability }));
     
    } catch (err) {
      console.error('Failed to update availability', err);
      if (err.response?.status === 403) {
        
        console.warn('Availability update authentication warning');
      }
    }
  };

  const handleMarkAsDelivered = (order) => {
    setSelectedOrder(order);
    setShowOtpModal(true);
  };

  const verifyOtpAndDeliver = async () => {
    if (!otpInput || otpInput.length !== 6) {
      
      return;
    }

    try {
      await api.patch(`/app2/api/v1/orders/Deliverypartner/ordercompleted?orderId=${selectedOrder.id}&otp=${otpInput}`);
      
      setOrders(orders.filter(order => order.id !== selectedOrder.id));
      setShowOtpModal(false);
      setOtpInput('');
      setSelectedOrder(null);
      
      
      window.location.reload();
    } catch (err) {
      console.error('Failed to deliver order', err);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('userId');
    navigate('/login');
  };

  const handleUpdateProfile = () => {
   
    setUpdateFormData({
      id: partnerData?.id || partnerId,
      email: partnerData?.email || '',
      password: '', 
      name: partnerData?.name || '',
      phno: partnerData?.phno || '',
      location: partnerData?.location || ''
    });
    setShowUpdateModal(true);
  };

  const handleUpdateFormChange = (e) => {
    const { name, value } = e.target;
    setUpdateFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const submitProfileUpdate = async () => {
    try {
      const token = localStorage.getItem('jwtToken');
      
      
      console.log('Starting profile update...');
      console.log('Partner ID:', partnerId);
      console.log('Current partner data BEFORE update:', partnerData);
      console.log('Update form data being sent:', JSON.stringify(updateFormData, null, 2));
      console.log('JWT Token exists:', !!token);
    
      if (!token) {
        alert('Authentication token not found. Please login again.');
        navigate('/login');
        return;
      }
     
      if (!updateFormData.email || !updateFormData.name || !updateFormData.phno) {
        alert('Email, Name, and Phone are required fields');
        return;
      }

     
      const dataToSend = { 
        ...updateFormData,
       
        phone: updateFormData.phno, 
        phoneNumber: updateFormData.phno, 
        address: updateFormData.location, 
      };
      
      if (!dataToSend.password || dataToSend.password.trim() === '') {
        delete dataToSend.password;
      }
      
      console.log('Final data being sent to API (with alternate field names):', JSON.stringify(dataToSend, null, 2));

      
      let response;
      let apiUrl;
      
      try {
        apiUrl = `/app1/api/v1/users/deliverypartner/update`;
        response = await api.put(apiUrl, dataToSend);
      } catch (error1) {
        console.error('Error details:', error1.response?.data);
        alert('Failed to update profile. Please try again.');
        return;
      }

      console.log('Profile update response status:', response.status);
      console.log('Profile update response data:', JSON.stringify(response.data, null, 2));
    
      const currentToken = localStorage.getItem('jwtToken');
      if (currentToken) {
        console.log('Maintaining authentication session');
      }
      
  
      console.log('Refreshing partner data to verify update...');
      try {
        await fetchPartnerData();
      } catch (fetchError) {
        console.warn('Failed to refresh data after update, but update was successful:', fetchError);
      }
      
     
      setShowUpdateModal(false);
      
     
      setTimeout(() => {
        console.log('Auto-logout after profile update');
        handleLogout();
      }, 500);
      
    } catch (err) {
      console.error('Failed to update profile - Full error:', err);
      console.error('Error response:', err.response);
      console.error('Error status:', err.response?.status);
      console.error('Error data:', err.response?.data);
      console.error('Error message:', err.message);
      
     
      setShowUpdateModal(false);
      
      if (err.response?.status === 401 || err.response?.status === 403) {
        alert('Authentication failed. Please try logging in again if the issue persists.');
  
      } else if (err.response) {
        alert(`Failed to update profile: ${err.response.status} - ${err.response.data?.message || err.response.statusText}`);
      } else {
        alert('Failed to update profile. Please check your connection and try again.');
      }
    }
  };

  const renderProfile = () => {
    if (!partnerData) {
      return <div className="loading-state">Loading profile...</div>;
    }

    return (
      <div className="profile-container">
        <div className="profile-header">
          <div className="profile-avatar">
            <span className="avatar-text">
              {partnerData.name ? partnerData.name.charAt(0).toUpperCase() : 'P'}
            </span>
          </div>
          <div className="profile-info">
            <h2 className="profile-name">{partnerData.name || 'No Name'}</h2>
            <p className="profile-role">Delivery Partner</p>
            <div className={`availability-badge ${isAvailable ? 'available' : 'unavailable'}`}>
              {isAvailable ? 'Available' : 'Unavailable'}
            </div>
          </div>
        </div>

        <div className="profile-details">
          <div className="profile-section">
            <div className="section-header-with-button">
              <h3>Personal Information</h3>
              <button className="update-profile-btn" onClick={handleUpdateProfile}>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M3 17.25V21H6.75L17.81 9.94L14.06 6.19L3 17.25ZM20.71 7.04C21.1 6.65 21.1 6.02 20.71 5.63L18.37 3.29C17.98 2.9 17.35 2.9 16.96 3.29L15.13 5.12L18.88 8.87L20.71 7.04Z" fill="currentColor"/>
                </svg>
                Update Profile
              </button>
            </div>
            <div className="info-grid">
              <div className="info-item">
                <span className="info-label">Email:</span>
                <span className="info-value">{partnerData.email || 'Not provided'}</span>
              </div>
              <div className="info-item">
                <span className="info-label">Partner ID:</span>
                <span className="info-value">{partnerData.id}</span>
              </div>
            </div>
          </div>

          <div className="profile-section">
            <h3>Delivery Statistics</h3>
            <div className="stats-grid">
              <div className="stat-card">
                <div className="stat-number">{partnerData.totalOrders || 0}</div>
                <div className="stat-label">Total Deliveries</div>
              </div>
              <div className="stat-card">
                <div className="stat-number">{isAvailable ? 'Available' : 'Unavailable'}</div>
                <div className="stat-label">Current Status</div>
              </div>
            </div>
          </div>


        </div>
      </div>
    );
  };

  const renderOrders = () => {
    if (orders.length === 0) {
      return (
        <div className="no-orders">
          <div className="no-orders-icon"></div>
          <h3>No Orders Assigned</h3>
          <p>You don't have any orders assigned at the moment.</p>
        </div>
      );
    }

    return (
      <div className="orders-grid">
        {orders.map(order => (
          <div key={order.id} className="order-card">
            <div className="order-header">
              <div className="order-id">
                <span className="order-number">Order #{order.id}</span>
                <span className={`order-status ${order.orderStatus.toLowerCase().replace(/\s+/g, '-')}`}>
                  {order.orderStatus}
                </span>
              </div>
            </div>

            <div className="order-details">
              <div className="customer-info">
                <h4>Customer Details</h4>
                <div className="detail-row">
                  <span className="label">Name:</span>
                  <span className="value">{order.orderAddress ? `${order.orderAddress.firstName} ${order.orderAddress.lastName}` : order.customerName}</span>
                </div>
                <div className="detail-row">
                  <span className="label">Phone:</span>
                  <span className="value">{order.orderAddress?.phoneNo || 'N/A'}</span>
                </div>
                <div className="detail-row">
                  <span className="label">Address:</span>
                  <span className="value">
                    {order.orderAddress ? 
                      `${order.orderAddress.street}, ${order.orderAddress.city}, ${order.orderAddress.state} - ${order.orderAddress.pin}` 
                      : 'N/A'
                    }
                  </span>
                </div>
              </div>

              <div className="order-info">
                <h4>Order Summary</h4>
                <div className="detail-row">
                  <span className="label">Total Amount:</span>
                  <span className="value">₹{order.totalPrice || 0}</span>
                </div>
                <div className="detail-row">
                  <span className="label">Payment:</span>
                  <span className="value">COD</span>
                </div>
                <div className="detail-row">
                  <span className="label">Items:</span>
                  <span className="value">{order.totalQty || order.orderItems?.length || 0} items</span>
                </div>
              </div>
            </div>

            <div className="order-actions">
              {order.orderStatus === 'PENDING' && (
                <button 
                  className="action-btn pickup-btn"
                  onClick={async () => {
                    try {
                      await api.put(`/app2/api/v1/orders/status/${order.id}?newStatus=OUT`);
                      fetchAssignedOrders();
                    } catch (err) {
                      console.error('Failed to update status', err);
                      
                    }
                  }}
                >
                   Mark as Picked Up
                </button>
              )}
              {order.orderStatus !== 'DELIVERED' && order.orderStatus?.toUpperCase() !== 'CANCELLED' && (
                <button 
                  className="action-btn deliver-btn"
                  onClick={() => handleMarkAsDelivered(order)}
                >
                   Mark as Delivered
                </button>
              )}
            </div>
          </div>
        ))}
      </div>
    );
  };

  return (
    <div className="delivery-partner-dashboard">
      <div className="dashboard-header">
        <div className="header-left">
          <h1>Delivery Partner Dashboard</h1>
          <button className="signout-btn" onClick={handleLogout}>
            Sign Out
          </button>
        </div>
        <div className="header-right">
          <p>Welcome back, {partnerData?.name || 'Partner'}</p>
          <div 
            className="profile-circle"
            onClick={() => setActiveTab('profile')}
            title="View Profile"
          >
            <span className="profile-circle-text">
              {partnerData?.name ? partnerData.name.charAt(0).toUpperCase() : 'P'}
            </span>
          </div>
        </div>
      </div>

      {activeTab === 'profile' && (
        <div className="tab-navigation">
          <button
            className="tab-btn active"
            onClick={() => setActiveTab('orders')}
          >
            ← Back to Orders
          </button>
        </div>
      )}

      <div className="tab-content">
        {activeTab === 'profile' ? renderProfile() : (
          <div className="orders-section">
            <div className="section-header">
              <h2>Assigned Orders ({orders.length})</h2>
              <div className="header-actions-group">
                <div className="availability-toggle">
                  <span className="toggle-label">
                    {isAvailable ? 'Available' : 'Unavailable'}
                  </span>
                  <label className="toggle-switch">
                    <input
                      type="checkbox"
                      checked={isAvailable || false}
                      onChange={toggleAvailability}
                    />
                    <span className="slider"></span>
                  </label>
                </div>
                <button className="refresh-btn" onClick={fetchAssignedOrders}>
                  Refresh
                </button>
              </div>
            </div>
            {renderOrders()}
          </div>
        )}
      </div>

      {showOtpModal && (
        <div className="modal-overlay">
          <div className="otp-modal">
            <div className="modal-header">
              <h3>Verify Delivery OTP</h3>
              <button 
                className="close-btn"
                onClick={() => {
                  setShowOtpModal(false);
                  setOtpInput('');
                  setSelectedOrder(null);
                }}
              >
                ✕
              </button>
            </div>
            <div className="modal-body">
              <p>Please enter the 6-digit OTP provided by the customer:</p>
              <div className="otp-input-container">
                <input
                  type="text"
                  value={otpInput}
                  onChange={(e) => setOtpInput(e.target.value.replace(/\D/g, '').slice(0, 6))}
                  placeholder="Enter 6-digit OTP"
                  className="otp-input"
                  maxLength="6"
                />
              </div>
              <div className="modal-actions">
                <button 
                  className="cancel-btn"
                  onClick={() => {
                    setShowOtpModal(false);
                    setOtpInput('');
                    setSelectedOrder(null);
                  }}
                >
                  Cancel
                </button>
                <button 
                  className="verify-btn"
                  onClick={verifyOtpAndDeliver}
                >
                  Verify & Deliver
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {showUpdateModal && (
        <div className="modal-overlay">
          <div className="update-profile-modal">
            <div className="modal-header">
              <h3>Update Profile</h3>
              <button 
                className="close-btn"
                onClick={() => {
                  setShowUpdateModal(false);
                  setUpdateFormData({
                    id: '',
                    email: '',
                    password: '',
                    name: '',
                    phno: '',
                    location: ''
                  });
                }}
              >
                ✕
              </button>
            </div>
            <div className="modal-body">
              <form className="update-form" onSubmit={(e) => e.preventDefault()}>
                <div className="form-group">
                  <label htmlFor="name">Name *</label>
                  <input
                    type="text"
                    id="name"
                    name="name"
                    value={updateFormData.name}
                    onChange={handleUpdateFormChange}
                    placeholder="Enter your full name"
                    required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="email">Email *</label>
                  <input
                    type="email"
                    id="email"
                    name="email"
                    value={updateFormData.email}
                    onChange={handleUpdateFormChange}
                    placeholder="Enter your email address"
                    required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="phno">Phone Number *</label>
                  <input
                    type="tel"
                    id="phno"
                    name="phno"
                    value={updateFormData.phno}
                    onChange={handleUpdateFormChange}
                    placeholder="Enter your phone number"
                    required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="location">Location</label>
                  <input
                    type="text"
                    id="location"
                    name="location"
                    value={updateFormData.location}
                    onChange={handleUpdateFormChange}
                    placeholder="Enter your location/address"
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="password">New Password (Optional)</label>
                  <input
                    type="password"
                    id="password"
                    name="password"
                    value={updateFormData.password}
                    onChange={handleUpdateFormChange}
                    placeholder="Leave blank to keep current password"
                  />
                </div>

                <div className="modal-actions">
                  <button 
                    type="button"
                    className="cancel-btn"
                    onClick={() => {
                      setShowUpdateModal(false);
                      setUpdateFormData({
                        id: '',
                        email: '',
                        password: '',
                        name: '',
                        phno: '',
                        location: ''
                      });
                    }}
                  >
                    Cancel
                  </button>
                  <button 
                    type="button"
                    className="update-btn"
                    onClick={submitProfileUpdate}
                  >
                    Update Profile
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default DeliveryPartner;