document.querySelector('.dashboard').addEventListener('click', function(event) {
    // Calculate the click position within the element
    var clickPosition = event.clientX - this.getBoundingClientRect().left;

    // If the click is on the right side of the element (where the arrow is)
    if (clickPosition > this.offsetWidth - 30) { // 30px is roughly the size of the arrow
        event.stopPropagation();
        this.classList.toggle('active');
    } else {
    	window.location.href = '/ECO-JB/user/dashboard'; 
    }
});

document.getElementById('toggle-sidebar').addEventListener('click', function() {
    var sidebar = document.getElementById('sidebar');
    sidebar.classList.toggle('retracted');
    // Save the state in localStorage
    localStorage.setItem('sidebarState', sidebar.classList.contains('retracted') ? 'retracted' : 'expanded');
});

// On page load, restore the state from localStorage
window.addEventListener('DOMContentLoaded', (event) => {
    var sidebar = document.getElementById('sidebar');
    var sidebarState = localStorage.getItem('sidebarState');
    if (sidebarState === 'retracted') {
        sidebar.classList.add('retracted');
    } else {
        sidebar.classList.remove('retracted');
    }
});