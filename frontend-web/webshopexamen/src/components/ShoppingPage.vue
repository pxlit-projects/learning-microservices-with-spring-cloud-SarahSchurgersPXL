<template>
  <div class="container">
    <h1>Duurzame webshop</h1>
    <div class="search-filter">
      <input type="text" v-model="searchQuery" placeholder="Zoek producten..." />
      <select v-model="selectedCategory">
        <option value="">Alle categorieën</option>
        <option value="VERZORGING">Verzorging</option>
        <option value="ELEKTRONICA">Elektronica</option>
        <option value="KLEDING">Kleding</option>
        <option value="BOEKEN">Boeken</option>
        <option value="WONING">Woning</option>
        <option value="TUIN">Tuin</option>
        <option value="SPEELGOED">Speelgoed</option>
      </select>
      <input type="number" v-model.number="minPrice" placeholder="Min prijs" />
      <input type="number" v-model.number="maxPrice" placeholder="Max prijs" />
    </div>
    <ul>
      <li v-for="product in filteredProducts" :key="product.id">
        <div>
          <strong>{{ product.name }}</strong> - €{{ product.price }}
        </div>
        <div>
          {{ product.description }}
        </div>
        <input type="number" v-model.number="quantities[product.id]" min="1" placeholder="Aantal" />
        <button @click="addToCart(product.id)">Toevoegen aan winkelwagen</button>
      </li>
    </ul>    
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      products: [],
      searchQuery: '',
      selectedCategory: '',
      minPrice: null,
      maxPrice: null,
      cart: {
        id: null,
        items: []
      },
      quantities: {} // Object to store quantities for each product
    };
  },
  computed: {
    filteredProducts() {
      return this.products.filter(product => {
        const matchesSearchQuery = product.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
                                   product.description.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
                                   product.labels.some(label => label.toLowerCase().includes(this.searchQuery.toLowerCase()));
        const matchesCategory = this.selectedCategory === '' || product.category === this.selectedCategory;
        const matchesMinPrice = this.minPrice === null || product.price >= this.minPrice;
        const matchesMaxPrice = this.maxPrice === null || product.price <= this.maxPrice;
        return matchesSearchQuery && matchesCategory && matchesMinPrice && matchesMaxPrice;
      });
    }
  },
  mounted() {
    this.fetchProducts();
    this.initializeCart();
  },
  methods: {
    async fetchProducts() {
      try {
        const response = await axios.get(`${process.env.VUE_APP_BACKEND_URL}/product/api/product`);
        this.products = response.data;
      } catch (error) {
        console.error('Error fetching products:', error);
      }
    },
    async initializeCart() {
      const cartId = localStorage.getItem('cartId');
      if (cartId) {
        this.fetchCart(cartId);
      } else {
        try {
          const response = await axios.post(`${process.env.VUE_APP_BACKEND_URL}/shoppingcart/api/shoppingcart`);
          this.cart = response.data;
          localStorage.setItem('cartId', this.cart.id); // Opslaan van de winkelwagen-ID in localStorage
        } catch (error) {
          console.error('Error initializing cart:', error);
        }
      }
    },
    async fetchCart(cartId) {
      try {
        const response = await axios.get(`${process.env.VUE_APP_BACKEND_URL}/shoppingcart/api/shoppingcart/${cartId}`);
        this.cart = response.data;
      } catch (error) {
        console.error('Error fetching cart:', error);
      }
    },
    async addToCart(productId) {
      const quantity = this.quantities[productId] || 1; // Default to 1 if no quantity is specified
      try {
        const response = await axios.post(`${process.env.VUE_APP_BACKEND_URL}/shoppingcart/api/shoppingcart/${this.cart.id}`, {
          productId: productId,
          quantity: quantity
        });
        this.cart = response.data;
      } catch (error) {
        console.error('Error adding to cart:', error);
      }
    },
    async removeFromCart(productId) {
      try {
        const response = await axios.delete(`${process.env.VUE_APP_BACKEND_URL}/shoppingcart/api/shoppingcart/${this.cart.id}`, {
          productId: productId
        });
        this.cart = response.data;
      } catch (error) {
        console.error('Error removing from cart:', error);
      }
    }
  }
};
</script>

<style scoped>
.container {
  width: 50%;
  margin: 0 auto;
}
.search-filter {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}
.search-filter input,
.search-filter select {
  margin-right: 10px;
  padding: 5px;
  flex: 1;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  margin: 10px 0;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
li div {
  margin-bottom: 5px;
}
.shopping-cart {
  margin-top: 20px;
}
</style>