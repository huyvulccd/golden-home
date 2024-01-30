$(document).ready(function() {
    $('.about-us').click(function() {
        $('form').hide();
        $('#form').css('height', "70%");
        $('.product-description').show();
        $('.about-us').addClass('focus');
        $('.tit-form').removeClass('focus');
    })
    $('.tit-form').click(function() {
        $('.product-description').hide();
        $('form').show();
        $('#form').css('height', "50%");
        $('.tit-form').addClass('focus');
        $('.about-us').removeClass('focus');
    })
})