function login() {
    // Lấy giá trị từ ô nhập tên đăng nhập và mật khẩu
    var username = document.getElementById('username').value;
    var password = document.getElementById('password').value;

    // Kiểm tra tên đăng nhập và mật khẩu
    if (username === 'admin' && password === '123') {
        // Đăng nhập thành công, chuyển hướng sang trang index_navigate_bar.html
        window.location.href = '../index_navigate_bar.html';
    } else {
        // Đăng nhập thất bại, hiển thị thông báo lỗi
        alert('Tên đăng nhập hoặc mật khẩu không đúng');
    }
}