import React from 'react';
import './About.css';

const About = () => {
  return (
    <div className="about-container">
      <h1>About QuickBite</h1>
      <p>
        QuickBite is your go-to place for delicious, authentic food delivered fast and fresh. We believe in quality, taste, and great service. Enjoy a variety of dishes from our menu, made with love since 1957.
      </p>
      <div className="about-details">
        <p><b>Our Mission:</b> To serve the best food with a smile.</p>
        <p><b>Contact:</b> support@quickbite.com</p>
      </div>
    </div>
  );
};

export default About;