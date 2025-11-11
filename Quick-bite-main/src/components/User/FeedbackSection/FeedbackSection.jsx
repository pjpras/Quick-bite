import React from 'react';
import './FeedbackSection.css';

const FeedbackSection = () => {
    const feedbacks = [
        {
            id: 1,
            name: "Sarah Johnson",
            image: "https://images.unsplash.com/photo-1494790108755-2616b612b786?w=150&h=150&fit=crop&crop=face",
            rating: 5,
            feedback: "Amazing food quality and super fast delivery! The pasta was perfectly cooked and still hot when it arrived. Highly recommend QuickBite!"
        },
        {
            id: 2,
            name: "Mike Chen",
            image: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&h=150&fit=crop&crop=face",
            rating: 4,
            feedback: "Great variety of food options and excellent customer service. The app is easy to use and payment process is smooth and secure."
        },
        {
            id: 3,
            name: "Emma Davis",
            image: "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150&h=150&fit=crop&crop=face",
            rating: 5,
            feedback: "Best food delivery service in town! Fresh ingredients, tasty meals, and always delivered on time. My family loves ordering from QuickBite!"
        },
        {
            id: 4,
            name: "David Wilson",
            image: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop&crop=face",
            rating: 4,
            feedback: "Impressed with the food quality and packaging. Everything arrived fresh and well-presented. The delivery partner was also very polite and professional."
        }
    ];

    const showStars = (rating) => {
        let stars = '';
        for (let i = 0; i < 5; i++) {
            if (i < rating) {
                stars += '⭐';
            } else {
                stars += '☆';
            }
        }
        return stars;
    };

    return (
        <div className="feedback-section">
            <div className="feedback-container">
                <h2>What Our Customers Say</h2>
                <div className="feedback-grid">
                    {feedbacks.map((feedback) => (
                        <div key={feedback.id} className="feedback-card">
                            <div className="customer-info">
                                <img 
                                    src={feedback.image} 
                                    alt={feedback.name} 
                                    className="customer-image"
                                    onError={(e) => {
                                        e.target.onerror = null;
                                        e.target.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(feedback.name)}&background=667eea&color=fff&size=150`;
                                    }}
                                />
                                <div className="customer-details">
                                    <h3>{feedback.name}</h3>
                                    <div className="rating">
                                        {showStars(feedback.rating)}
                                    </div>
                                </div>
                            </div>
                            <p className="feedback-text">"{feedback.feedback}"</p>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default FeedbackSection;
