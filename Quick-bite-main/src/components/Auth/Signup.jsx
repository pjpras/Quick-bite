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
const [errors, setErrors] = useState({});


const validateEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

const validatePhone = (phone) => {
  const phoneRegex = /^[0-9]{10}$/;
  return phoneRegex.test(phone);
}

const validateName = (name) => {
  const nameRegex = /^[a-zA-Z\s]{3,}$/;
  return nameRegex.test(name);
}

const validatePassword = (password) => {
  const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
  return passwordRegex.test(password);
}

const validateForm = () => {
  const newErrors = {};


  if (!name.trim()) {
    newErrors.name = 'Name is required';
  } else if (name.trim().length < 3) {
    newErrors.name = 'Name must be at least 3 characters';
  } else if (!validateName(name)) {
    newErrors.name = 'Name should contain only letters';
  }


  if (!email.trim()) {
    newErrors.email = 'Email is required';
  } else if (!validateEmail(email)) {
    newErrors.email = 'Please enter a valid email address';
  }


  if (!phone.trim()) {
    newErrors.phone = 'Phone number is required';
  } else if (!validatePhone(phone)) {
    newErrors.phone = 'Phone number must be 10 digits';
  }


  if (!location.trim()) {
    newErrors.location = 'Location is required';
  } else if (location.trim().length < 5) {
    newErrors.location = 'Location must be at least 5 characters';
  }


  if (!password) {
    newErrors.password = 'Password is required';
  } else if (password.length < 8) {
    newErrors.password = 'Password must be at least 8 characters';
  } else if (!validatePassword(password)) {
    newErrors.password = 'Password must contain uppercase, lowercase, and number';
  }


  setErrors(newErrors);
  return Object.keys(newErrors).length === 0;
}

const handleSubmit = async (e) => {
  e.preventDefault();
  

  if (!validateForm()) {
    toast.error('Please fix the validation errors');
    return;
  }

  try {
    const endpoint = role === "delivery-partner" 
      ? '/app1/api/v1/users/signup/partner' 
      : '/app1/api/v1/users/signup/customer';
    
    const payload = {
      name,
      email,
      phno: phone,
      location,
      password
    };

    // LOG: print payload and headers so we can compare with Postman
    console.log('Signup -> endpoint:', endpoint);
    console.log('Signup -> payload:', JSON.stringify(payload));
    console.log('Signup -> axios default headers:', api.defaults?.headers);
    
    const res = await api.post(endpoint, payload);
    
    if (res.status === 201 || res.status === 200) {
      toast.success('Account created successfully!');
      navigate('/login');
    }
  } catch (err) {
    // Detailed logging for debugging gateway 400
    console.error('Signup failed - message:', err.message);
    console.error('Signup failed - response status:', err.response?.status);
    console.error('Signup failed - response data:', err.response?.data);
    console.error('Signup failed - response headers:', err.response?.headers);
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
              onChange={(e) => {
                setName(e.target.value);
                if (errors.name) setErrors({...errors, name: ''});
              }}
              className={errors.name ? 'input-error' : ''}
              placeholder="Enter your full name (min 3 characters)"
            />
            {errors.name && <span className="error-message">{errors.name}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              value={email}
              onChange={(e) => {
                setEmail(e.target.value);
                if (errors.email) setErrors({...errors, email: ''});
              }}
              className={errors.email ? 'input-error' : ''}
              placeholder="Enter your email"
            />
            {errors.email && <span className="error-message">{errors.email}</span>}
          </div>

          {(
            <div className="form-group">
              <label htmlFor="phone">Phone Number</label>
              <input
                type="tel"
                id="phone"
                name="phone"
                value={phone}
                onChange={(e) => {
                  setPhone(e.target.value);
                  if (errors.phone) setErrors({...errors, phone: ''});
                }}
                className={errors.phone ? 'input-error' : ''}
                placeholder="Enter 10-digit phone number"
                maxLength={10}
              />
              {errors.phone && <span className="error-message">{errors.phone}</span>}
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
                onChange={(e) => {
                  setLocation(e.target.value);
                  if (errors.location) setErrors({...errors, location: ''});
                }}
                className={errors.location ? 'input-error' : ''}
                placeholder="Enter your location (min 5 characters)"
              />
              {errors.location && <span className="error-message">{errors.location}</span>}
            </div>
          )}

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              name="password"
              value={password}
              onChange={(e) => {
                setPassword(e.target.value);
                if (errors.password) setErrors({...errors, password: ''});
              }}
              className={errors.password ? 'input-error' : ''}
              placeholder="Min 8 chars with uppercase, lowercase & number"
            />
            {errors.password && <span className="error-message">{errors.password}</span>}
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
