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

        // Get orders of customers 
        const ordersRef = ref(db, 'orders');
        onValue(ordersRef, (snapshot) => {
            const ordersData = snapshot.val();
            if (ordersData) {
                const orders = Object.values(ordersData);
                orders.forEach(order => {
                    if (order.userID === key) {
                        const orderBooks = order.orderBooks;
                        const orderDate = order.orderDate;
                        const status = order.status;
                        const name = order.name;
                        const unitPrice = order.unitPrice;
                        const quantity = order.quantity;
                    }
                });
            }
        });
    } else {
        console.log("No user data available");
    }
}).catch((error) => {
    console.error(error);
});
