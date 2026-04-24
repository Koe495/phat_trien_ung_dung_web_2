const mainMenu = document.getElementById("main-menu");
const gameScreen = document.getElementById("game-screen");
const startBtn = document.getElementById("start-btn");
const backBtn = document.getElementById("back-btn");

startBtn.addEventListener("click", () => {
  mainMenu.classList.remove("active");
  gameScreen.classList.add("active");
});

backBtn.addEventListener("click", () => {
  gameScreen.classList.remove("active");
  mainMenu.classList.add("active");
});
