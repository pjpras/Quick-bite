import React from 'react'
import './footer.css'
import { assets } from '../../../assets/assets'
import { Link } from 'react-router-dom'

const Footer = () => {
  return (
    <div className='footer' id='footer'>
      <div className='footer-content'>
        <div className="footer-content-left">
          <img src={assets.logo} alt="QuickBite Logo" />
          <p>Delicious food delivered fast. Enjoy a variety of cuisines and quick service with QuickBite!</p>
          <div className='footer-social-icons'>
            <img src={assets.facebook_icon} alt="Facebook" />
            <img src={assets.twitter_icon} alt="Twitter" />
            <img src={assets.linkedin_icon} alt="LinkedIn" />
          </div>
        </div>
        <div className="footer-content-center">
          <h2>Get in Touch</h2>
          <ul>
            <li>Contact Us</li>
            <li>Mail: <a href="mailto:customerservice@quickbite.com">customerservice@quickbite.com</a></li>
          </ul>
        </div>
        <div className="footer-content-right">
          <h2>Company</h2>
          <ul>
            <Link to='/'><li>Home</li></Link>
            <Link to='/about'><li>About</li></Link>
            <li>Privacy Policy</li>
          </ul>
        </div>
      </div>
      <hr style={{border: 'none', borderTop: '1px solid #444', margin: '16px 0'}}/>
      <p className='footer-copyright'>Copyright 2025 @QuickBite.com - All rights reserved</p>
    </div>
  )
}

export default Footer