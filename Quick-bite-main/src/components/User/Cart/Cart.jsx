import React, { useContext } from "react";
import { useNavigate } from "react-router-dom";
import './Cart.css'
import { useEffect } from "react";
import Navbar from "../Navbar/NavBar";
import { useState } from "react";
import { assets } from "../../../assets/assets";
import api from "../../../config/api";
import { toast } from "react-toastify";

const Cart = () => {

    const navigate = useNavigate();

    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);

    // Promocode state
    const [promocode, setPromocode] = useState('');
    const [promocodeError, setPromocodeError] = useState('');
    const [discountPercentage, setDiscountPercentage] = useState(0);
    const [isPromocodeApplied, setIsPromocodeApplied] = useState(false);
    const [promocodeLoading, setPromocodeLoading] = useState(false);

    useEffect(() => {
        loadCart();
    }, []);

    const loadCart = async () => {
        try {
            const response = await api.get('/app2/api/v1/cart');
            setCartItems(response.data);
            setLoading(false);
        } catch (error) {
            console.error('Error loading cart:', error);
            setCartItems([]);
            setLoading(false);
        }
    };

    const totalItems = cartItems.reduce((sum, item) => sum + item.quantity, 0);
    const totalPrice = cartItems.reduce((sum, item) => sum + item.totalPrice, 0);
    const deliverycharge = 30;

    // Calculate discount and final total
    const discountAmount = (discountPercentage > 0 ? (discountPercentage / 100) * totalPrice : 0);
    const finalTotal = totalPrice + deliverycharge - discountAmount;

    // Promocode logic
    const validatePromocode = async () => {
        if (!promocode.trim()) {
            setPromocodeError('Please enter a promocode');
            return;
        }
        try {
            setPromocodeLoading(true);
            setPromocodeError('');
            const response = await api.get(`/app2/api/v1/orders/customer/promocode?code=${promocode.trim()}`);
            if (response.status === 200 && response.data && response.data.discountPercentage) {
                const discount = response.data.discountPercentage;
                setDiscountPercentage(discount);
                setIsPromocodeApplied(true);
                setPromocodeError('');
                toast.success(`Promocode applied! ${discount}% discount`);
            }
        } catch (error) {
            setDiscountPercentage(0);
            setIsPromocodeApplied(false);
            if (error.response?.status === 404) {
                setPromocodeError('Invalid promocode');
            } else if (error.response?.status === 400) {
                setPromocodeError('Promocode expired or not valid');
            } else {
                setPromocodeError('Unable to validate promocode. Please try again.');
            }
        } finally {
            setPromocodeLoading(false);
        }
    };

    const removePromocode = () => {
        setPromocode('');
        setPromocodeError('');
        setDiscountPercentage(0);
        setIsPromocodeApplied(false);
    };

    const handleProceedToCheckout = () => {
        navigate("/user/placeorder", {
            state: {
                discountPercentage,
                isPromocodeApplied,
                promocode
            }
        });
    };

    return (

        <div className="cart main-content">
            <Navbar />
            <div className="cart-items">
                <div className="cart-items-title cart-items-item-header">
                    <p>Item</p>
                    <p>Title</p>
                    <p>Price</p>
                    <p>Total</p>
                    <p>Remove</p>
                </div>
                <hr />

                {loading ? (
                    <div className="cart-loading">Loading cart...</div>
                ) : cartItems.length === 0 ? (
                    <div className="cart-empty">Your cart is empty</div>
                ) : (
                    cartItems.map((item) => (
                        <div key={item.id}>
                            <div className='cart-items-item'>
                                <img 
                                    src={item.foodImage || assets.food_1} 
                                    alt={item.foodName || 'Food item'}
                                    onError={(e) => {
                                        e.target.src = assets.food_1;
                                    }}
                                />
                                <p>{item.foodName}</p>
                                <p>&#8377;{item.price}</p>
                                <p>&#8377;{item.totalPrice}</p>
                                <p className="cross">x</p>
                                <span className="item-quantity">x{item.quantity}</span>
                            </div>
                            <hr />
                        </div>
                    ))
                )}

            </div>
            <div className="cart-bottom">
                <div className="cart-total">
                    <h2>Cart Totals</h2>
                    {/* Promocode Section */}
                    <div className="promocode-section">
                        <div className="promocode-input-container">
                            <input
                                type="text"
                                placeholder="Enter promocode"
                                value={promocode}
                                onChange={(e) => setPromocode(e.target.value)}
                                className={`promocode-input ${promocodeError ? 'error' : ''} ${isPromocodeApplied ? 'success' : ''}`}
                                disabled={isPromocodeApplied}
                            />
                            {!isPromocodeApplied ? (
                                <button 
                                    type="button"
                                    onClick={validatePromocode}
                                    disabled={promocodeLoading || !promocode.trim()}
                                    className="promocode-btn"
                                >
                                    {promocodeLoading ? 'Checking...' : 'Apply'}
                                </button>
                            ) : (
                                <button 
                                    type="button"
                                    onClick={removePromocode}
                                    className="promocode-remove-btn"
                                >
                                    Remove
                                </button>
                            )}
                        </div>
                        {promocodeError && (
                            <div className="promocode-error">
                                {promocodeError}
                            </div>
                        )}
                        {isPromocodeApplied && (
                            <div className="promocode-success">
                                Promocode applied! {discountPercentage}% discount
                            </div>
                        )}
                    </div>
                    <div>
                        <div className="cart-total-details">
                            <p>Subtotal</p>
                            <p>₹{totalPrice}</p>
                        </div>
                        <hr />
                        <div className="cart-total-details">
                            <p>Delivery Charges</p>
                            <p>₹{deliverycharge}</p>
                        </div>
                        {isPromocodeApplied && discountAmount > 0 && (
                            <>
                                <hr />
                                <div className="cart-total-details discount-row">
                                    <p>Discount ({discountPercentage}%)</p>
                                    <p className="discount-amount">-₹{discountAmount.toFixed(2)}</p>
                                </div>
                            </>
                        )}
                        <hr />
                        <div className="cart-total-details">
                            <b>Total</b>
                            <b>₹{finalTotal.toFixed(2)}</b>
                        </div>
                    </div>
                    <button onClick={handleProceedToCheckout}>PROCEED TO CHECKOUT</button>
                </div>
            </div>
        </div>
    )
}

export default Cart;