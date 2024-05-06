import { initializeApp } from "https://www.gstatic.com/firebasejs/10.11.1/firebase-app.js";
import { getDatabase, ref, onValue } from "https://www.gstatic.com/firebasejs/10.11.1/firebase-database.js";

const firebaseConfig = {
    apiKey: "AIzaSyALP0s0RbtbJ2glwArGQ1s_yQ9kwhGicpI",
    authDomain: "thestore-55f0f.firebaseapp.com",
    databaseURL: "https://thestore-55f0f-default-rtdb.firebaseio.com",
    projectId: "thestore-55f0f",
    storageBucket: "thestore-55f0f.appspot.com",
    messagingSenderId: "318647012345",
    appId: "1:318647012345:web:fc1ceaf122b8376eaa8b28",
    measurementId: "G-1XNCYRDHHN",
};

const app = initializeApp(firebaseConfig);
// const auth = getAuth();
const db = getDatabase();
const adminRef = ref(db, "admin/");
document.getElementById("loginForm").addEventListener("submit", function (event) {
    event.preventDefault(); // Ngăn chặn việc tải lại trang

    // Lấy giá trị từ trường "admin" và "password"
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    // Lắng nghe sự thay đổi dữ liệu và cập nhật giá trị của ô input
    onValue(adminRef, (snapshot) => {
        const data = snapshot.val();
        for (const key in data) {
            const admin = data[key];
            console.log(admin)
            if (admin.username === username && admin.password === password) {
                // Xác thực thành công, chuyển hướng đến trang mới
                window.location.href = "navigate_bar.html";
            } else {
                // Xác thực không thành công, hiển thị thông báo lỗi
                document.getElementById("error-msg").textContent = "Username or password is incorrect!";
            }
        }
    })
});