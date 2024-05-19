import { initializeApp } from "https://www.gstatic.com/firebasejs/10.11.1/firebase-app.js";
import { getDatabase, ref, get, set } from "https://www.gstatic.com/firebasejs/10.11.1/firebase-database.js";
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

let idInput = document.getElementById("idAdd");
let bookNameInput = document.getElementById("nameAdd");
let imageUploadInput = document.getElementById("imageUpload");
let imgBook = document.getElementById("book_image");
let unitpriceInput = document.getElementById("unitpriceAdd");
let authorInput = document.getElementById("authorAdd");
let descriptionInput = document.getElementById("descriptionAdd");
let publicationDateInput = document.getElementById("publicationDateAdd");
let ratingInput = document.getElementById("ratingAdd");
let reviewNumInput = document.getElementById("reviewNumAdd");
let categoryInput = document.getElementById("categoryAdd")

function postBook() {
  if (idInput.value.trim() === '' || bookNameInput.value.trim() === '') {
    alert("Please enter a valid id and name");
    return;
  }

  const imageFile = imageUploadInput.files[0];
  if (!imageFile) {
    alert("Please select an image to upload");
    return;
  }
  const filename = `${Date.now()}-${imageFile.name}`;
  const bookRef = ref(db, `books/${idInput.value}`);

  // Check if the ID already exists before upload
  get(bookRef)
    .then((snapshot) => {
      if (snapshot.exists()) {
        alert("This ID already exists. Please choose a different ID.");
        return; // Exit the function if ID exists
      }

      // If ID doesn't exist, proceed with upload and book creation
      uploadBytes(storageRef(storage, `books/${filename}`), imageFile)
        .then((snapshot) => {
          return getDownloadURL(snapshot.ref);
        })
        .then((imageUrl) => {
          set(bookRef, {
            Id: idInput.value,
            imageLink: {0:imageUrl},
            name: bookNameInput.value,
            unitPrice: unitpriceInput.value,
            author: authorInput.value,
            description: descriptionInput.value,
            publicationDate: publicationDateInput.value,
            rating: ratingInput.value,
            reviewNum: reviewNumInput.value,
            category: categoryInput.value,
          })
          .then(() => {
            alert("Book added successfully!");
            window.history.back();
          })
          .catch((error) => {
            alert("Failed to add book. Please check details.");
            console.error(error);
          });
        })
        .catch((error) => {
          alert("Error uploading images or retrieving URLs. Please try again.");
          console.error(error);
        });
    })
    .catch((error) => {
      console.error("Error checking for existing ID:", error);
    });
}

let addBook = document.getElementById("addBook");
addBook.addEventListener('click', postBook);
