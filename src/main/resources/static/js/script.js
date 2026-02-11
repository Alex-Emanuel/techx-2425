// QUOTES SLIDER
// =============

const quotes = document.querySelectorAll('.quote-carousel .quote');
let currentIndex = 0;

setInterval(() => {
	const current = quotes[currentIndex];
    const nextIndex = (currentIndex + 1) % quotes.length;
    const next = quotes[nextIndex];

    quotes.forEach(quote => quote.classList.remove('active', 'prev'));

    current.classList.add('prev');
    next.classList.add('active');

    currentIndex = nextIndex;
}, 5000);