import React, { useContext, useEffect, useState } from 'react'
import './MyOrders.css'
import { assets } from '../../../assets/assets';
import Navbar from '../../../components/User/Navbar/Navbar';
import api from '../../../config/api';
import { toast } from 'react-toastify';

const MyOrders = () => {
    const [orders, setOrders] = useState([]);
    const [currentUser, setCurrentUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [ratingStates, setRatingStates] = useState({});
    const [ratingStatusCache, setRatingStatusCache] = useState({});

    useEffect(() => {
        const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
        setCurrentUser(user);
        fetchOrders();
    }, []);

    const loadRatingStatuses = async (orders, customerId) => {
        if (!customerId) return;
        
        for (const order of orders) {
            if (order.orderStatus?.toLowerCase() === "delivered" && order.orderItems) {
                for (const item of order.orderItems) {
                    if (item.foodId) {
                        await checkRatingStatus(item.foodId, order.id, customerId);
                    }
                }
            }
        }
    };

    const fetchOrders = async () => {
        try {
            setLoading(true);
            
            const token = localStorage.getItem('jwtToken');

            
            if (!token) {
                toast.error('Please login to view orders');
                setLoading(false);
                return;
            }
            
            const response = await api.get('/app2/api/v1/orders/all');
       

            const validOrders = (response.data || [])
                .filter(order => order !== null && order !== undefined && order.id)
                .sort((a, b) => {
                    // Handle cases where orderDate might be null or undefined
                    const dateA = a.orderDate ? new Date(a.orderDate) : new Date(0);
                    const dateB = b.orderDate ? new Date(b.orderDate) : new Date(0);
                    
                    // Sort in descending order (newest first)
                    return dateB - dateA;
                });

            setOrders(validOrders);
            
            
            const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
            if (user.id) {
                await loadRatingStatuses(validOrders, user.id);
            }
            
            setLoading(false);
        } catch (error) {
            if (error.response?.status === 401) {
                toast.error('Session expired. Please login again');
            } else if (error.response?.status === 500) {
                toast.error('Server error. Please try again later');
            } else {
                toast.error('Failed to load orders: ' + (error.response?.data?.message || error.message));
            }
            setOrders([]);
            setLoading(false);
        }
    };

    const formatDate = (dateString) => {
        if (!dateString) return 'N/A';
        try {
            const date = new Date(dateString);
            return date.toLocaleDateString('en-US'); 
        } catch {
            return 'N/A';
        }
    };

    const checkRatingStatus = async (foodId, orderId, customerId) => {
        try {
            const cacheKey = `${foodId}-${orderId}-${customerId}`;
            
           
            
            if (ratingStatusCache[cacheKey] !== undefined) {
                return ratingStatusCache[cacheKey];
            }            const response = await api.get(`/app2/api/v1/feedback/Rating/status/food?foodId=${foodId}&orderId=${orderId}&customerId=${customerId}`);
            
           
            let isRated = false;
            if (response.data === true || response.data === 'true' || response.data === 1 || response.data === '1') {
                isRated = true;
            } else if (response.data === false || response.data === 'false' || response.data === 0 || response.data === '0') {
                isRated = false;
            } else if (typeof response.data === 'object' && response.data !== null) {
               
                isRated = response.data.status === true || response.data.status === 'true' || 
                         response.data.isRated === true || response.data.rated === true || 
                         response.data.hasRating === true;
            }
            
            setRatingStatusCache(prev => ({
                ...prev,
                [cacheKey]: isRated
            }));
            
            return isRated;
        } catch (error) {
            return false;
        }
    };

    const submitFoodRating = async (foodId, rating, orderId, comment = '') => {
        try {
            const response = await api.post(`/app2/api/v1/feedback/food/${foodId}`, {
                foodRating: parseInt(rating),
                orderId: parseInt(orderId)  // 
            });

            if (response.status === 200 || response.status === 201) {
                toast.success('Rating submitted successfully!');
                
              
                setRatingStates(prev => ({
                    ...prev,
                    [foodId]: { ...prev[foodId], rated: true, rating: parseInt(rating) }
                }));
                
             
                const cacheKey = `${foodId}-${orderId}-${currentUser.id}`;
                setRatingStatusCache(prev => ({
                    ...prev,
                    [cacheKey]: true
                }));
            }
        } catch (error) {
            
            toast.error('Failed to submit rating: ' + (error.response?.data?.message || error.message));
        }
    };

    const cancelOrder = async (orderId) => {
        try {
            const token = localStorage.getItem('jwtToken');
            if (!token) {
                toast.error('Please login to cancel order');
                return;
            }

            const response = await api.patch(`/app2/api/v1/orders/cancel/${orderId}`, {}, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (response.status === 200 || response.status === 204) {
                toast.success('Order cancelled successfully!');
                fetchOrders();
            }
        } catch (error) {
            if (error.response?.status === 500) {
                // Show the specific error message from backend for 500 errors
                const errorMessage = error.response?.data?.errorMessage || error.response?.data?.message || 'Server error. Please try again or contact support.';
                toast.error(errorMessage);
            } else if (error.response?.status === 404) {
                toast.error('Order not found or already processed.');
            } else if (error.response?.status === 403) {
                toast.error('You are not authorized to cancel this order.');
            } else {
                toast.error('Failed to cancel order: ' + (error.response?.data?.message || error.message));
            }
        }
    };

    if (loading) {
        return (
            <>
                <Navbar />
                <div className='myorders-zomato'>
                    <div className="orders-container-zomato">
                        <div className="no-orders">Loading your orders...</div>
                    </div>
                </div>
            </>
        );
    }

    if (!currentUser || !currentUser.id) {
        return (
            <>
                <Navbar />
                <div className='myorders-zomato'>
                    <div className="orders-container-zomato">
                        <div className="no-orders">Please login to view your orders</div>
                    </div>
                </div>
            </>
        );
    }

    if (!orders || orders.length === 0) {
        return (
            <>
                <Navbar />
                <div className='myorders-zomato'>
                    <div className="orders-container-zomato">
                        <div className="orders-header">
                            <h1>Your Orders</h1>
                        </div>
                        <div className="no-orders">No orders found. Start ordering now!</div>
                    </div>
                </div>
            </>
        );
    }

    return (
        <>
            <Navbar />
            <div className='myorders-zomato'>
                <div className="orders-container-zomato">
                    <div className="orders-header">
                        <h1>Your Orders</h1>
                    </div>

                    <div className="orders-list">
                        {orders
                            .filter(order => order.id)
                            .map((order) => {
                                try {
                                    
                                    const FoodRatingComponent = ({ item, orderId }) => {
                                        const [isRated, setIsRated] = useState(null);
                                        const [loading, setLoading] = useState(true);
                                        
                                        useEffect(() => {
                                            const loadRatingStatus = async () => {
                                                if (currentUser?.id && item.foodId) {
                                                    const rated = await checkRatingStatus(item.foodId, orderId, currentUser.id);
                                                    setIsRated(rated);
                                                }
                                                setLoading(false);
                                            };
                                            
                                            loadRatingStatus();
                                        }, [item.foodId, orderId]);
                                        
                                        if (loading) {
                                            return <div className="rating-loading">Checking rating status...</div>;
                                        }
                                        
                                        if (isRated === true) {
                                            return (
                                                <div className="food-rating-display">
                                                    <span className="rating-star">★</span>
                                                    <span className="rated-text">Rated</span>
                                                </div>
                                            );
                                        }
                                        
                                        return (
                                            <select 
                                                className="rate-food-btn" 
                                                value="0"
                                                onChange={(e) => {
                                                    const rating = e.target.value;
                                                    if (rating !== "0") {
                                                        submitFoodRating(item.foodId, rating, orderId);
                                                        setIsRated(true);
                                                    }
                                                }}
                                            >
                                                <option value="0">Rate Food</option>
                                                <option value="5">★★★★★ </option>
                                                <option value="4">★★★★☆ </option>
                                                <option value="3">★★★☆☆ </option>
                                                <option value="2">★★☆☆☆ </option>
                                                <option value="1">★☆☆☆☆ </option>
                                            </select>
                                        );
                                    };

                                    return (
                                        <div key={order.id} className="order-card-zomato">

                                            <div className="order-header-zomato">
                                                <div className="restaurant-info">
                                                    {order?.otp && (
                                                        <p className="order-otp">OTP: {order.otp}</p>
                                                    )}
                                                    {order.assignDeliveryPerson && (
                                                        <p className="delivery-partner">
                                                            Delivery Partner: {order.assignDeliveryPerson}
                                                        </p>
                                                    )}
                                                </div>
                                                <div className="order-status-badge">
                                                    <div className={`status-dot ${(order.orderStatus || '').toLowerCase().replace('_', '-')}`}></div>
                                                    <span className="status-text">{order.orderStatus || 'Pending'}</span>
                                                </div>
                                            </div>

                                            <div className="order-items-section">
                                                {(order?.orderItems || [])
                                                    .filter(item => item && item.foodName) 
                                                    .map((item, index) => (
                                                        <div key={`${order.id}-item-${index}`} className="order-item">
                                                            <div className="item-details-zomato">
                                                                <span className="item-name">{item.foodName || 'Unknown item'}</span>
                                                                <span className="item-quantity">x{item.quantity || 1}</span>
                                                                <span className="item-price">₹{item.price || 0}</span>
                                                            </div>
                                                            {order.orderStatus?.toLowerCase() === "delivered" && (
                                                                <div className="item-rating">
                                                                    <FoodRatingComponent 
                                                                        item={item} 
                                                                        orderId={order.id} 
                                                                    />
                                                                </div>
                                                            )}
                                                        </div>
                                                    ))}
                                            </div>

                                            <div className="order-footer-zomato">
                                                <div className="order-info-left">
                                                    <p className="order-time">
                                                        {formatDate(order.orderDate)}
                                                        {order.orderTime && ` at ${order.orderTime.substring(0, 5)}`}
                                                    </p>
                                                    <p className="order-total">₹{order?.totalPrice || 0}</p>
                                                    <p className="order-qty">Total Items: {order?.totalQty || 0}</p>
                                                </div>
                                                <div className="order-actions">
                                                    {order.orderStatus?.toUpperCase() === "PENDING" && (
                                                        <button
                                                            className="cancel-btn"
                                                            onClick={() => {
                                                                if (window.confirm('Are you sure you want to cancel this order?')) {
                                                                    cancelOrder(order.id);
                                                                }
                                                            }}
                                                        >
                                                            CANCEL ORDER
                                                        </button>
                                                    )}
                                                    {order.orderStatus?.toLowerCase() !== "delivered" && 
                                                     order.orderStatus?.toUpperCase() !== "CANCELLED" && (
                                                        <button
                                                            className="track-btn"
                                                            onClick={() => window.location.reload()}
                                                        >
                                                            TRACK ORDER
                                                        </button>
                                                    )}
                                                </div>
                                            </div>

                                            <div className="order-id-footer">
                                                <span>Order ID:{order.id || 'N/A'}</span>
                                            </div>
                                        </div>
                                    );
                                } catch (error) {
                                    return (
                                        <div key={`error-${Math.random()}`} className="order-card-zomato">
                                            <div style={{ padding: '20px', color: 'red' }}>
                                                Error loading order data
                                            </div>
                                        </div>
                                    );
                                }
                            })}
                    </div>
                </div>
            </div>
        </>
    )
}

export default MyOrders