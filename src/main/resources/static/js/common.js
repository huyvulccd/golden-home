function redirectTo(url) {
    if (url) {
        window.location.href = url;
    } else {
        console.error("URL không được cung cấp.");
    }
}