import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from "tailwindcss";
import tailwindcssConfig from "./tailwind.config.js"
import postcss from "postcss";
import postcssConfig from "./postcss.config.js";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    react(),
    tailwindcssConfig,
    postcssConfig,
  ],
})
