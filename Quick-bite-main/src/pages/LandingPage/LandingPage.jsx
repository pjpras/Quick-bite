import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { assets } from "../../assets/assets";
import Footer from "../../components/User/Footer/Footer";
import "./LandingPage.css";
 
const LandingPage = () => {
  const navigate = useNavigate();

  return (
    <div className="landing-page-wrapper">
      <div 
        className="landing-page"
        style={{
          backgroundImage: `linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url(${assets.landing_img})`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          backgroundRepeat: 'no-repeat'
        }}
      >
        <div className="landing-header">
          <h1>QuickBite</h1>
          <p>Order delivery near you</p>
        </div>
        
        <div className="landing-page-input">
          <div className="input-with-icon">
            <img
              src={assets.search_icon}
              alt="location"
              className="input-icon"
            />
            <input 
              type="text" 
              name="location" 
              placeholder="Enter your delivery location" 

            />
          </div>
          <button className="auth-button" >
            Find Food Near Me
          </button>
        </div>

        <div className="landing-actions">
          <button className="auth-button" onClick={() => navigate('/login')}>
            Login
          </button>
          <button className="auth-button" onClick={() => navigate('/signup')}>
            Sign Up
          </button>
        </div>
      </div>
      
      <Footer />
    </div>
  );
};
 
export default LandingPage;
