/** @type {import('tailwindcss').Config} */
module.exports = {
  purge: ['./src/**/*.html', './src/**/*.{js, jsx, ts, tsx}'],
  content: ['./src/**/*.{html,js}'],
  theme: {
    extend: {
      backgroundImage: {
        'logo': 'url("./img/logo.png")',
        'cardboard': 'linear-gradient(to right bottom, rgb(191, 250, 172), rgb(103, 168, 229)), ' +
          'url("./img/cardboard.png")',
      },
      fontFamily: {
        sans: ['Graphik', 'sans-serif'],
        serif: ['Merriweather', 'serif'],
      },
      extend: {
        spacing: {
          '8xl': '96rem',
          '9xl': '128rem',
        },
        borderRadius: {
          '4xl': '2rem',
        }
      }
    },
  },
  plugins: [],
};
