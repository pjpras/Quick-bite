import React, { useState, useEffect } from 'react';
import './Admindashboard.css';

const Admindashboard = () => {
  const [stats, setStats] = useState({
    totalOrders: 0,
    totalCustomers: 0,
    totalDeliveryPartners: 0,
    totalRevenue: 0
  });

  return (
    <div className="admin-dashboard">
      <div className="dashboard-header">
        <h1>Admin Dashboard</h1>
        <p>Welcome to your admin portal</p>
      </div>
      
      <div className="dashboard-stats">
        <div className="stat-card">
          <h3>Total Orders</h3>
          <div className="stat-number">{stats.totalOrders}</div>
          <p className="stat-text">Orders processed</p>
        </div>
        
        <div className="stat-card">
          <h3>Total Customers</h3>
          <div className="stat-number">{stats.totalCustomers}</div>
          <p className="stat-text">Registered users</p>
        </div>
        
        <div className="stat-card">
          <h3>Delivery Partners</h3>
          <div className="stat-number">{stats.totalDeliveryPartners}</div>
          <p className="stat-text">Active partners</p>
        </div>
        
        <div className="stat-card">
          <h3>Total Revenue</h3>
          <div className="stat-number">₹{stats.totalRevenue.toFixed(2)}</div>
          <p className="stat-text">Revenue earned</p>
        </div>
      </div>
      
      <div className="recent-activity">
        <h2>Recent Activity</h2>
        <div className="activity-list">
          <div className="activity-item">
            <div className="activity-icon"></div>
            <div className="activity-details">
              <h4>New Order Received</h4>
              <p>Order #1234 placed by customer</p>
              <span className="activity-time">15 minutes ago</span>
            </div>
          </div>
          
          <div className="activity-item">
            <div className="activity-icon"></div>
            <div className="activity-details">
              <h4>New Customer Registered</h4>
              <p>Customer signed up successfully</p>
              <span className="activity-time">45 minutes ago</span>
            </div>
          </div>
          
          <div className="activity-item">
            <div className="activity-icon"></div>
            <div className="activity-details">
              <h4>Delivery Partner Added</h4>
              <p>New delivery partner joined the platform</p>
              <span className="activity-time">2 hours ago</span>
            </div>
          </div>
          
          <div className="activity-item">
            <div className="activity-icon"></div>
            <div className="activity-details">
              <h4>New Product Added</h4>
              <p>Pizza Margherita added to menu</p>
              <span className="activity-time">3 hours ago</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Admindashboard;
