/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      colors: {
        ink: '#151217',
        'ink-deep': '#0b0a0d',
        parchment: '#f4ede1',
        rust: '#b3452c',
        brass: '#c9a04a',
        'brass-light': '#e6c877',
        blood: '#7a1d16',
      },
      fontFamily: {
        display: ['"Playfair Display"', 'Georgia', 'Cambria', 'serif'],
        sans: ['Inter', 'system-ui', 'sans-serif'],
        mono: ['"Courier Prime"', 'ui-monospace', 'SFMono-Regular', 'monospace'],
      },
      boxShadow: {
        'glow-brass': '0 0 0 1px rgba(201,160,74,0.25), 0 8px 30px -8px rgba(201,160,74,0.35)',
        'glow-rust': '0 0 0 1px rgba(179,69,44,0.3), 0 8px 30px -8px rgba(179,69,44,0.45)',
        card: '0 4px 24px -6px rgba(0,0,0,0.55)',
      },
      backgroundImage: {
        'radial-fade': 'radial-gradient(ellipse at 50% -10%, rgba(201,160,74,0.14), transparent 60%)',
        'noir-grain': "url(\"data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='160' height='160'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='2' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)' opacity='0.05'/%3E%3C/svg%3E\")",
        'photo-evidence': "url('/login_bg.jpeg')",
        'photo-topics': "url('/topics_bg.jpeg')",
        'photo-schema': "url('/schemaDiagram.jpeg')",
        'photo-faqs': "url('/faqs_bg.jpeg')",
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: 0, transform: 'translateY(8px)' },
          '100%': { opacity: 1, transform: 'translateY(0)' },
        },
        drift: {
          '0%': { transform: 'translate(0,0)' },
          '50%': { transform: 'translate(6px,-10px)' },
          '100%': { transform: 'translate(0,0)' },
        },
        flicker: {
          '0%, 100%': { opacity: 1 },
          '92%': { opacity: 1 },
          '93%': { opacity: 0.6 },
          '94%': { opacity: 1 },
          '96%': { opacity: 0.75 },
          '97%': { opacity: 1 },
        },
      },
      animation: {
        fadeIn: 'fadeIn 0.5s ease-out both',
        drift: 'drift 9s ease-in-out infinite',
        flicker: 'flicker 6s linear infinite',
      },
    },
  },
  plugins: [],
}
