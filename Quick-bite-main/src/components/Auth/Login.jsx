import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { toast } from 'react-toastify'
import { assets } from '../../assets/assets'
import './Auth.css'
import api from '../../config/api'

const Login = () => {
  const navigate = useNavigate()
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log('Login attempt with:', { email, password });
    
  
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('currentUser');
    
    try {
      console.log('Sending login request to:', '/app1/api/v1/users/login');
      const res = await api.post('/app1/api/v1/users/login', {
        email,
        password
      });
      
      console.log('Login response:', res.data);
      
      if (res.data.accessToken) {
        
        localStorage.setItem('jwtToken', res.data.accessToken);
        localStorage.setItem('userId', res.data.id); 
    
        const userData = {
          id: res.data.id,
          name: res.data.name,
          email: res.data.email,
          role: res.data.role
        };
        localStorage.setItem('currentUser', JSON.stringify(userData));
        
        toast.success('Login successful!');
        
        console.log('User role from response:', res.data.role);
        console.log('Role type:', typeof res.data.role);
        
     
        const userRole = res.data.role?.toLowerCase();
        console.log('Normalized role:', userRole);
        
        if (userRole === 'customer') {
          console.log('Navigating to /user');
          navigate('/user');
        } else if (userRole === 'admin') {
          console.log('Navigating to /admin/MenuManagement');
          navigate('/admin/MenuManagement');
        } else if (userRole === 'deliverypartner' || userRole === 'delivery_partner') {
          console.log('Navigating to /delivery-partner');
          navigate('/delivery-partner');
        } else {
          console.log('Role not recognized, navigating to home. Role was:', res.data.role);
          toast.warning('Role not recognized, redirecting to home');
          navigate('/');
        }
      } else {
        console.error('No accessToken in response:', res.data);
        toast.error('Invalid response from server');
      }
    } catch (err) {
      console.error('Login failed:', err);
      console.error('Error response:', err.response);
      toast.error(err.response?.data?.message || err.message || 'Login failed. Please check your credentials.');
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
          <h2>Login to your account</h2>
        </div>
        
        <form className="auth-form" onSubmit={handleSubmit}>
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
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              name="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              placeholder="Enter your password"
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
          <p>Don't have an account? <Link to="/signup">Sign up</Link></p>
          <p><Link to="/">Back to Landing Page</Link></p>
        </div>
      </div>
    </div>
  )
}

export default Login
