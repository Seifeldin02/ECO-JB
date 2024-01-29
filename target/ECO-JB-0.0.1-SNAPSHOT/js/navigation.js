document.querySelector('.dashboard').addEventListener('click', function(event) {
    if (event.target !== this) {
        return;
    }
    window.location.href = '/dashboard'; /* replace with the actual URL of your dashboard */
});

document.querySelector('.dashboard::after').addEventListener('click', function(event) {
    event.stopPropagation();
    this.parentNode.classList.toggle('active');
});