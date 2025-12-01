const card = document.getElementById('card');
let startX = 0;
let currentX = 0;

card.addEventListener('touchstart', e => {
  startX = e.touches[0].clientX;
});

card.addEventListener('touchmove', e => {
  currentX = e.touches[0].clientX;
  const diff = currentX - startX;
  if (Math.abs(diff) < 150) {
    card.style.transform = `translateX(${diff}px)`;
  }
});

card.addEventListener('touchend', e => {
  const diff = currentX - startX;
  if (Math.abs(diff) > 80) {
    const isRight = diff > 0;
    Android.onCardSwiped(isRight);

    card.style.transition = 'transform 0.4s ease, opacity 0.3s ease';
    card.style.transform = `translateX(${isRight ? 500 : -500}px)`;
    card.style.opacity = '0';

    setTimeout(() => {
      card.style.transition = 'none';
      card.style.transform = 'translateX(0)';
      card.style.opacity = '1';
      document.getElementById('card-text').textContent = "Nova carta carregada!";
    }, 400);
  } else {
    card.style.transition = 'transform 0.3s ease';
    card.style.transform = 'translateX(0)';
  }
});