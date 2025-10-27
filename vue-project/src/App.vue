<template>
    <main class="App-main-content">
      <RouterView />
    </main>
</template>

<style>
h1 {
    font-size: 26pt;
    text-align: center;
    margin-bottom: 1rem;
}
h2 {
    font-size: 22pt;
}
.App-main-content {
    padding: 0.5rem;
}
a, p, label, input, select, option {
    font-size: 18pt;
}
.btn-primary {
    background-color: #050;
}
.btn-primary:hover {
    background-color: #090;
}
.btn-link {
    text-decoration: none;
    background-color: #999;
    color: black;
}
.btn-link:hover {
    background-color: #ccc;
}
input[type="text"], input[type="password"] {
    width: 100%;
}
/*
* {
    box-sizing: border-box;
    font-family: 'Courier New';
}
a, p, button, input, select {
    font-size: 14pt;
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
*/
</style>