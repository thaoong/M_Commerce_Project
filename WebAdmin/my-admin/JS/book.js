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
const bookRef = ref(db, "books/");
const dataContainer = document.querySelector("tbody")

onValue(bookRef, (snapshot) => {
  const data = snapshot.val();
  var htmlData = "";
  var i = 0;
  for (var key in data) {
    var book = data[key];
    var id = book.Id;
    var imageLink = book.imageLink;
    var name = book.name;
    var unitprice = book.unitPrice.toLocaleString("vi-VN", {minimumFractionDigits: 0})+"đ";;
    var category = book.category;

    htmlData += `
      <tr>
        <td>${++i}</td>
        <td>${id}</td>
        <td>${name}</td>
        <td>${category}</td>
        <td>
            <img src='${imageLink[0]}' width="100">
        </td>
        <td>${unitprice}</td>
        <td>
          <button id="visual" onclick="viewBookDetail('${key}')">
            <span class="material-icons-outlined">visibility</span>
          </button>
        </td>
      </tr>
    `;
  }
  dataContainer.innerHTML = htmlData;
})





