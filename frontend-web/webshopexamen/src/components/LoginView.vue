<template>
  <div class="login">
    <h1>Login</h1>
    <form @submit.prevent="login">
      <div>
        <label for="username">Username:</label>
        <input type="text" v-model="username" id="username" required />
      </div>
      <div>
        <label for="password">Password:</label>
        <input type="password" v-model="password" id="password" required />
      </div>
      <button type="submit">Login</button>
    </form>
    <p v-if="errorMessage">{{ errorMessage }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      password: '',
      errorMessage: ''
    };
  },
  methods: {
    login() {
      if (this.username === 'user' && this.password === 'user') {
        this.$router.push({ name: 'Shopping' });
      } else if (this.username === 'admin' && this.password === 'admin') {
        this.$router.push({ name: 'AdminDashboard' });
      } else {
        this.errorMessage = 'Invalid username or password';
      }
    }
  }
};
</script>

<style scoped>
.login {
  max-width: 300px;
  margin: 0 auto;
  padding: 1em;
  border: 1px solid #ccc;
  border-radius: 5px;
}
.login h1 {
  text-align: center;
}
.login div {
  margin-bottom: 1em;
}
.login label {
  display: block;
  margin-bottom: 0.5em;
}
.login input {
  width: 100%;
  padding: 0.5em;
  box-sizing: border-box;
}
.login button {
  width: 100%;
  padding: 0.5em;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
.login button:hover {
  background-color: #0056b3;
}
.login p {
  color: red;
  text-align: center;
}
</style>