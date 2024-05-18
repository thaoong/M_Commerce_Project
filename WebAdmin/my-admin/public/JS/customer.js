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
const userRef = ref(db, "users/");
const dataContainer = document.querySelector("tbody")

onValue(userRef, (snapshot) => {
  const data = snapshot.val();
  var htmlData = "";
  var i = 0;
  var totalUser = Object.keys(data).length;
  document.getElementById("totalCustomer").innerHTML = "Total custocustomer: " + totalUser + " customers"
  for (var key in data) {
    var user = data[key];
    var name = user.name;
    var email = user.email;
    var phone = user.phone;

    htmlData += `
      <tr>
        <td>${++i}</td>
        <td>${key}</td>
        <td>${name}</td>
        <td>${email}</td>
        <td>${phone}</td>
        <td>
          <button id="visual" onclick="viewCustomerDetail('${key}')">
            <span class="material-icons-outlined">visibility</span>
          </button>
        </td>
      </tr>
    `;
  }
  dataContainer.innerHTML = htmlData;
})





