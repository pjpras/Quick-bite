import React, { useContext } from "react";
import { useNavigate } from "react-router-dom";
import './Cart.css'
import { useEffect } from "react";
import Navbar from "../../../components/User/Navbar/Navbar";
import { useState } from "react";
import { assets } from "../../../assets/assets";
import api from "../../../config/api";
import { toast } from "react-toastify";

const Cart = () => {

    const navigate = useNavigate();

    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);

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

    const handleRemoveFromCart = async (cartItemId, foodId) => {
        try {
            // Use the correct endpoint: /remove/{foodId}
            const response = await api.delete(`/app2/api/v1/cart/remove/${foodId}`);
            toast.success('Item removed from cart');
            // Reload the cart after removal
            loadCart();
            // Dispatch event to update cart count in navbar
            window.dispatchEvent(new Event('cartUpdated'));
        } catch (error) {
            console.error('Error removing item from cart:', error);
            console.error('Error details:', error.response?.data);
            toast.error(error.response?.data?.message || 'Failed to remove item from cart');
        }
    };

    const totalItems = cartItems.reduce((sum, item) => sum + item.quantity, 0);
    const totalPrice = cartItems.reduce((sum, item) => sum + item.totalPrice, 0);
    const deliverycharge = 30;
    const finalTotal = totalPrice + deliverycharge;

    const handleProceedToCheckout = () => {
        navigate("/user/placeorder");
    };

    return (

        <div className="cart main-content">
            <Navbar />
            <div className="cart-items">
                <div className="cart-items-title cart-items-item-header">
                    <p>Item</p>
                    <p>Title</p>
                    <p>Price</p>
                    <p>Quantity</p>
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
                                <p className="cart-item-quantity">
                                    <span className="quantity-badge">{item.quantity}</span>
                                </p>
                                <p>&#8377;{item.totalPrice}</p>
                                <p className="cross" onClick={() => handleRemoveFromCart(item.id, item.foodId)}>x</p>
                            </div>
                            <hr />
                        </div>
                    ))
                )}

            </div>
            <div className="cart-bottom">
                <div className="cart-total">
                    <h2>Cart Totals</h2>
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