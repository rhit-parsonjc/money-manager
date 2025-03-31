<template>
  <main class="App-main-content">
    <RouterView />
  </main>
</template>

<style>
.App-main-content {
  padding: 0.5rem;
}
* {
  box-sizing: border-box;
  font-family: 'Courier New';
}
a, p, button, input, select {
  font-size: 14pt;
}
h1 {
  font-size: 20pt;
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