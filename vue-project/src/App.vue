<template>
  <nav>
    <RouterLink to="/records">Records</RouterLink>
  </nav>
  <main id="mainContent">
    <RouterView />
  </main>
</template>

<style scoped>
#mainContent {
  padding: 0.75rem;
}
</style>