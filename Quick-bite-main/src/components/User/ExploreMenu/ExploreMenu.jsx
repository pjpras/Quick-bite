import React, { useState, useEffect } from 'react'
import './ExploreMenu.css'
import { assets } from '../../../assets/assets'
import { api } from '../../../config/api'

const ExploreMenu = ({category,setCategory}) => {
  const [categories, setCategories] = useState([])

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const res = await api.get('/app2/api/v1/category');
        console.log('Categories from backend:', res.data);
        setCategories(res.data);
      } catch (err) {
        console.error('Failed to fetch categories', err);
      }
    };
    fetchCategories();
  }, []);

  return (
    <div className='explore-menu' id='explore-menu'>
        <h1>Explore our menu</h1>
        <p className='explore-menu-text'>Explore the authentic menu from our restaurant</p>
        <div className='explore-menu-list'>
            {categories.map((item, index) => {
                return (
                    <div onClick={()=>setCategory(prev=>prev===item.name?"all":item.name)} className='explore-menu-item' key={index}>
                        <img className={category===item.name?"Active":""} src={item.img} alt={item.name}/>
                        <h3>{item.name}</h3>
                    </div>
                )   
            })}
        </div>
        <hr/>
    </div>
  )
}

export default ExploreMenu