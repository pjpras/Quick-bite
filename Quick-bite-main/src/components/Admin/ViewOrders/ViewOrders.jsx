
import React, { useState, useEffect } from 'react';
import './ViewOrders.css';
import axios from 'axios';
import AdminLayout from '../AdminLayout/AdminLayout';
import api from '../../../config/api';

const ViewOrders = () => {
  const [orders, setOrders] = useState([]);
  const [selectedTab, setSelectedTab] = useState('orders');
  const [assigningOrder, setAssigningOrder] = useState(null);
  const [deliveryPartners, setDeliveryPartners] = useState([]);

 
  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const data = await api.get('/app2/api/v1/orders/all');
        console.log('Fetched orders:', data.data);
        setOrders(data.data);
      } catch (err) {
        console.error('Failed to fetch orders', err);
      }
    };
    
    fetchOrders();
  }, []);

  useEffect(() => {
    const fetchDeliveryPartners = async () => {
      try {
       
        const res = await api.get('/app1/api/v1/users/deliverypartners/active');
        console.log('Fetched active delivery partners:', res.data);
        setDeliveryPartners(res.data);
      } catch (err) {
        console.error('Failed to fetch delivery partners', err);
      }
    };
    
    fetchDeliveryPartners();
  }, []);

  const assignDeliveryPartner = async (orderId, partnerId) => {
    try {
      const res = await api.put(`/app2/api/v1/orders/assign/${orderId}?partnerId=${partnerId}`);
      
      if (res.status === 200) {
        console.log('Assigned delivery partner:', res.data);
      
        const ordersRes = await api.get('/app2/api/v1/orders/all');
        setOrders(ordersRes.data);
        setAssigningOrder(null);
     
      }
    } catch (err) {
      console.error('Failed to assign delivery partner', err);
     
    }
  };

  const getStatusColor = (status) => {
    switch (status?.toUpperCase()) {
      case 'PENDING': return '#f59e0b';
      case 'OUT': return '#3b82f6';
      case 'DELIVERED': return '#10b981';
      case 'CANCELLED': return '#ef4444';
      default: return '#6b7280';
    }
  };

  
  const formatDate = (orderDate, orderTime) => {
    if (!orderDate) return 'N/A';
    try {
     
      const dateStr = `${orderDate}${orderTime ? 'T' + orderTime : ''}`;
      return new Date(dateStr).toLocaleDateString('en-IN', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: orderTime ? '2-digit' : undefined,
        minute: orderTime ? '2-digit' : undefined
      });
    } catch (error) {
      return orderDate;
    }
  };

  return (
    <AdminLayout>
      <div className="view-orders">
       
        <div className="orders-header">
          <h1>Order Management</h1>
          <p>Manage customer orders and delivery assignments</p>
        </div>

     
      <div className="tab-navigation">
        <button 
          className={`tab-btn ${selectedTab === 'orders' ? 'active' : ''}`}
          onClick={() => setSelectedTab('orders')}
        >
          All Orders ({orders.length})
        </button>
        <button 
          className={`tab-btn ${selectedTab === 'partners' ? 'active' : ''}`}
          onClick={() => setSelectedTab('partners')}
        >
          Delivery Partners ({deliveryPartners.length})
        </button>
      </div>

     
      {selectedTab === 'orders' && (
        <div className="orders-section">
          <div className="orders-stats">
            <div className="stat-card">
              <h3>Total Orders</h3>
              <div className="stat-number">{orders.length}</div>
            </div>
            <div className="stat-card">
              <h3>Pending</h3>
              <div className="stat-number">
                {orders.filter(order => order.orderStatus === 'PENDING').length}
              </div>
            </div>
            <div className="stat-card">
              <h3>Out for Delivery</h3>
              <div className="stat-number">
                {orders.filter(order => order.orderStatus === 'OUT').length}
              </div>
            </div>
            <div className="stat-card">
              <h3>Delivered</h3>
              <div className="stat-number">
                {orders.filter(order => order.orderStatus === 'DELIVERED').length}
              </div>
            </div>
          </div>

          <div className="orders-list">
            {orders.map(order => (
              <div key={order.id} className="order-card">
                <div className="order-header">
                  <div className="order-info">
                    <h3>Order #{order.id}</h3>
                    <span className="order-date">{formatDate(order.orderDate, order.orderTime)}</span>
                  </div>
                  <div className="order-status">
                    <span 
                      className="status-badge" 
                      style={{ backgroundColor: getStatusColor(order.orderStatus) }}
                    >
                      {order.orderStatus}
                    </span>
                  </div>
                </div>

                <div className="order-content">
                  <div className="customer-info">
                    <div className="customer-details-container">
                      <div className="customer-detail">
                        <span className="detail-label">Customer Name:</span>
                        <span className="detail-value">{order.customerName || 'N/A'}</span>
                      </div>
                      <div className="customer-detail">
                        <span className="detail-label">Customer ID:</span>
                        <span className="detail-value">{order.customerId || 'N/A'}</span>
                      </div>
                      <div className="customer-detail">
                        <span className="detail-label">Phone:</span>
                        <span className="detail-value">{order.orderAddress?.phoneNo || 'N/A'}</span>
                      </div>
                      <div className="customer-detail">
                        <span className="detail-label">Address:</span>
                        <span className="detail-value">
                          {order.orderAddress ? 
                            `${order.orderAddress.street || ''}, ${order.orderAddress.city || ''}, ${order.orderAddress.state || ''} - ${order.orderAddress.pin || ''}` 
                            : 'N/A'}
                        </span>
                      </div>
                    </div>
                  </div>

                  <div className="order-items">
                    <h4>Order Items ({order.orderItems?.length || 0})</h4>
                    <div className="items-list">
                      {order.orderItems?.map((item, index) => (
                        <div key={index} className="order-item">
                          <span className="item-name">{item.foodName}</span>
                          <span className="item-price">₹{item.subtotal || (item.price * item.quantity)}</span>
                          <span className="item-quantity">x{item.quantity}</span>
                        </div>
                      ))}
                    </div>
                  </div>

                  <div className="order-summary">
                    <div className="summary-row">
                      <span>Total Quantity:</span>
                      <span>{order.totalQty}</span>
                    </div>
                    <div className="summary-row total">
                      <span>Total Price:</span>
                      <span>₹{order.totalPrice}</span>
                    </div>
                  </div>
                </div>

                <div className="order-actions">
                  {order.deliveryPartnerId ? (
                    <div className="assigned-partner">
                      <span className="partner-info">
                        Assigned to: <strong>{order.assignDeliveryPerson}</strong>
                        <br />
                        <small>Partner ID:{order.deliveryPartnerId}</small>
                      </span>
                    </div>
                  ) : (
                    order.orderStatus === 'PENDING' && (
                      <button 
                        className="assign-btn"
                        onClick={() => setAssigningOrder(order.id)}
                      >
                        Assign Delivery Partner
                      </button>
                    )
                  )}
                 
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

    
      {selectedTab === 'partners' && (
        <div className="partners-section">
          <div className="partners-stats">
            <div className="stat-card">
              <h3>Total Partners</h3>
              <div className="stat-number">{deliveryPartners.length}</div>
            </div>
            <div className="stat-card available">
              <h3>Available</h3>
              <div className="stat-number">
                {deliveryPartners.filter(p => p.availabilityStatus === true).length}
              </div>
            </div>
            <div className="stat-card unavailable">
              <h3>Unavailable</h3>
              <div className="stat-number">
                {deliveryPartners.filter(p => p.availabilityStatus === false).length}
              </div>
            </div>
          </div>

          <div className="partners-list">
            {deliveryPartners.length > 0 ? (
              deliveryPartners.map(partner => (
                <div key={partner.id} className="partner-card">
                  <div className="partner-header">
                    <div className="partner-info">
                      <h3>{partner.name}</h3>
                      <span className="partner-phone">{partner.phno || 'N/A'}</span>
                    </div>
                    <div className="partner-availability">
                      <span className={`availability-badge ${partner.availabilityStatus ? 'available' : 'unavailable'}`}>
                        {partner.availabilityStatus ? 'Available' : 'Unavailable'}
                      </span>
                    </div>
                  </div>
                  <div className="partner-details">
                    <p><strong>Email:</strong> {partner.email}</p>
                    <p><strong>Location:</strong> {partner.location || 'N/A'}</p>
                    <p><strong>Partner ID:</strong> {partner.id}</p>
                  </div>
                </div>
              ))
            ) : (
              <p className="no-partners">No delivery partners found</p>
            )}
          </div>
        </div>
      )}

     
      {assigningOrder && (
        <div className="assignment-modal">
          <div className="modal-content">
            <div className="modal-header">
              <h3>Assign Delivery Partner</h3>
              <button 
                className="close-btn"
                onClick={() => setAssigningOrder(null)}
              >
                ×
              </button>
            </div>
            
            <div className="modal-body">
              <h4>Delivery Partners</h4>
              <div className="available-partners">
                {deliveryPartners.length > 0 ? (
                  deliveryPartners.map(partner => (
                    <div key={partner.id} className={`partner-option ${!partner.availabilityStatus ? 'unavailable-partner' : ''}`}>
                      <div className="partner-info">
                        <div className="partner-name-status">
                          <h5>{partner.name}</h5>
                          <span className={`availability-badge-small ${partner.availabilityStatus ? 'available' : 'unavailable'}`}>
                            {partner.availabilityStatus ? 'Available' : 'Unavailable'}
                          </span>
                        </div>
                        <p>Partner ID: {partner.id}</p>
                        <p>Phone: {partner.phno}</p>
                        <p>Location: {partner.location || 'N/A'}</p>
                      </div>
                      <button 
                        className="assign-partner-btn"
                        onClick={() => assignDeliveryPartner(assigningOrder, partner.id)}
                        disabled={!partner.availabilityStatus}
                      >
                        {partner.availabilityStatus ? 'Assign' : 'Not Available'}
                      </button>
                    </div>
                  ))
                ) : (
                  <p className="no-partners">No delivery partners available</p>
                )}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
    </AdminLayout>
  );
};

export default ViewOrders; 