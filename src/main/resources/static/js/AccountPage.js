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
    $('.sign-in #submitBtn').click(function(event){
       event.preventDefault();

       // Lấy giá trị từ các input
       var username = $('.sign-in #username').val();
       var password = $('.sign-in #password').val();
       var remember = $('.sign-in #remember-signup').is(":checked");

       // Gom dữ liệu thành một đối tượng JSON
       var data = {
           "username": username,
           "password": password,
           "remember": remember
       };

       // Gửi request AJAX
       $.ajax({
           url: '/auth-token',
           type: 'POST',
           contentType: 'application/json',
           data: JSON.stringify(data),
           dataType : 'text',
           success: function(response) {
               // Xử lý kết quả trả về nếu cần
               console.log('Request thành công: token');
           },
           error: function(xhr, status, error) {
               // Xử lý lỗi nếu có
               console.log('Lỗi trong quá trình gửi request:', error);
           },
           complete: function(response) {
               // Xử lý lỗi nếu có
           }

       });
   });
   $('.sign-up #submitBtn').click(function(event){
     event.preventDefault(); // Chặn xử lý submit form

     // Lấy giá trị từ các input
     var username = $('.sign-up #username').val();
     var password = $('.sign-up #password').val();
     var email = $('.sign-up #email').val();

     // Gom dữ liệu thành một đối tượng JSON
     var data = {
         "username": username,
         "password": password,
         "email": email
     };

     // Gửi request AJAX
     $.ajax({
         url: '/registration',
         type: 'POST',
         contentType: 'application/json',
         data: JSON.stringify(data),
         dataType: 'text',
         success: function(response) {
             // Xử lý kết quả trả về nếu cần
             container.removeClass("active");
             document.title = "SIGN IN | project-name";
             isSignInPage = true;
             handleResponsive();
             console.log(response);
         },
         error: function(xhr, status, error) {
             // Xử lý lỗi nếu có
             console.log('Lỗi trong quá trình gửi request:', error);
         },
         complete: function(response) { // Sự kiện hoàn tất
             // Thực hiện bất kỳ hành động nào cần thiết sau khi request hoàn tất
             console.log('Request hoàn tất');
         }
     });
   });

});

