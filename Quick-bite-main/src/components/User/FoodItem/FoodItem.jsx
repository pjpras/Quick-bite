import React, { useState, useEffect } from "react";
import "./FoodItem.css";
import { assets } from "../../../assets/assets";
import api from "../../../config/api";

const FoodItem = ({
  id,
  name,
  price,
  description,
  image,
  avgRating,
  category,
}) => {
  const [itemCount, setItemCount] = useState(0);

  useEffect(() => {
    loadCartQuantity();
  }, [id]);

  const loadCartQuantity = async () => {
    try {
      const response = await api.get("/app2/api/v1/cart");
      const cartItems = response.data;

      const existingItem = cartItems.find((item) => item.foodId === id);
      if (existingItem) {
        setItemCount(existingItem.quantity);
      } else {
        setItemCount(0);
      }
    } catch (error) {
      console.error("Error loading cart:", error);
      setItemCount(0);
    }
  };

  const addToCart = async () => {
    try {
      console.log("Adding to cart - Food ID:", id);
      const newCount = itemCount + 1;

      const response = await api.post("/app2/api/v1/cart/add", {
        foodId: id,
        quantity: 1,
      });

      console.log("Add to cart response:", response.data);
      setItemCount(newCount);
      console.log("Item added to cart successfully");

      window.dispatchEvent(new Event("cartUpdated"));
    } catch (error) {
      console.error("Error adding to cart:", error);
      console.error("Error response:", error.response?.data);
      console.error("Error status:", error.response?.status);
    }
  };

  const removeFromCart = async () => {
    if (itemCount <= 0) return;

    try {
      console.log("Removing from cart - Food ID:", id);
      const newCount = itemCount - 1;

      if (newCount === 0) {
        const response = await api.delete(`/app2/api/v1/cart/remove/${id}`);
        console.log("Remove from cart response:", response.data);
        setItemCount(0);
      } else {
        const response = await api.patch(
          `/app2/api/v1/cart/update/${id}?quantity=${newCount}`
        );
        console.log("Update cart response:", response.data);
        setItemCount(newCount);
      }

      console.log("Cart updated successfully");

      window.dispatchEvent(new Event("cartUpdated"));
    } catch (error) {
      console.error("Error removing from cart:", error);
      console.error("Error response:", error.response?.data);
      console.error("Error status:", error.response?.status);
    }
  };

  const getRatingImage = (rating) => {
    console.log("Getting rating image for:", rating);
    if (rating >= 0 && rating <= 1.2) {
      return assets["1"];
    } else if (rating >= 1.21 && rating <= 1.7) {
      return assets["1.5"];
    } else if (rating >= 1.71 && rating <= 2.2) {
      return assets["2"];
    } else if (rating >= 2.21 && rating <= 2.7) {
      return assets["2.5"];
    } else if (rating >= 2.71 && rating <= 3.2) {
      return assets["3"];
    } else if (rating >= 3.21 && rating <= 3.7) {
      return assets["3.5"];
    } else if (rating >= 3.71 && rating <= 4.2) {
      return assets["4"];
    } else if (rating >= 4.21 && rating <= 4.7) {
      return assets["4.5"];
    } else if (rating >= 4.71 && rating <= 5) {
      return assets["5"];
    } else {
      return assets["1"];
    }
  };

  return (
    <div className="food-item">
      <div className="food-item-img-container">
        <img className="food-item-image" src={image} alt={name} />
        {itemCount === 0 ? (
          <img
            className="add"
            onClick={addToCart}
            src={assets.add_icon_white}
          />
        ) : (
          <div className="food-item-counter">
            <img onClick={removeFromCart} src={assets.remove_icon_red} alt="" />
            <p>{itemCount}</p>
            <img onClick={addToCart} src={assets.add_icon_green} alt="" />
          </div>
        )}
      </div>
      <div className="food-item-info">
        <div className="food-item-name-rating">
          <p>{name}</p>
          <img src={getRatingImage(avgRating)} alt="" />
        </div>
        <p className="food-item-category">
          {category?.name || "Uncategorized"}
        </p>
        <p className="food-item-description">{description}</p>
        <p className="food-item-price">&#8377;{price}</p>
      </div>
    </div>
  );
};

export default FoodItem;
