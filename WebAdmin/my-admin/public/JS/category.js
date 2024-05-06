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
const categoryRef = ref(db, "categories/");
const dataContainer = document.querySelector("tbody")

onValue(categoryRef, (snapshot) => {
  const data = snapshot.val();
  var htmlData = "";
  var i = 0;
  for (var key in data) {
    var category = data[key];
    var id = category.id;
    var imageLink = category.imageLink;
    var name = category.name;

    htmlData += `
      <tr>
        <td>${++i}</td>
        <td>${id}</td>
        <td>
            <img src='${imageLink}' width="100">
        </td>
        <td>${name}</td>
        <td>
          <button id="visual" onclick="viewCategoryDetail('${key}')">
            <span class="material-icons-outlined">visibility</span>
          </button>
        </td>
      </tr>
    `;
  }
  dataContainer.innerHTML = htmlData;
})





