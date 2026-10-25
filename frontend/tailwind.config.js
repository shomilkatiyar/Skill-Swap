/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      colors: {
        brand: {
          50: '#f2f7f5',
          100: '#dcece5',
          200: '#b8d9cc',
          300: '#8ac0ac',
          400: '#5aa088',
          500: '#3d8570',
          600: '#2e6b5a',
          700: '#27564a',
          800: '#22453c',
          900: '#1e3a33',
        },
      },
    },
  },
  plugins: [],
}
