import React, { useState, useEffect } from "react";
import "./DisplayMenu.css";
import FoodItem from "../FoodItem/FoodItem";
import api from "../../../config/api";

const ITEMS_PER_PAGE = 8;

const DisplayMenu = ({ category }) => {
  const [foodList, setFoodList] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(true);
  const [allCategoryFoods, setAllCategoryFoods] = useState([]);

  useEffect(() => {
    setCurrentPage(1);
  }, [category]);

  useEffect(() => {
    fetchProducts();
  }, [currentPage, category]);

  const fetchProducts = async () => {
    try {
      setLoading(true);
      console.log(`Fetching page ${currentPage}, category: ${category}`);

      if (category && category !== "all") {
        if (currentPage === 1 || allCategoryFoods.length === 0) {
          const url = `/app2/api/v1/food/category?category=${encodeURIComponent(
            category
          )}`;
          console.log("API URL (category filter):", url);
          const res = await api.get(url);
          console.log("API Response:", res.data);

          const foods = Array.isArray(res.data) ? res.data : [];
          setAllCategoryFoods(foods);

          const pageSize = 8;
          const totalPgs = Math.ceil(foods.length / pageSize);
          const startIdx = (currentPage - 1) * pageSize;
          const endIdx = startIdx + pageSize;
          const paginatedFoods = foods.slice(startIdx, endIdx);

          setFoodList(paginatedFoods);
          setTotalPages(totalPgs);
          console.log(
            "Filtered foods:",
            foods.length,
            "Total pages:",
            totalPgs,
            "Showing:",
            paginatedFoods.length
          );
        } else {
          const pageSize = 8;
          const startIdx = (currentPage - 1) * pageSize;
          const endIdx = startIdx + pageSize;
          const paginatedFoods = allCategoryFoods.slice(startIdx, endIdx);

          setFoodList(paginatedFoods);
          setTotalPages(Math.ceil(allCategoryFoods.length / pageSize));
          console.log(
            "Using cached category foods, showing page:",
            currentPage
          );
        }
      } else {
        setAllCategoryFoods([]);

        const url = `/app2/api/v1/food/active?page=${currentPage}`;
        console.log("API URL (all foods):", url);
        const res = await api.get(url);
        console.log("API Response:", res.data);
        console.log("Content array:", res.data.content);
        console.log("Total pages:", res.data.totalPages);

        setFoodList(res.data.content || []);
        setTotalPages(res.data.totalPages || 1);
      }
    } catch (err) {
      console.error("Failed to fetch products", err);
      console.error("Error details:", err.response?.data);
      console.error("Error status:", err.response?.status);
      setFoodList([]);
      setTotalPages(1);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="food-display" id="food-display">
        <h2>Loading menu...</h2>
      </div>
    );
  }

  if (foodList.length === 0) {
    return (
      <div className="food-display" id="food-display">
        <h2>No food items available</h2>
        <p>Please check back later or contact support.</p>
      </div>
    );
  }

  const handlePageChange = (page) => {
    setCurrentPage(page);
    document
      .getElementById("food-display")
      ?.scrollIntoView({ behavior: "smooth" });
  };

  return (
    <div className="food-display" id="food-display">
      <h2>The best dishes from our restaurant</h2>
      <div className="food-display-list">
        {foodList.map((item) => (
          <FoodItem
            key={item.id}
            id={item.id}
            name={item.name}
            price={item.price}
            description={item.description}
            image={item.img}
            avgRating={item.avgRating}
            category={item.category}
          />
        ))}
      </div>

      <div className="pagination">
        <button
          disabled={currentPage === 1}
          onClick={() => handlePageChange(currentPage - 1)}
        >
          Prev
        </button>
        {Array.from({ length: totalPages }, (_, i) => i + 1).map((pageNum) => (
          <button
            key={pageNum}
            className={currentPage === pageNum ? "active" : ""}
            onClick={() => handlePageChange(pageNum)}
          >
            {pageNum}
          </button>
        ))}
        <button
          disabled={currentPage === totalPages}
          onClick={() => handlePageChange(currentPage + 1)}
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default DisplayMenu;
