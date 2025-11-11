import React from 'react';
import './Contact.css';
import Navbar from '../../../components/User/Navbar/Navbar';
import Footer from '../../../components/User/Footer/Footer';

const Contact = () => {
  return (
    <div>
      <Navbar />
      <div className="contact-container">
        <h1>Contact Us</h1>
        <p>We'd love to hear from you! Fill out the form below or reach us at <b>support@quickbite.com</b>.</p>
        <form className="contact-form">
          <input type="text" placeholder="Your Name" required />
          <input type="email" placeholder="Your Email" required />
          <textarea placeholder="Your Message" rows="4" required />
          <button type="submit">Send Message</button>
        </form>
        <div className="contact-details">
          <p><b>Phone:</b> +91 12345 67890</p>
          <p><b>Address:</b> 123, Food Street, Delhi, India</p>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default Contact;