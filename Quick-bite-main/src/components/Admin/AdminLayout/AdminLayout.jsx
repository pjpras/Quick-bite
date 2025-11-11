import React from 'react'
import { Link, useNavigate } from 'react-router-dom'

import './AdminLayout.css'

const AdminLayout = ({ children }) => {
  const navigate = useNavigate()

  const handleLogout = () => {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('userId');
    navigate('/login');
  }

  return (
    <div className="admin-layout">
      
      <div className="sidebar">
        <div className="sidebar-header">
          <h2>QuickBite Admin</h2>
        </div>

        <nav className="sidebar-nav">
          <Link to="/admin/MenuManagement" className="nav-item">
            Menu Management
          </Link>

          <Link to="/admin/view-orders" className="nav-item">
            View Orders
          </Link>
          <Link to="/admin/view-customers" className="nav-item">
            View Customers
          </Link>
          <Link to="/admin/view-delivery-partners" className="nav-item">
            Delivery Partners
          </Link>
        </nav>
      </div>

      <div className="admin-content">
        <button onClick={handleLogout} className="logout-btn">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M17 7L15.59 8.41L18.17 11H8V13H18.17L15.59 15.59L17 17L22 12L17 7Z" fill="currentColor"/>
            <path d="M19 19H5V5H19V3H5C3.89 3 3 3.89 3 5V19C3 20.1 3.89 21 5 21H19C20.1 21 21 20.1 21 19V17H19V19Z" fill="currentColor"/>
          </svg>
          Sign Out
        </button>
        {children}
      </div>
    </div>
  )
}

export default AdminLayout
