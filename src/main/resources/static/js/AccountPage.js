$(document).ready(function() {
    const container = $('#container');
    const registerBtn = $('#register');
    const loginBtn = $('#login');
    const toggleContainer = $('.toggle-container').eq(0);
    var isSignInPage = true;

    registerBtn.on('click', function() {
        container.addClass("active");
        document.title = "SIGN UP | project-name";
        isSignInPage = false;
        handleResponsive();
    });

    loginBtn.on('click', function() {
        container.removeClass("active");
        document.title = "SIGN IN | project-name";
        isSignInPage = true;
        handleResponsive();
    });

    $(window).on('resize load', handleResponsive);

    function handleResponsive() {
        var windowWidth = $(window).innerWidth();
        if (windowWidth < 572) {
            if (isSignInPage) {
                toggleContainer.css('left', '85%');
            } else {
                toggleContainer.css('left', '15%');
            }
        } else {
            toggleContainer.css('left', '50%');
        }
    }

     $('#submitBtn').click(function(event){
            event.preventDefault();

            // Lấy giá trị từ các input
            var username = $('#username').val();
            var password = $('#password').val();
            var remember = $('#remember-signup').is(":checked");

            // Gom dữ liệu thành một đối tượng JSON
            var data = {
                "username": username,
                "password": password,
                "remember": remember
            };

            // Gửi request AJAX
            $.ajax({
                url: '/login',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: function(response) {
                    // Xử lý kết quả trả về nếu cần
                    console.log('Request thành công');
                },
                error: function(xhr, status, error) {
                    // Xử lý lỗi nếu có
                    console.error('Lỗi trong quá trình gửi request:', error);
                }
            });
        });
});

