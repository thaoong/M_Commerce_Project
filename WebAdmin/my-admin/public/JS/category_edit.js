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

let idInput = document.getElementById("idAdd")
let categoryNameInput = document.getElementById("nameAdd")
let imageUploadInput = document.getElementById("imageUpload");
let imgCategory = document.getElementById("category_image")

get(child(dbRef, `categories/${key}`)).then((snapshot) => {
    if (snapshot.exists()) {
        var category = snapshot.val()
        imgCategory.src = category.imageLink
        imgCategory.width = 300
        idInput.value = category.id
        categoryNameInput.value = category.name
        // console.log(snapshot.val());
    } else {
        console.log("No data available");
    }
}).catch((error) => {
    console.error(error);
});

function deleteCategory() {
    var confirmation = confirm("Are you sure want to delete this category?");
    if (confirmation) {
        remove(ref(db, 'categories/' + key))
        .then(() => {
            alert("Category is deleted!");
            window.history.back();
        })
        .catch((error) => {
            alert("Delete unsuccessfully1");
            console.log(error);
        });
    }
}

function updateCategory() {
    const imageFile = imageUploadInput.files[0];
    const filename = imageFile ? `${Date.now()}-${imageFile.name}` : null;
    const categoryRef = ref(db, `categories/${idInput.value}`);
  
    const updateData = {
      id: idInput.value,
      name: categoryNameInput.value,
    };
  
    // Check if an image was selected for upload
    if (imageFile) {
      const imageRef = storageRef(storage, `categories/${filename}`);
  
      // Upload the image to Firebase Storage
      uploadBytes(imageRef, imageFile)
        .then((snapshot) => {
          return getDownloadURL(snapshot.ref); // Get the image URL after upload
        })
        .then((imageUrl) => {
          updateData.imageLink = imageUrl; // Update imageLink with the new URL
          return update(categoryRef, updateData); // Update the category with image
        })
        .then(() => {
          alert("Category updated successfully!");
          window.history.back();
        })
        .catch((error) => {
          alert("Error uploading or updating category. Please check details.");
          console.error(error);
        });
    } else {
      // If no image was selected, update the category without image change
      update(categoryRef, updateData)
        .then(() => {
          alert("Category updated successfully!");
          window.history.back();
        })
        .catch((error) => {
          alert("Error updating category. Please check details.");
          console.error(error);
        });
    }
  }

let deleteBtn = document.getElementById("delete")
let updateBtn = document.getElementById("updateCategory")
deleteBtn.addEventListener('click', deleteCategory)
updateBtn.addEventListener('click', updateCategory)