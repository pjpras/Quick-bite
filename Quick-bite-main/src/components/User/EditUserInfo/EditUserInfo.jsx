import React, { useState, useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import axios from 'axios'
import { toast } from 'react-toastify'
import { assets } from '../../../assets/assets'
import './EditUserInfo.css'
import api from '../../../config/api'
const EditUserInfo = () => {
  const navigate = useNavigate()
  
const [role, setRole] = useState("customer");
const [name, setName] = useState("");
const [email, setEmail] = useState("");
const [phno, setPhno] = useState("");
const [location, setLocation] = useState("");
const [password, setPassword] = useState("");
const [currentUserId, setCurrentUserId] = useState(null);
const [loading, setLoading] = useState(false);
const [fetchingData, setFetchingData] = useState(true);

useEffect(() => {
    const fetchCurrentUserData = async () => {
        try {
            setFetchingData(true);
            const token = localStorage.getItem('jwtToken');
            const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
            
            if (!token) {
                toast.error('Please login first');
                navigate('/login');
                return;
            }

            if (currentUser.role !== 'customer') {
                toast.error('Access denied. Customer login required.');
                navigate('/login');
                return;
            }

           
            setCurrentUserId(currentUser.id);
            
           
            const response = await api.get(`/app1/api/v1/users/${currentUser.id}`);
            
            if (response.status === 200 && response.data) {
                const userData = response.data;
                
              
                const userId = userData.id || userData.customerId || userData.userId;
                const userPhone = userData.phno || userData.phone || userData.phoneNumber || userData.phoneNo;
                
                setCurrentUserId(userId);
                setName(userData.name || "");
                setEmail(userData.email || "");
                setPhno(userPhone || "");
                setLocation(userData.location || "");
                setRole(userData.role || "customer");
            }
            
        } catch (error) {
            if (error.response?.status === 401) {
                toast.error('Session expired. Please login again.');
                navigate('/login');
            } else {
               
                const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
                
                setName(currentUser.name || "");
                setEmail(currentUser.email || "");
                setPhno(currentUser.phno || currentUser.phone || "");
                setLocation(currentUser.location || "");
                
                toast.error('Failed to fetch user data. Using cached data.');
            }
        } finally {
            setFetchingData(false);
        }
    };

    fetchCurrentUserData();
}, [navigate]);


const handlesubmit = async (e) => {
    e.preventDefault();
    
    if (!currentUserId) {
        toast.error('User not found');
        return;
    }

  
    if (!name.trim() || !email.trim()) {
        toast.error('Name and Email are required fields');
        return;
    }

    
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        toast.error('Please enter a valid email address');
        return;
    }

    try {
        setLoading(true);
        const token = localStorage.getItem('jwtToken');
        
        if (!token) {
            toast.error('Authentication required. Please login again.');
            navigate('/login');
            return;
        }

        const updateData = {
            name: name.trim(),
            email: email.trim(),
            phno: phno.trim(),
            location: location.trim()
        };

     
        if (password.trim()) {
            updateData.password = password.trim();
        }

        const response = await api.put('/app1/api/v1/users/customer/update', updateData);
        
        if (response.status === 200) {
            const updatedUser = response.data;
            
          
            const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
            const updatedCurrentUser = {
                ...currentUser,
                name: updatedUser.name,
                email: updatedUser.email,
                phno: updatedUser.phno,
                location: updatedUser.location
            };
            
            localStorage.setItem('currentUser', JSON.stringify(updatedCurrentUser));
            toast.success('Profile updated successfully!');
            navigate('/user'); 
        }
    } catch (error) {
        
        if (error.response?.status === 400) {
            toast.error('Invalid data provided. Please check your inputs.');
        } else if (error.response?.status === 401) {
            toast.error('Session expired. Please login again.');
            navigate('/login');
        } else if (error.response?.status === 403) {
            toast.error('You are not authorized to update this profile.');
        } else if (error.response?.status === 404) {
            toast.error('User not found.');
        } else {
            toast.error('Failed to update profile. Please try again.');
        }
    } finally {
        setLoading(false);
    }
}


  return (
    
    <div 
      className="auth-container"
      style={{
        backgroundImage: `linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url(${assets.landing_img})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat'
      }}
    >
      <div className="auth-card fade-in">
        <div className="auth-header">
          <h1>QuickBite</h1>
          <h2>Edit Your Profile</h2>
        </div>
        
        {fetchingData ? (
          <div className="loading-container">
            <div className="loading-spinner"></div>
            <p>Loading your profile...</p>
          </div>
        ) : (
          <form className="auth-form" onSubmit={handlesubmit}>
         
         

          
          <div className="form-group">
            <label htmlFor="name">Full Name</label>
            <input
              type="text"
              id="name"
              name="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
              placeholder="Enter your full name"
            />
          </div>

          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              placeholder="Enter your email"
            />
          </div>

          
          <div className="form-group">
            <label htmlFor="phno">Phone Number</label>
            <input
              type="tel"
              id="phno"
              name="phno"
              value={phno}
              onChange={(e) => setPhno(e.target.value)}
              placeholder="Enter your phone number"
            />
          </div>

          <div className="form-group">
            <label htmlFor="location">Location</label>
            <input
              type="text"
              id="location"
              name="location"
              value={location}
              onChange={(e) => setLocation(e.target.value)}
              placeholder="Enter your location"
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">New Password</label>
            <input
              type="password"
              id="password"
              name="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Leave blank to keep current password"
            />
          </div>

          <button type="submit" className="auth-button" disabled={loading}>
            {loading ? 'Updating...' : 'Update Profile'}
          </button>
        </form>
        )}

        <div className="auth-footer">
          <button 
            type="button" 
            className="back-button" 
            onClick={() => navigate('/user')}
          >
            ← Back to Dashboard
          </button>
        </div>
      </div>
      
    </div>
  )
}

export default EditUserInfo
