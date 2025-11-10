import React, { useState } from 'react'

import Header from './Header/Header'
import ExploreMenu from './ExploreMenu/ExploreMenu'
import DisplayMenu from './DisplayMenu/DisplayMenu'
import FeedbackSection from './FeedbackSection/FeedbackSection'
import Footer from './Footer/Footer'
import Navbar from './Navbar/NavBar'

const User = () => {

  const [category,setCategory] = useState('all')
  return (
    <div>
      <div className="main-content">
        <Navbar/>
        <Header/>
        <ExploreMenu category={category} setCategory={setCategory}/>
        <DisplayMenu category={category}/>
        <FeedbackSection/>
        <Footer/>
      </div>
    </div>
  )
}

export default User