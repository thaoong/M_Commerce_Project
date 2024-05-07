import { initializeApp } from "https://www.gstatic.com/firebasejs/10.11.1/firebase-app.js";
import { getDatabase, ref, child, get, set, update, remove, onValue } from "https://www.gstatic.com/firebasejs/10.11.1/firebase-database.js";

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
const db = getDatabase();
const dbRef = ref(getDatabase());

var urlParams = new URLSearchParams(window.location.search);
var key = urlParams.get('key');

get(child(dbRef, `orders/${key}`)).then((snapshot) => {
    if (snapshot.exists()) {
        var order = snapshot.val()
        document.getElementById("ord_id").innerHTML = key
        document.getElementById("order_date").innerHTML = order.orderDate
        document.getElementById("payment_method").innerHTML = order.paymentMethod

        // Get books in order
        const bookRef = ref(db, 'orders/' + key + '/orderBooks');
        onValue(bookRef, (snapshot) => {
            const data = snapshot.val();
            console.log(data)
            var i = 0;
            for (var key in data) {
                var book = data[key];
                var imageLink = book.imageLink;
                var bookName = book.name;
                var quantity = book.quantity;
                var unitPrice = book.unitPrice;

                var tbody = document.getElementById("tbody1")
                var trow = document.createElement("tr");
                var td1 = document.createElement("td");
                var td2 = document.createElement("td");
                var td3 = document.createElement("td");
                var td4 = document.createElement("td");
                var td5 = document.createElement("td");
                var td6 = document.createElement("td");

                td1.innerHTML = ++i;

                var img = document.createElement("img");
                img.src = imageLink;
                img.width = 100;
                td2.appendChild(img);
                td3.innerHTML = bookName;
                td4.innerHTML = quantity;
                td5.innerHTML = unitPrice.toLocaleString("vi-VN", {minimumFractionDigits: 0})+"đ";
                td6.innerHTML = (quantity * unitPrice).toLocaleString("vi-VN", {minimumFractionDigits: 0})+"đ";

                trow.appendChild(td1);
                trow.appendChild(td2);
                trow.appendChild(td3);
                trow.appendChild(td4);
                trow.appendChild(td5);
                trow.appendChild(td6);
                tbody.appendChild(trow)
            }
        })

        document.getElementById("pre_price").innerHTML = order.prePrice.toLocaleString("vi-VN", {minimumFractionDigits: 0})+"đ"
        document.getElementById("shipping_fee").innerHTML = order.shippingFee.toLocaleString("vi-VN", {minimumFractionDigits: 0})+"đ"
        document.getElementById("discount").innerHTML = order.discount.toLocaleString("vi-VN", {minimumFractionDigits: 0})+"đ"
        document.getElementById("total").innerHTML = order.total.toLocaleString("vi-VN", {minimumFractionDigits: 0})+"đ"
        document.getElementById("customer_name").innerHTML = "Khách hàng: " + order.name
        document.getElementById("phone_number").innerHTML = "SĐT: " + order.phone
        document.getElementById("address").innerHTML = "Địa chỉ: " + order.street + ", " + order.ward + ", " + order.district + ", " + order.province

        // console.log(snapshot.val());
    } else {
        console.log("No data available");
    }
}).catch((error) => {
    console.error(error);
});

function deleteOrder() {
    remove(ref(db, 'orders/' + key))
    .then(()=>{
        alert("Delete order successfully")
        window.history.back();
    })
    .catch((error)=>{
        alert("Unsuccessful")
        console.log(error)
    })
}

function updateDelivery() {
    const orderStatusRef = ref(db, `orders/${key}/status`);
    set(orderStatusRef, "Đang vận chuyển")
        .then(() => {
            alert("Update order status successfully");
            window.history.back();
        })
        .catch((error) => {
            console.error(error);
        });
}

function cancelOrder() {
    const orderStatusRef = ref(db, `orders/${key}/status`);
    set(orderStatusRef, "Đã hủy")
        .then(() => {
            alert("Update order status successfully");
            window.history.back();
        })
        .catch((error) => {
            console.error(error);
        });
}

function updateComplete() {
    const orderStatusRef = ref(db, `orders/${key}/status`);
    set(orderStatusRef, "Hoàn tất")
        .then(() => {
            alert("Update order status successfully");
            window.history.back();
        })
        .catch((error) => {
            console.error(error);
        });
}

let approveBtn = document.getElementById("approve")
let deliveryBtn = document.getElementById("deli")
let cancelBtn = document.getElementById("cancel")
deliveryBtn.addEventListener('click', updateDelivery)
cancelBtn.addEventListener('click', cancelOrder)
approveBtn.addEventListener('click', updateComplete)
