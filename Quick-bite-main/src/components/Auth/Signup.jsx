import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { toast } from 'react-toastify'
import { assets } from '../../assets/assets'
import './Auth.css'
import api from '../../config/api'

const Signup = () => {
  const navigate = useNavigate()
  
const [role,setRole]=useState("customer");
const [name,setName]=useState("");
const [email,setEmail]=useState("");
const [phone,setPhone]=useState("");
const [location,setLocation]=useState("");
const [password,setPassword]=useState("");
const [status,setStatus]=useState("online");

const handleSubmit = async (e) => {
  e.preventDefault();
  try {
    const endpoint = role === "delivery-partner" 
      ? '/app1/api/v1/users/signup/partner' 
      : '/app1/api/v1/users/signup/customer';
    
    const payload = {
      name,
      email,
      phone,
      address: location,
      password
    };
    
    const res = await api.post(endpoint, payload);
    
    if (res.status === 201 || res.status === 200) {
      toast.success('Account created successfully!');
      navigate('/login');
    }
  } catch (err) {
    console.error('Signup failed', err);
    toast.error(err.response?.data?.message || 'Signup failed. Please try again.');
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
          <h2>Create your account</h2>
        </div>
        
        <form className="auth-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="role">Select Role</label>
            <select
              id="role"
              name="role"
              value={role}
              onChange={(e) => setRole(e.target.value)}
              required
            >
              <option value="customer">Customer</option>
              
              <option value="delivery-partner">Delivery Partner</option>
            </select>
          </div>

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

          {(
            <div className="form-group">
              <label htmlFor="phone">Phone Number</label>
              <input
                type="tel"
                id="phone"
                name="phone"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
                required
                placeholder="Enter your phone number"
              />
            </div>
          )}

          {(
            <div className="form-group">
              <label htmlFor="location">Location</label>
              <input
                type="text"
                id="location"
                name="location"
                value={location}
                onChange={(e) => setLocation(e.target.value)}
                required
                placeholder="Enter your location"
              />
            </div>
          )}

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              name="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              placeholder="Enter your password"
              minLength={6}
            />
          </div>

         

          <button type="submit" className="auth-button">
            Submit
          </button>
        </form>

        <div className="auth-footer">
          <button 
            type="button" 
            className="back-button" 
            onClick={() => navigate('/')}
          >
            ← Back to Home
          </button>
          <p>Already have an account? <Link to="/login">Login</Link></p>
          <p><Link to="/">Back to Landing Page</Link></p>
        </div>
      </div>
      
    </div>
  )
}

export default Signup
