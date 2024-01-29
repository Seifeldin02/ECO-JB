document.querySelector('.dashboard').addEventListener('click', function(event) {
    // Calculate the click position within the element
    var clickPosition = event.clientX - this.getBoundingClientRect().left;

    // If the click is on the right side of the element (where the arrow is)
    if (clickPosition > this.offsetWidth - 30) { // 30px is roughly the size of the arrow
        event.stopPropagation();
        this.classList.toggle('active');
    } else {
        window.location.href = '/dashboard'; /* replace with the actual URL of your dashboard */
    }
});

document.getElementById('toggle-sidebar').addEventListener('click', function() {
    document.getElementById('sidebar').classList.toggle('retracted');
});