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
const orderRef = ref(db, "orders/");
const dataContainer = document.querySelector("tbody")

onValue(orderRef, (snapshot) => {
    const data = snapshot.val();
    var htmlData = "";
    var count = 0;
    for (var key in data) {
        var order = data[key];
        var status = order.status;
        if (status === "Chờ xác nhận") {
            var name = order.name;
            var orderDate = order.orderDate;
            var total = order.total.toLocaleString("vi-VN", { minimumFractionDigits: 0 }) + "đ";

            htmlData += `
          <tr>
            <td>${++count}</td>
            <td>${key}</td>
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

    var totalOrder = count;
    document.getElementById("totalOrder").innerHTML = "Tổng số lượng đơn hàng: " + totalOrder + " đơn hàng";
    dataContainer.innerHTML = htmlData;
});





