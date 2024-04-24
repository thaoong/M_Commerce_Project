import { initializeApp } from "https://www.gstatic.com/firebasejs/10.11.0/firebase-app.js";
import { getAnalytics } from "https://www.gstatic.com/firebasejs/10.11.0/firebase-analytics.js";
import { getDatabase, ref, set, get, update, remove } from "https://www.gstatic.com/firebasejs/10.11.0/firebase-database.js";

const firebaseConfig = {
    // Your Firebase configuration
};

const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);
const db = getDatabase();

const idInput = document.getElementById('idInput');
const authorInput = document.getElementById('authorInput');
const bestSellingInput = document.getElementById('bestSellingInput');
const categoryInput = document.getElementById('categoryInput');
const descriptionInput = document.getElementById('descriptionInput');
const imageLinkInput = document.getElementById('imageLinkInput');
const nameInput = document.getElementById('nameInput');
const publicationDateInput = document.getElementById('publicationDateInput');
const ratingInput = document.getElementById('ratingInput');
const reviewNumInput = document.getElementById('reviewNumInput');
const unitPriceInput = document.getElementById('unitPriceInput');

const btnAdd = document.getElementById("Btn_Add");
const btnRet = document.getElementById("Btn_Ret");
const btnEdit = document.getElementById("Btn_Edit");
const btnDelete = document.getElementById("Btn_Delete");

// Add Book
btnAdd.addEventListener('click', addData);

// Retrieve Book
btnRet.addEventListener('click', retData);

// Edit Book
btnEdit.addEventListener('click', updateData);

// Delete Book
btnDelete.addEventListener('click', deleteData);

function addData(event) {
    event.preventDefault();
    if (!idInput.value || !authorInput.value || !nameInput.value) {
        alert("Please fill in all required fields.");
        return;
    }
    set(ref(db, 'Book/' + idInput.value), {
        id: idInput.value,
        author: authorInput.value,
        bestSelling: bestSellingInput.value || "",
        category: categoryInput.value || "",
        description: descriptionInput.value || "",
        imageLink: imageLinkInput.value || "",
        name: nameInput.value,
        publicationDate: publicationDateInput.value || "",
        rating: ratingInput.value || "",
        reviewNum: reviewNumInput.value || "",
        unitPrice: unitPriceInput.value || ""
    }).then(() => {
        alert("Data Added Successfully");
        clearInputs();
    }).catch((error) =>{
        alert("Failed to add data: " + error.message);
        console.error(error);
    });
}

function retData(event) {
    event.preventDefault();
    const bookId = idInput.value;
    if (!bookId) {
        alert("Please enter book ID to retrieve.");
        return;
    }
    const dbRef = ref(db, 'Book/' + bookId);
    get(dbRef).then((snapshot) => {
        if (snapshot.exists()) {
            const data = snapshot.val();
            authorInput.value = data.author;
            bestSellingInput.value = data.bestSelling || "";
            categoryInput.value = data.category || "";
            descriptionInput.value = data.description || "";
            imageLinkInput.value = data.imageLink || "";
            nameInput.value = data.name;
            publicationDateInput.value = data.publicationDate || "";
            ratingInput.value = data.rating || "";
            reviewNumInput.value = data.reviewNum || "";
            unitPriceInput.value = data.unitPrice || "";
        } else {
            alert("Book does not exist");
            clearInputs();
        }
    }).catch((error) =>{
        alert("Failed to retrieve data: " + error.message);
        console.error(error);
    });
}

function clearInputs() {
    idInput.value = "";
    authorInput.value = "";
    bestSellingInput.value = "";
    categoryInput.value = "";
    descriptionInput.value = "";
    imageLinkInput.value = "";
    nameInput.value = "";
    publicationDateInput.value = "";
    ratingInput.value = "";
    reviewNumInput.value = "";
    unitPriceInput.value = "";
}

function updateData(event) {
    event.preventDefault();
    const bookId = idInput.value;
    if (!bookId) {
        alert("Please enter book ID to update.");
        return;
    }
    update(ref(db, 'Book/' + bookId), {
        author: authorInput.value,
        bestSelling: bestSellingInput.value || "",
        category: categoryInput.value || "",
        description: descriptionInput.value || "",
        imageLink: imageLinkInput.value || "",
        name: nameInput.value,
        publicationDate: publicationDateInput.value || "",
        rating: ratingInput.value || "",
        reviewNum: reviewNumInput.value || "",
        unitPrice: unitPriceInput.value || ""
    }).then(() => {
        alert("Data Updated Successfully");
        clearInputs();
    }).catch((error) =>{
        alert("Failed to update data: " + error.message);
        console.error(error);
    });
}

function deleteData(event) {
    event.preventDefault();
    const bookId = idInput.value;
    if (!bookId) {
        alert("Please enter book ID to delete.");
        return;
    }
    remove(ref(db, 'Book/' + bookId))
    .then(() => {
        alert("Data Deleted Successfully");
        clearInputs();
    }).catch((error) =>{
        alert("Failed to delete data: " + error.message);
        console.error(error);
    });

    // Thêm sự kiện mở popup khi nhấn vào nút "Add Book"
btnAdd.addEventListener('click', function(event) {
    document.querySelector('.popup').classList.add('active');
});

// Thêm sự kiện mở popup khi nhấn vào nút "Edit Book"
btnEdit.addEventListener('click', function(event) {
    document.querySelector('.popup').classList.add('active');
});

// Thêm sự kiện đóng popup khi nhấn vào nút "Close"
document.querySelector('.close').addEventListener('click', function(event) {
    document.querySelector('.popup').classList.remove('active');
});


}
