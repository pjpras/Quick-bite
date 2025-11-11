import React, { useContext, useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import { toast } from 'react-toastify'
import './PlaceOrder.css'
import { useEffect } from 'react'
import Navbar from '../../../components/User/Navbar/Navbar'
import api from '../../../config/api' 

const PlaceOrder = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [street, setStreet] = useState('');
    const [city, setCity] = useState('');
    const [state, setState] = useState('');
    const [zipcode, setZipcode] = useState('');
    const [phone, setPhone] = useState('');
    const [errors, setErrors] = useState({});

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

    // Validation functions
    const validateName = (name) => {
        const nameRegex = /^[a-zA-Z\s]{2,}$/;
        return nameRegex.test(name);
    };

    const validateCity = (city) => {
        const cityRegex = /^[a-zA-Z\s]{2,}$/;
        return cityRegex.test(city);
    };

    const validateState = (state) => {
        const stateRegex = /^[a-zA-Z\s]{2,}$/;
        return stateRegex.test(state);
    };

    const validateZipcode = (zipcode) => {
        const zipcodeRegex = /^[0-9]{6}$/;
        return zipcodeRegex.test(zipcode);
    };

    const validatePhone = (phone) => {
        const phoneRegex = /^[0-9]{10}$/;
        return phoneRegex.test(phone);
    };

    const validateForm = () => {
        const newErrors = {};

        if (!firstname.trim()) {
            newErrors.firstname = 'First name is required';
        } else if (firstname.trim().length < 2) {
            newErrors.firstname = 'First name must be at least 2 characters';
        } else if (!validateName(firstname)) {
            newErrors.firstname = 'First name should contain only letters';
        }

        if (!lastname.trim()) {
            newErrors.lastname = 'Last name is required';
        } else if (lastname.trim().length < 2) {
            newErrors.lastname = 'Last name must be at least 2 characters';
        } else if (!validateName(lastname)) {
            newErrors.lastname = 'Last name should contain only letters';
        }

        if (!street.trim()) {
            newErrors.street = 'Street address is required';
        } else if (street.trim().length < 5) {
            newErrors.street = 'Street address must be at least 5 characters';
        }

        if (!city.trim()) {
            newErrors.city = 'City is required';
        } else if (!validateCity(city)) {
            newErrors.city = 'City should contain only letters';
        }

        if (!state.trim()) {
            newErrors.state = 'State is required';
        } else if (!validateState(state)) {
            newErrors.state = 'State should contain only letters';
        }

        if (!zipcode.trim()) {
            newErrors.zipcode = 'Zip code is required';
        } else if (!validateZipcode(zipcode)) {
            newErrors.zipcode = 'Zip code must be 6 digits';
        }

        if (!phone.trim()) {
            newErrors.phone = 'Phone number is required';
        } else if (!validatePhone(phone)) {
            newErrors.phone = 'Phone number must be 10 digits';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validateForm()) {
            toast.error('Please fix the validation errors');
            return;
        }

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
            console.error('Order placement error:', err);
            console.error('Error response data:', err.response?.data);
            console.error('Error status:', err.response?.status);
            
            // Extract error message from various possible formats
            let errorMessage = 'Order placement failed. Please try again.';
            
            if (err.response?.data) {
                if (typeof err.response.data === 'string') {
                    errorMessage = err.response.data;
                } else if (err.response.data.errorMessage) {
                    // Backend returns errorMessage property
                    errorMessage = err.response.data.errorMessage;
                } else if (err.response.data.message) {
                    errorMessage = err.response.data.message;
                } else if (err.response.data.error) {
                    errorMessage = err.response.data.error;
                }
            } else if (err.message) {
                errorMessage = err.message;
            }
            
            console.log('Final error message to display:', errorMessage);
            toast.error(errorMessage, {
                position: "top-right",
                autoClose: 5000,
            });
        }
    }

    return (
        <>
            <Navbar/>
            <form className='place-order main-content' onSubmit={handleSubmit}>
                <div className='place-order-left'>
                    <p className="title">Delivery Information</p>
                    <div className='multi-fields'>
                        <div className="form-group">
                            <input
                                type="text"
                                name="firstName"
                                placeholder='First Name'
                                value={firstname}
                                onChange={(e) => {
                                    setFirstname(e.target.value);
                                    if (errors.firstname) setErrors({...errors, firstname: ''});
                                }}
                                className={errors.firstname ? 'input-error' : ''}
                            />
                            {errors.firstname && <span className="error-message">{errors.firstname}</span>}
                        </div>
                        <div className="form-group">
                            <input
                                type="text"
                                name="lastName"
                                placeholder='Last Name'
                                value={lastname}
                                onChange={(e) => {
                                    setLastname(e.target.value);
                                    if (errors.lastname) setErrors({...errors, lastname: ''});
                                }}
                                className={errors.lastname ? 'input-error' : ''}
                            />
                            {errors.lastname && <span className="error-message">{errors.lastname}</span>}
                        </div>
                    </div>
                    <div className="form-group">
                        <input
                            type="text"
                            name="street"
                            placeholder='Street'
                            value={street}
                            onChange={(e) => {
                                setStreet(e.target.value);
                                if (errors.street) setErrors({...errors, street: ''});
                            }}
                            className={errors.street ? 'input-error' : ''}
                        />
                        {errors.street && <span className="error-message">{errors.street}</span>}
                    </div>
                    <div className="multi-fields">
                        <div className="form-group">
                            <input
                                type="text"
                                name="city"
                                placeholder='City'
                                value={city}
                                onChange={(e) => {
                                    setCity(e.target.value);
                                    if (errors.city) setErrors({...errors, city: ''});
                                }}
                                className={errors.city ? 'input-error' : ''}
                            />
                            {errors.city && <span className="error-message">{errors.city}</span>}
                        </div>
                        <div className="form-group">
                            <input
                                type="text"
                                name="state"
                                placeholder='State'
                                value={state}
                                onChange={(e) => {
                                    setState(e.target.value);
                                    if (errors.state) setErrors({...errors, state: ''});
                                }}
                                className={errors.state ? 'input-error' : ''}
                            />
                            {errors.state && <span className="error-message">{errors.state}</span>}
                        </div>
                    </div>
                    <div className="form-group">
                        <input
                            type="text"
                            name="zipCode"
                            placeholder='Zip Code (6 digits)'
                            value={zipcode}
                            onChange={(e) => {
                                setZipcode(e.target.value);
                                if (errors.zipcode) setErrors({...errors, zipcode: ''});
                            }}
                            className={errors.zipcode ? 'input-error' : ''}
                            maxLength={6}
                        />
                        {errors.zipcode && <span className="error-message">{errors.zipcode}</span>}
                    </div>
                    <div className="form-group">
                        <input
                            type="tel"
                            name="phone"
                            placeholder='Phone (10 digits)'
                            value={phone}
                            onChange={(e) => {
                                setPhone(e.target.value);
                                if (errors.phone) setErrors({...errors, phone: ''});
                            }}
                            className={errors.phone ? 'input-error' : ''}
                            maxLength={10}
                        />
                        {errors.phone && <span className="error-message">{errors.phone}</span>}
                    </div>
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