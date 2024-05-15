document.addEventListener("DOMContentLoaded", function() {
        const token = localStorage.getItem("token");
        if (token) {
            fetch('/newsfeed', {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token
                }
            })
            .then(response => {
                if (response.ok) {
                    return response.text();
                } else {
                    throw new Error('Unauthorized');
                }
            })
            .then(html => {
                document.body.innerHTML = html;
            })
            .catch(error => {
                console.error('Error:', error);
                window.location.href = '/';
            });
        } else {
            window.location.href = '/';
        }
    });

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
