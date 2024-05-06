import { initializeApp } from "https://www.gstatic.com/firebasejs/10.11.1/firebase-app.js";
import { getDatabase, ref, child, get, set, update, remove, onValue } from "https://www.gstatic.com/firebasejs/10.11.1/firebase-database.js";
import { getStorage, ref as storageRef, uploadBytes, getDownloadURL } from "https://www.gstatic.com/firebasejs/10.11.1/firebase-storage.js";

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
const storage = getStorage(app);

let idInput = document.getElementById("idAdd")
let categoryNameInput = document.getElementById("nameAdd")
let imageUploadInput = document.getElementById("imageUpload");

function postCategory() {
  if (idInput.value.trim() === '' || categoryNameInput.value.trim() === '') {
    alert("Please enter a valid id and name");
    return;
  }

  const imageFile = imageUploadInput.files[0];
  if (!imageFile) {
    alert("Please select an image to upload");
    return;
  }

  const filename = `${Date.now()}-${imageFile.name}`;
  const categoryRef = ref(db, `categories/${idInput.value}`);

  // Check if the ID already exists before upload
  get(categoryRef)
    .then((snapshot) => {
      if (snapshot.exists()) {
        alert("This ID already exists. Please choose a different ID.");
        return; // Exit the function if ID exists
      }
      // If ID doesn't exist, proceed with upload and category creation
      uploadBytes(storageRef(storage, `categories/${filename}`), imageFile)
        .then((snapshot) => {
          return getDownloadURL(snapshot.ref);
        })
        .then((imageUrl) => {
          set(categoryRef, {
            id: idInput.value,
            imageLink: imageUrl,
            name: categoryNameInput.value,
          })
            .then(() => {
              alert("Category added successfully!");
              window.history.back();
            })
            .catch((error) => {
              alert("Failed to add category. Please check details.");
              console.error(error);
            });
        })
        .catch((error) => {
          alert("Error uploading image or retrieving URL. Please try again.");
          console.error(error);
        });
    })
    .catch((error) => {
      console.error("Error checking for existing ID:", error);
    });
}


let addCategory = document.getElementById("addCategory")
addCategory.addEventListener('click', postCategory)