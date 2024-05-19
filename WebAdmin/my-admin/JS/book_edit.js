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
const dbRef = ref(getDatabase());
const storage = getStorage(app);

var urlParams = new URLSearchParams(window.location.search);
var key = urlParams.get('key');

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

get(child(dbRef, `books/${key}`)).then((snapshot) => {
    if (snapshot.exists()) {
        var book = snapshot.val()
        imgBook.src = book.imageLink
        imgBook.width = 300
        idInput.value = book.Id
        bookNameInput.value = book.name
        unitpriceInput.value = book.unitPrice
        authorInput.value = book.author
        descriptionInput.value = book.description
        publicationDateInput.value = book.publicationDate
        ratingInput.value = book.rating
        reviewNumInput.value = book.reviewNum
        categoryInput.value = book.category
        // console.log(snapshot.val());
    } else {
        console.log("No data available");
    }
}).catch((error) => {
    console.error(error);
});

function deleteBook() {
    var confirmation = confirm("Are you sure want to delete this book?");
    if (confirmation) {
        remove(ref(db, 'books/' + key))
        .then(() => {
            alert("Book is deleted!");
            window.history.back();
        })
        .catch((error) => {
            alert("Delete unsuccessfully1");
            console.log(error);
        });
    }
}

function updateBook() {
    const imageFile = imageUploadInput.files[0];
    const filename = imageFile ? `${Date.now()}-${imageFile.name}` : null;
    const bookRef = ref(db, `books/${idInput.value}`);
  
    const updateData = {
      id: idInput.value,
      name: bookNameInput.value,
      unitPrice: unitpriceInput.value,
      author: authorInput.value,
      description: descriptionInput.value,
      publicationDate: publicationDateInput.value,
      rating: ratingInput.value,
      reviewNum: reviewNumInput.value,
      category: categoryInput.value,

    };
  
    // Check if an image was selected for upload
    if (imageFile) {
      const imageRef = storageRef(storage, `books/${filename}`);
  
      // Upload the image to Firebase Storage
      uploadBytes(imageRef, imageFile)
        .then((snapshot) => {
          return getDownloadURL(snapshot.ref); // Get the image URL after upload
        })
        .then((imageUrl) => {
          updateData.imageLink = imageUrl; // Update imageLink with the new URL
          return update(bookRef, updateData); // Update the book with image
        })
        .then(() => {
          alert("Book updated successfully!");
          window.history.back();
        })
        .catch((error) => {
          alert("Error uploading or updating book. Please check details.");
          console.error(error);
        });
    } else {
      // If no image was selected, update the book without image change
      update(bookRef, updateData)
        .then(() => {
          alert("Book updated successfully!");
          window.history.back();
        })
        .catch((error) => {
          alert("Error updating book. Please check details.");
          console.error(error);
        });
    }
  }

let deleteBtn = document.getElementById("delete")
let updateBtn = document.getElementById("updateBook")
deleteBtn.addEventListener('click', deleteBook)
updateBtn.addEventListener('click', updateBook)