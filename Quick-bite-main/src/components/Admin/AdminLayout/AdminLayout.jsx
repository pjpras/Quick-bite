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
          Sign Out
        </button>
        {children}
      </div>
    </div>
  )
}

export default AdminLayout
