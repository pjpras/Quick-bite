import React, { useContext, useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import { toast } from 'react-toastify'
import './PlaceOrder.css'
import { useEffect } from 'react'
import Navbar from '../Navbar/NavBar'
import api from '../../../config/api' 

const PlaceOrder = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const [firstname, setFirstname] = useState();
    const [lastname, setLastname] = useState();
    const [street, setStreet] = useState();
    const [city, setCity] = useState();
    const [state, setState] = useState();
    const [zipcode, setZipcode] = useState();
    const [phone, setPhone] = useState();

    const [cartItems, setCartItems] = useState([]);
    const [currentUser, setCurrentUser] = useState(null);
    const [loading, setLoading] = useState(true);

    // Get discount info from navigation state (from Cart)
    const discountPercentage = location.state?.discountPercentage || 0;
    const isPromocodeApplied = location.state?.isPromocodeApplied || false;
    const promocode = location.state?.promocode || '';

    useEffect(() => {
        loadCartAndUser();
    }, []);

    const loadCartAndUser = async () => {
        try {
            const cartResponse = await api.get('/app2/api/v1/cart');
            setCartItems(cartResponse.data);

            const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
            setCurrentUser(user);
            setLoading(false);
        } catch (error) {
            setCartItems([]);
            setLoading(false);
        }
    };

    const totalPrice = cartItems.reduce((sum, item) => sum + item.totalPrice, 0);
    const deliverycharge = 30;

    // Use integer percentage (e.g., 10 for 10%)
    const discountAmount = (discountPercentage > 0 ? (discountPercentage / 100) * totalPrice : 0);
    const finalTotal = totalPrice + deliverycharge - discountAmount;

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const orderData = {
                address: {
                    firstName: firstname,
                    lastName: lastname,
                    street: street,
                    city: city,
                    state: state,
                    pin: zipcode,
                    phoneNo: phone
                },
                items: cartItems.map(item => ({
                    foodId: item.foodId,
                    quantity: item.quantity
                })),
                
                ...(isPromocodeApplied && promocode ? { promocode } : {})
            };

            const res = await api.post('/app2/api/v1/orders/place', orderData);

            if (res.status === 201) {
                toast.success('Order placed successfully!');
                await api.delete('/app2/api/v1/cart/clear');
                navigate('/user/myorders');
            }
        } catch (err) {
            toast.error(err.response?.data?.message || 'Order placement failed. Please try again.');
        }
    }

    return (
        <>
            <Navbar/>
            <form className='place-order main-content' onSubmit={handleSubmit}>
                <div className='place-order-left'>
                    <p className="title">Delivery Information</p>
                    <div className='multi-fields'>
                        <input
                            type="text"
                            name="firstName"
                            placeholder='First Name'
                            onChange={(e) => setFirstname(e.target.value)}
                            required
                        />
                        <input
                            type="text"
                            name="lastName"
                            placeholder='Last Name'
                            onChange={(e) => setLastname(e.target.value)}
                            required
                        />
                    </div>
                    <input
                        type="text"
                        name="street"
                        placeholder='Street'
                        onChange={(e) => setStreet(e.target.value)}
                        required
                    />
                    <div className="multi-fields">
                        <input
                            type="text"
                            name="city"
                            placeholder='City'
                            onChange={(e) => setCity(e.target.value)}
                            required
                        />
                        <input
                            type="text"
                            name="state"
                            placeholder='State'
                            onChange={(e) => setState(e.target.value)}
                            required
                        />
                    </div>
                    <input
                        type="text"
                        name="zipCode"
                        placeholder='Zip Code'
                        onChange={(e) => setZipcode(e.target.value)}
                        required
                    />
                    <input
                        type="tel"
                        name="phone"
                        placeholder='Phone'
                        onChange={(e) => setPhone(e.target.value)}
                        required
                    />
                </div>
                <div className="place-order-right">
                    <div className="cart-total">
                        <h2>Cart Totals</h2>
                        {/* No promocode section here */}
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
                        <button type="submit" className="">
                            <div className="order-loader">
                                <div className=""></div>
                                <span>Place Order</span>
                            </div>
                        </button>
                    </div>
                </div>
            </form>
        </>
    )
}

export default PlaceOrder