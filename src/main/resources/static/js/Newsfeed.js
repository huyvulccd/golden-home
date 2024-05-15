document.addEventListener("DOMContentLoaded", function() {
    const userIcon = document.querySelector('.user-icon');
    const dropdown = document.querySelector('.dropdown-content');

    userIcon.addEventListener('click', function() {
        dropdown.classList.toggle('show');
    });

    window.addEventListener('click', function(e) {
        if (!userIcon.contains(e.target)) {
            dropdown.classList.remove('show');
        }
    });
});
