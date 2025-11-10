import React, { useState, useContext, useEffect } from 'react'
import { assets } from '../../../assets/assets';
import './Navbar.css'
import { Link, useNavigate } from 'react-router-dom';

const Navbar = () => {

    const navigate = useNavigate();
    const [currentUser, setCurrentUser] = useState(null);
        
    useEffect(() => {
        const getCurrentUser = () => {
            const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
            if (user && user.id) {
                setCurrentUser(user);
            }
        };
        getCurrentUser();
    }, []);

  return (
    <div className='navbar'>
        <img src={assets.logo} alt="" className='logo'/>
        <ul className='navbar-menu'>
         <Link to='/user'> <li  className="">Home</li> </Link>  
         <Link to='/user/about'>   <li  className="">About</li> </Link>
         <Link to='/user/contact'>  <li  className="">Contact</li> </Link>
        </ul>
        <div className='navbar-right'>
            <img 
                src={assets.search_icon} 
                alt='Search' 
                className='search-icon'
                
            />
            <div className='navbar-search-icon'>
                <Link to={"/user/cart"}>
                <img  src={assets.basket_icon} alt="" />
                </Link>
                <div className=""></div>
            </div>

                <div className="navbar-profile">
                    <img src={assets.profile_icon} alt="Profile" className="profile-icon" />
                    <div className="profile-dropdown">
                        <div className="profile-info">
                            {currentUser ? currentUser.name : 'Guest'}
                        </div>
                        <Link to="/user/myorders">My Orders</Link>
                        
                        <Link to="/user/editinfo">Edit profile</Link>

                        <Link to="/">
                        <button className="logout-btn" onClick={() => {
                            localStorage.removeItem('currentUser');
                            localStorage.removeItem('jwtToken');
                            localStorage.removeItem('userId');
                            window.location.href = '/';
                        }}>
                            Logout
                        </button>
                        </Link>
                    </div>
                </div>

        </div>

    </div>
  )
}

export default Navbar