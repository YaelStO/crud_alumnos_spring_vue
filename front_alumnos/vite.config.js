import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      // Redirige las llamadas de API al backend de Spring Boot durante el desarrollo
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      }
    }
  },
  build: {
    // Asegura que el build se genere en la carpeta 'dist' que Maven copiará
    outDir: 'dist',
    emptyOutDir: true,
  }
})