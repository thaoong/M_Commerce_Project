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
const db = getDatabase();
const userRef = ref(db, "users/")
const orderRef = ref(db, "orders/");
const dataContainer = document.querySelector("tbody")

// Get total customer
onValue(userRef, (snapshot) => {
    const data = snapshot.val();

    var totalCustomer = Object.keys(data).length;
    document.getElementById("totalCustomers").innerHTML = totalCustomer;
})

// Get total order and total sales
onValue(orderRef, (snapshot) => {
    const data = snapshot.val();

    var totalOrder = Object.keys(data).length;
    document.getElementById("totalOrders").innerHTML = totalOrder;

    var totalSales = 0; 
    for (var key in data) {
        var order = data[key];
        var total = order.total;
        totalSales += total;
    }
    document.getElementById("totalSales").innerHTML = totalSales.toLocaleString("vi-VN", {minimumFractionDigits: 0}) + "đ";
})

// Get recent order
onValue(orderRef, (snapshot) => {
    const data = snapshot.val();
    var htmlData = "";
    var i = 0;
    var today = new Date();
    var yesterday = new Date(today);
    yesterday.setDate(today.getDate() - 1);
    var todayString = formatDate(today);
    var yesterdayString = formatDate(yesterday);

    for (var key in data) {
        var order = data[key];
        var orderDate = order.orderDate;
        if (orderDate === todayString || orderDate === yesterdayString) {
            var name = order.name;
            var total = order.total.toLocaleString("vi-VN", {minimumFractionDigits: 0}) + "đ";
            var status = order.status;
            htmlData += `
                <tr>
                    <td>${++i}</td>
                    <td>${name}</td>
                    <td>${orderDate}</td>
                    <td>${total}</td>
                    <td>${status}</td>
                    <td>
                        <button id="visual" onclick="viewOrderDetail('${key}')">
                            <span class="material-icons-outlined">visibility</span>
                        </button>
                    </td>
                </tr>
            `;
        }
    }
    dataContainer.innerHTML = htmlData;
})

function formatDate(date) {
    var day = String(date.getDate()).padStart(2, '0');
    var month = String(date.getMonth() + 1).padStart(2, '0');
    var year = date.getFullYear();
    return `${day}/${month}/${year}`;
}
