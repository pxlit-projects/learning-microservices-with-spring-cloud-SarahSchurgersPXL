<template>
  <div class="container">
    <h1>Mijn Winkelwagen</h1>
    <div v-if="cart.items.length === 0">Uw winkelwagen is leeg.</div>
    <ul v-else>
      <li v-for="item in cart.items" :key="item.id">
        <div>
          <strong>{{ item.product.name }}</strong> - €{{ item.product.price }} x {{ item.quantity }}
        </div>
        <button @click="removeFromCart(item.product.id)">Verwijderen</button>
      </li>
    </ul>
    <div v-if="cart.items.length > 0">
      <h3>Totaal: €{{ totalPrice }}</h3>
      <button @click="placeOrder">Bestellen</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      cart: {
        id: null,
        items: []
      }
    };
  },
  computed: {
    totalPrice() {
      return this.cart.items.reduce((total, item) => total + item.product.price * item.quantity, 0).toFixed(2);
    }
  },
  mounted() {
    this.fetchCart();
  },
  methods: {
    async fetchCart() {
      try {
        const cartId = localStorage.getItem('cartId'); // Ophalen van de winkelwagen-ID uit localStorage
        const response = await axios.get(`${process.env.VUE_APP_BACKEND_URL}/shoppingcart/api/shoppingcart/${cartId}`);
        this.cart = response.data;
      } catch (error) {
        console.error('Error fetching cart:', error);
      }
    },
    async removeFromCart(productId) {
      try {
        const response = await axios.delete(`${process.env.VUE_APP_BACKEND_URL}/shoppingcart/api/shoppingcart/${this.cart.id}`, {
          data: { productId: productId }
        });
        this.cart = response.data;
      } catch (error) {
        console.error('Error removing from cart:', error);
      }
    },
    async placeOrder() {
      alert('Bestelling goed ontvangen');
      // Verwijder de winkelwagen-ID uit localStorage
      localStorage.removeItem('cartId');
      // Maak een nieuwe winkelwagen aan
      await this.initializeCart();
    },
    async initializeCart() {
      try {
        const response = await axios.post(`${process.env.VUE_APP_BACKEND_URL}/shoppingcart/api/shoppingcart`);
        this.cart = response.data;
        localStorage.setItem('cartId', this.cart.id); // Opslaan van de nieuwe winkelwagen-ID in localStorage
      } catch (error) {
        console.error('Error initializing cart:', error);
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
</style>