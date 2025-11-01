<template>
    <main class="App-main-content">
      <RouterView />
    </main>
</template>

<style>
h1 {
    font-size: 26pt;
}
h2 {
    font-size: 22pt;
}
a, p, label, input, select, option {
    font-size: 18pt;
}
.btn, input, select {
    width: 100%;
}
.btn-primary {
    background-color: #050;
}
.btn-primary:hover {
    background-color: #090;
}
.btn-primary:active {
    background-color: #0c0 !important;
}
.btn-link {
    text-decoration: none;
    background-color: #999;
    color: black;
}
.btn-link:hover {
    background-color: #ccc;
}
.btn-link:active {
    background-color: #eee !important;
}
a:hover {
    cursor: pointer;
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