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
const orderRef = ref(db, "orders/");

var urlParams = new URLSearchParams(window.location.search);
var key = urlParams.get('key');

get(child(dbRef, `users/${key}`)).then((snapshot) => {
    if (snapshot.exists()) {
        var user = snapshot.val()
        document.getElementById("cus_id").innerHTML = key
        document.getElementById("cus_image").src = user.profileImage
        document.getElementById("cus_name").innerHTML = user.name
        document.getElementById("cus_gender").innerHTML = user.gender
        document.getElementById("cus_DOB").innerHTML = user.date_of_birth
        document.getElementById("cus_phone").innerHTML = user.phone
        document.getElementById("cus_email").innerHTML = user.email
    } else {
        console.log("No user data available");
    }
}).catch((error) => {
    console.error(error);
});

//Get orders of customer
const dataContainer = document.getElementById("order_history");
onValue(orderRef, (snapshot) => {
    const data = snapshot.val();
    dataContainer.innerHTML = ""; 
    for (var key in data) {
        var order = data[key];
        var userID = order.userID;
        if (userID === urlParams.get('key')) {
            var orderDate = order.orderDate;
            var status = order.status;

            var orderContainer = document.createElement('div');
            orderContainer.classList.add('order_history-item');
            orderContainer.innerHTML = `
                <div class="order_history-title">
                    <div>
                        <h4>Order on ${orderDate}</h4>
                        <span>${status}</span>
                    </div>
                    <button id="visual" onclick="viewOrderDetail('${key}')">
                        <span class="material-icons-outlined">visibility</span>
                    </button>
                </div>
                <table id="order_books_${key}" class="table_list_product"></table>
            `;
            dataContainer.appendChild(orderContainer);

            // Get books in order
            const bookRef = ref(db, 'orders/' + key + '/orderBooks');
            onValue(bookRef, (snapshot) => {
                var tableListProduct = "";
                const booksData = snapshot.val();
                for (var bookKey in booksData) {
                    var book = booksData[bookKey];
                    var imageLink = book.imageLink;
                    var bookName = book.name;
                    var quantity = book.quantity;
                    var unitPrice = book.unitPrice.toLocaleString("vi-VN", {minimumFractionDigits: 0});
                    tableListProduct += `
                        <tr>
                            <td class="img-column">
                                <img class="table_list_product-img" src="${imageLink}">
                            </td>
                            <td class="name-column">${bookName}</td>
                            <td class="price-column">${unitPrice}đ</td>
                            <td class="quantity-column">x${quantity}</td>
                        </tr>
                    `;
                }
                document.getElementById(`order_books_${key}`).innerHTML = tableListProduct;
            });
        }
    }
});