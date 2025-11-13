import React, { useState, useContext, useEffect } from 'react'
import { assets } from '../../../assets/assets';
import './Navbar.css'
import { Link, useNavigate } from 'react-router-dom';
import api from '../../../config/api';
import { toast } from 'react-toastify';

const Navbar = () => {

    const navigate = useNavigate();
    const [currentUser, setCurrentUser] = useState(null);
    const [cartItemCount, setCartItemCount] = useState(0);
    const [showSearch, setShowSearch] = useState(false);
    const [searchQuery, setSearchQuery] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [isSearching, setIsSearching] = useState(false);
    const [itemCounts, setItemCounts] = useState({});
    const [showMobileMenu, setShowMobileMenu] = useState(false);
        
    useEffect(() => {
        const getCurrentUser = () => {
            const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
            if (user && user.id) {
                setCurrentUser(user);
            }
        };
        getCurrentUser();
        loadCartCount();
        
       
        const handleCartUpdate = () => {
            loadCartCount();
        };
        window.addEventListener('cartUpdated', handleCartUpdate);
        
        return () => {
            window.removeEventListener('cartUpdated', handleCartUpdate);
        };
    }, []);
    
    const loadCartCount = async () => {
        try {
            const token = localStorage.getItem('jwtToken');
            if (token) {
                const response = await api.get('/app2/api/v1/cart');
                const totalItems = response.data.reduce((sum, item) => sum + item.quantity, 0);
                setCartItemCount(totalItems);
            }
        } catch (error) {
            console.error('Error loading cart count:', error);
            setCartItemCount(0);
        }
    };

    const handleSearchClick = () => {
        setShowSearch(true);
    };

    const handleCloseSearch = () => {
        setShowSearch(false);
        setSearchQuery('');
        setSearchResults([]);
    };

    const handleSearchInput = async (e) => {
        const query = e.target.value;
        setSearchQuery(query);

        if (query.trim().length < 2) {
            setSearchResults([]);
            return;
        }

        try {
            setIsSearching(true);
            
            
            let response;
            let apiError = null;
            
            try {
                response = await api.get('/app2/api/v1/food');
            } catch (err) {
                apiError = err;
               
                console.log('First endpoint failed, trying active endpoint...');
                try {
                    response = await api.get('/app2/api/v1/food/active');
                    apiError = null; 
                } catch (err2) {
                    apiError = err2;
                }
            }
            
            
            if (response && response.data) {
               
                const filteredResults = (response.data || []).filter(food => 
                    food.name.toLowerCase().includes(query.toLowerCase()) ||
                    (food.description && food.description.toLowerCase().includes(query.toLowerCase())) ||
                    (food.categoryName && food.categoryName.toLowerCase().includes(query.toLowerCase()))
                );
                
                console.log('Search query:', query);
                console.log('Total foods:', response.data?.length);
                console.log('Filtered results:', filteredResults.length);
                
                setSearchResults(filteredResults);
            } else if (apiError) {
                throw apiError;
            }
            
            setIsSearching(false);
        } catch (error) {
            console.error('Error searching foods:', error);
            console.error('Error details:', error.response?.data);
            
            if (error.response?.status === 500) {
                const errorMessage = error.response?.data?.message || error.response?.data || '';
                if (errorMessage.toLowerCase().includes('no') && errorMessage.toLowerCase().includes('food')) {
                    toast.info('No food items available yet. Please ask admin to add some foods.');
                } else {
                    toast.error('Server error. Please try again later.');
                }
            } else if (error.response?.status === 401) {
                toast.error('Please login to search for foods.');
            } else {
                toast.error('Failed to search foods. Please try again.');
            }
            
            setSearchResults([]);
            setIsSearching(false);
        }
    };

    const addToCart = async (foodId) => {
        try {
            const currentCount = itemCounts[foodId] || 0;
            const newCount = currentCount + 1;
            
            if (currentCount === 0) {
               
                await api.post('/app2/api/v1/cart/add', {
                    foodId: foodId,
                    quantity: 1
                });
                toast.success('Item added to cart!');
            } else {
                await api.patch(`/app2/api/v1/cart/update/${foodId}?quantity=${newCount}`);
            }
            
            setItemCounts(prev => ({
                ...prev,
                [foodId]: newCount
            }));
            
    
            loadCartCount();
            window.dispatchEvent(new Event('cartUpdated'));
        } catch (error) {
            console.error('Error adding to cart:', error);
            toast.error('Failed to add item to cart');
        }
    };

    const removeFromCart = async (foodId) => {
        try {
            const currentCount = itemCounts[foodId] || 0;
            if (currentCount <= 0) return;
            
            const newCount = currentCount - 1;
            
            if (newCount === 0) {
                
                await api.delete(`/app2/api/v1/cart/remove/${foodId}`);
                toast.info('Item removed from cart');
            } else {
                
                await api.patch(`/app2/api/v1/cart/update/${foodId}?quantity=${newCount}`);
            }
            
            setItemCounts(prev => ({
                ...prev,
                [foodId]: newCount
            }));
            
            
            loadCartCount();
            window.dispatchEvent(new Event('cartUpdated'));
        } catch (error) {
            console.error('Error removing from cart:', error);
            toast.error('Failed to remove item from cart');
        }
    };

  return (
    <div className='navbar'>
        <Link to='/user'>
            <img src={assets.logo} alt="QuickBite" className='logo'/>
        </Link>
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
                onClick={handleSearchClick}
            />
            <div className='navbar-search-icon'>
                <Link to={"/user/cart"}>
                <img  src={assets.basket_icon} alt="" />
                <div className={cartItemCount > 0 ? "dot-green" : "dot-red"}></div>
                </Link>
            </div>

                <div className="navbar-profile">
                    <img src={assets.profile_icon} alt="Profile" className="profile-icon" />
                    <div className="profile-dropdown">
                        <div className="profile-info">
                            {currentUser ? currentUser.name : 'Guest'}
                        </div>
                        <Link to="/user/myorders">My Orders</Link>
                        
                        <Link to="/user/editinfo">Edit profile</Link>
                    </div>
                </div>

                <button className="navbar-logout-btn" onClick={() => {
                    localStorage.removeItem('currentUser');
                    localStorage.removeItem('jwtToken');
                    localStorage.removeItem('userId');
                    window.location.href = '/';
                }}>
                    Logout
                </button>

        </div>

        
        <button className="mobile-menu-toggle" onClick={() => setShowMobileMenu(!showMobileMenu)}>
            <div className="hamburger-icon">
                <span></span>
                <span></span>
                <span></span>
            </div>
            <span className="menu-text">Menu</span>
        </button>

        
        {showMobileMenu && (
            <div className="mobile-menu-overlay" onClick={() => setShowMobileMenu(false)}>
                <div className="mobile-menu" onClick={(e) => e.stopPropagation()}>
                    <div className="mobile-menu-header">
                        <h3>Menu</h3>
                        <button className="close-mobile-menu" onClick={() => setShowMobileMenu(false)}>×</button>
                    </div>
                    <div className="mobile-menu-content">
                        <div className="mobile-menu-icons">
                            <div className="mobile-menu-item" onClick={() => { handleSearchClick(); setShowMobileMenu(false); }}>
                                <img src={assets.search_icon} alt="Search" />
                                <span>Search</span>
                            </div>
                            <div className="mobile-menu-item" onClick={() => { navigate('/user/cart'); setShowMobileMenu(false); }}>
                                <div className="mobile-cart-wrapper">
                                    <img src={assets.basket_icon} alt="Cart" />
                                    {cartItemCount > 0 && <div className="mobile-cart-badge">{cartItemCount}</div>}
                                </div>
                                <span>Cart</span>
                            </div>
                            <div className="mobile-menu-item">
                                <img src={assets.profile_icon} alt="Profile" />
                                <span>{currentUser ? currentUser.name : 'Profile'}</span>
                            </div>
                        </div>
                        <div className="mobile-menu-links">
                            <Link to="/user/myorders" onClick={() => setShowMobileMenu(false)}>My Orders</Link>
                            <Link to="/user/editinfo" onClick={() => setShowMobileMenu(false)}>Edit Profile</Link>
                            <Link to="/user/about" onClick={() => setShowMobileMenu(false)}>About</Link>
                            <Link to="/user/contact" onClick={() => setShowMobileMenu(false)}>Contact</Link>
                            <button className="mobile-logout-btn" onClick={() => {
                                localStorage.removeItem('currentUser');
                                localStorage.removeItem('jwtToken');
                                localStorage.removeItem('userId');
                                window.location.href = '/';
                            }}>
                                Logout
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        )}

        
        {showSearch && (
            <div className="search-overlay" onClick={handleCloseSearch}>
                <div className="search-box" onClick={(e) => e.stopPropagation()}>
                    <div className="search-header">
                        <h3>Search Food</h3>
                        <button className="close-search" onClick={handleCloseSearch}>
                            ×
                        </button>
                    </div>
                    
                    <div className="search-input-container">
                        <input
                            type="text"
                            className="search-input"
                            placeholder="Search for dishes..."
                            value={searchQuery}
                            onChange={handleSearchInput}
                            autoFocus
                        />
                    </div>
                    
                    <div className="search-results">
                        {isSearching && (
                            <div className="search-placeholder">
                                <p>Searching...</p>
                            </div>
                        )}
                        
                        {!isSearching && searchQuery.length < 2 && (
                            <div className="search-placeholder">
                                <p>Type at least 2 characters to search</p>
                            </div>
                        )}
                        
                        {!isSearching && searchQuery.length >= 2 && searchResults.length === 0 && (
                            <div className="no-results">
                                <p>No results found</p>
                                <small>Try searching with different keywords</small>
                            </div>
                        )}
                        
                        {!isSearching && searchResults.length > 0 && (
                            searchResults.map((item) => (
                                <div key={item.id} className="search-result-item">
                                    <div className="item-image-container">
                                        <img 
                                            src={item.image || assets.food_1} 
                                            alt={item.name} 
                                            className="item-image"
                                        />
                                        {!itemCounts[item.id] ? (
                                            <img
                                                className="search-add-icon"
                                                onClick={() => addToCart(item.id)}
                                                src={assets.add_icon_white}
                                                alt="Add"
                                            />
                                        ) : (
                                            <div className="search-item-counter">
                                                <img
                                                    onClick={() => removeFromCart(item.id)}
                                                    src={assets.remove_icon_red}
                                                    alt="Remove"
                                                    style={{ width: '20px', cursor: 'pointer' }}
                                                />
                                                <p style={{ margin: 0, fontSize: '14px', fontWeight: 'bold' }}>
                                                    {itemCounts[item.id]}
                                                </p>
                                                <img
                                                    onClick={() => addToCart(item.id)}
                                                    src={assets.add_icon_green}
                                                    alt="Add"
                                                    style={{ width: '20px', cursor: 'pointer' }}
                                                />
                                            </div>
                                        )}
                                    </div>
                                    
                                    <div className="item-details" style={{ flex: 1 }}>
                                        <h4 style={{ margin: '0 0 8px 0', fontSize: '16px', fontWeight: '600' }}>
                                            {item.name}
                                        </h4>
                                        <p style={{ margin: '0 0 8px 0', fontSize: '14px', color: '#6c757d' }}>
                                            {item.description || 'Delicious food item'}
                                        </p>
                                        <p style={{ margin: 0, fontSize: '16px', fontWeight: 'bold', color: '#667eea' }}>
                                            ₹{item.price}
                                        </p>
                                    </div>
                                </div>
                            ))
                        )}
                    </div>
                </div>
            </div>
        )}

    </div>
  )
}

export default Navbar