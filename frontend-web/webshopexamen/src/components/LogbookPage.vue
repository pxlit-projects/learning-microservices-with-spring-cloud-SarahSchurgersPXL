<template>
  <div class="container">
    <h1>Logboek</h1>
    <ul>
      <li v-for="log in logs" :key="log.id">
        <div>
          <strong>Actie:</strong> {{ log.message }}
        </div>
        <div>
          <strong>Gebruiker:</strong> {{ log.user }}
        </div>
        <div>
          <strong>Product ID:</strong> {{ log.productId }}
        </div>
        <div>
          <strong>Tijdstip:</strong> {{ log.timestamp }}
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
      logs: []
    };
  },
  mounted() {
    this.fetchLogs();
  },
  methods: {
    async fetchLogs() {
      try {
        const response = await axios.get(`${process.env.VUE_APP_BACKEND_URL}/logbook/logs`);
        this.logs = response.data;
      } catch (error) {
        console.error('Error fetching logs:', error);
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