import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import { ToastContainer } from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css'


import LandingPage from './pages/LandingPage/LandingPage'


import Login from './components/Auth/Login'
import Signup from './components/Auth/Signup'


import User from './components/User/User'
import Admin from './components/Admin/Admin'
import DeliveryPartner from './components/DeliveryPartner/DeliveryPartner'
import EditUserInfo from './components/User/EditUserInfo/EditUserInfo'

import About from './components/User/About/About'
import Contact from './components/User/Contact/Contact'
import Cart from './components/User/Cart/Cart'

import './App.css'
import PlaceOrder from './components/User/Placeorder/Placeorder'
import MyOrders from './components/User/MyOrders/MyOrders'
import AddProducts from './components/Admin/AddProducts/AddProducts'
import MenuManagement from './components/Admin/MenuManagement/MenuManagement'
import ViewOrders from './components/Admin/ViewOrders/ViewOrders'
import ViewCustomers from './components/Admin/ViewCustomers/ViewCustomers'
import ViewDeliverypartners from './components/Admin/ViewDeliverypartners/ViewDeliverypartners'
import EditProducts from './components/Admin/EditProducts/EditProducts'
import AddCategory from './components/Admin/AddCategory/AddCategory'

const App = () => {

  return (
    <div className="app">
      <Routes>
       
        <Route path="/" element={<LandingPage />} />
        
      
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        
       
        <Route path="/user" element={<User />} />
        <Route path="/user/editinfo" element={<EditUserInfo />} />
        <Route path="/user/cart" element={<Cart/>} />
        <Route path="/user/placeorder" element={<PlaceOrder/>} />
        <Route path="/user/myorders" element={<MyOrders/>} />
        
        
        
        <Route path="/admin" element={<Admin />} />
        <Route path="/admin/MenuManagement" element={<MenuManagement />} />
        <Route path="/admin/add-product" element={<AddProducts />} />
        <Route path="/admin/add-category" element={<AddCategory />} />
        <Route path="/admin/view-orders" element={<ViewOrders />} />
        <Route path="/admin/view-customers" element={<ViewCustomers />} />
        <Route path="/admin/view-delivery-partners" element={<ViewDeliverypartners />} />
        <Route path="admin/edit-product" element={<EditProducts />} />
        
        <Route path="/delivery-partner" element={<DeliveryPartner />} />
       
      </Routes>
      
      <ToastContainer 
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
      />
    </div>
  )
}

export default App
