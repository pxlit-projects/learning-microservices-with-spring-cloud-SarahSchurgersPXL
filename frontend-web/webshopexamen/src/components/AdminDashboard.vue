<template>
  <div class="container">
    <h1>Beheer Producten</h1>
    <button @click="goToLogbook">Bekijk Logboek</button>
    <button @click="showAddProductForm = true">Nieuw Product Toevoegen</button>
    <div v-if="showAddProductForm">
      <h2>Nieuw Product</h2>
      <form @submit.prevent="addProduct">
        <div>
          <label for="name">Naam:</label>
          <input type="text" v-model="newProduct.name" id="name" required />
        </div>
        <div>
          <label for="description">Beschrijving:</label>
          <input type="text" v-model="newProduct.description" id="description" required />
        </div>
        <div>
          <label for="price">Prijs:</label>
          <input type="number" v-model.number="newProduct.price" id="price" step="0.01" required />
        </div>
        <div>
          <label for="category">Categorie:</label>
          <select v-model="newProduct.category" id="category" required>
            <option value="">Selecteer een categorie</option>
            <option value="VERZORGING">Verzorging</option>
            <option value="ELEKTRONICA">Elektronica</option>
            <option value="KLEDING">Kleding</option>
            <option value="BOEKEN">Boeken</option>
            <option value="WONING">Woning</option>
            <option value="TUIN">Tuin</option>
            <option value="SPEELGOED">Speelgoed</option>
          </select>
        </div>
        <div>
          <label for="labels">Labels (comma separated):</label>
          <input type="text" v-model="newProduct.labels" id="labels" required />
        </div>
        <button type="submit">Toevoegen</button>
        <button @click="showAddProductForm = false">Annuleren</button>
      </form>
    </div>
    <ul>
      <li v-for="product in products" :key="product.id">
        <div>
          <strong>{{ product.name }}</strong> - €{{ product.price }}
        </div>
        <div>
          {{ product.description }}
        </div>
        <button @click="editProduct(product)">Bewerken</button>
        <button @click="deleteProduct(product.id)">Verwijderen</button>
        <div v-if="showEditProductForm && editProductData.id === product.id">
          <h2>Product Bewerken</h2>
          <form @submit.prevent="updateProduct">
            <div>
              <label for="editName">Naam:</label>
              <input type="text" v-model="editProductData.name" id="editName" required />
            </div>
            <div>
              <label for="editDescription">Beschrijving:</label>
              <input type="text" v-model="editProductData.description" id="editDescription" required />
            </div>
            <div>
              <label for="editPrice">Prijs:</label>
              <input type="number" v-model.number="editProductData.price" id="editPrice" step="0.01" required />
            </div>
            <div>
              <label for="editCategory">Categorie:</label>
              <select v-model="editProductData.category" id="editCategory" required>
                <option value="">Selecteer een categorie</option>
                <option value="VERZORGING">Verzorging</option>
                <option value="ELEKTRONICA">Elektronica</option>
                <option value="KLEDING">Kleding</option>
                <option value="BOEKEN">Boeken</option>
                <option value="WONING">Woning</option>
                <option value="TUIN">Tuin</option>
                <option value="SPEELGOED">Speelgoed</option>
              </select>
            </div>
            <div>
              <label for="editLabels">Labels (comma separated):</label>
              <input type="text" v-model="editProductData.labels" id="editLabels" required />
            </div>
            <button type="submit">Opslaan</button>
            <button @click="showEditProductForm = false">Annuleren</button>
          </form>
        </div>
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
      newProduct: {
        name: '',
        description: '',
        price: 0,
        category: '',
        labels: ''
      },
      editProductData: {},
      showAddProductForm: false,
      showEditProductForm: false
    };
  },
  mounted() {
    this.fetchProducts();
  },
  methods: {
    async fetchProducts() {
      try {
        const response = await axios.get(`${process.env.VUE_APP_BACKEND_URL}/product`);
        this.products = response.data;
      } catch (error) {
        console.error('Error fetching products:', error);
      }
    },
    async addProduct() {
      try {
        const response = await axios.post(`${process.env.VUE_APP_BACKEND_URL}/product`, {
          ...this.newProduct,
          labels: this.newProduct.labels.split(',').map(label => label.trim())
        });
        this.products.push(response.data);
        this.showAddProductForm = false;
        this.resetNewProductForm();
      } catch (error) {
        console.error('Error adding product:', error);
      }
    },
    async editProduct(product) {
      this.editProductData = { ...product, labels: product.labels.join(', ') };
      this.showEditProductForm = true;
    },
    async updateProduct() {
      try {
        await axios.put(`${process.env.VUE_APP_BACKEND_URL}/product/${this.editProductData.id}`, {
          ...this.editProductData,
          labels: this.editProductData.labels.split(',').map(label => label.trim())
        });
        this.showEditProductForm = false;
        this.fetchProducts(); // Haal de lijst van producten opnieuw op
      } catch (error) {
        console.error('Error updating product:', error);
      }
    },
    async deleteProduct(productId) {
      try {
        await axios.delete(`${process.env.VUE_APP_BACKEND_URL}/product/${productId}`);
        this.products = this.products.filter(p => p.id !== productId);
      } catch (error) {
        console.error('Error deleting product:', error);
      }
    },
    resetNewProductForm() {
      this.newProduct = {
        name: '',
        description: '',
        price: 0,
        category: '',
        labels: ''
      };
    },
    goToLogbook() {
      this.$router.push({ name: 'LogbookPage' });
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
form div {
  margin-bottom: 10px;
}
</style>