import React from 'react'
import './Header.css'

const Header = () => {
  const scrollToMenu = () => {
    const menuElement = document.getElementById('explore-menu');
    if (menuElement) {
      menuElement.scrollIntoView({ behavior: 'smooth' });
    }
  };

  return (
    <div className='header' style={{backgroundImage: `url(/header_img.jpg)`}}>
        <div className='header-content'>
            <h2>Order Your favorite food here</h2>
            
            <button onClick={scrollToMenu}>View Menu</button>
        </div>

    </div>
  )
}

export default Header