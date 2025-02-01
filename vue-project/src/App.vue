<template>
  <main class="App-main-content">
    <RouterView />
  </main>
</template>

<style>
.App-main-content {
  padding: 0.75rem;
}
* {
  box-sizing: border-box;
  font-family: 'Courier New';
}
a, p, button, input, select {
  font-size: 12pt;
}
h1 {
  font-size: 18pt;
}
h2 {
  font-size: 16pt;
}
button:hover, a:hover {
  cursor: pointer;
}
a:hover {
  text-decoration: underline;
}
</style>