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

let codeInput = document.getElementById("codeAdd")
let amountInput = document.getElementById("amountAdd")
let conditionInput = document.getElementById("conditionAdd");
let expirationInput = document.getElementById("expirationAdd");

get(child(dbRef, `vouchers/${key}`)).then((snapshot) => {
    if (snapshot.exists()) {
        var voucher = snapshot.val()
        codeInput.value = voucher.code
        amountInput.value = voucher.amount
        conditionInput.value = voucher.condition
        const expirationParts = voucher.expiration.split("/");
        const formattedExpiration = expirationParts[2] + "-" + expirationParts[1] + "-" + expirationParts[0];
        expirationInput.value = formattedExpiration
        // console.log(snapshot.val());
    } else {
        console.log("No data available");
    }
}).catch((error) => {
    console.error(error);
});

function deleteVoucher() {
    var confirmation = confirm("Are you sure want to delete this voucher?");
    if (confirmation) {
        remove(ref(db, 'vouchers/' + key))
        .then(() => {
            alert("Voucher is deleted!");
            window.history.back();
        })
        .catch((error) => {
            alert("Delete unsuccessfully1");
            console.log(error);
        });
    }
}

function updateVoucher() {
    const voucherRef = ref(db, `vouchers/${codeInput.value}`);
    const expirationParts = expirationInput.value.split("-");
    const formattedExpiration = expirationParts[2] + "/" + expirationParts[1] + "/" + expirationParts[0];
    const amount = parseInt(amountInput.value);
    const condition = parseInt(conditionInput.value);
    const updateData = {
        amount: amount,
        code: codeInput.value,
        condition: condition,
        expiration: formattedExpiration
    };
    update(voucherRef, updateData)
        .then(() => {
          alert("Voucher updated successfully!");
          window.history.back();
        })
        .catch((error) => {
          alert("Error updating voucher. Please check details.");
          console.error(error);
        });
}

let deleteBtn = document.getElementById("delete")
deleteBtn.addEventListener('click', deleteVoucher)
let updateBtn = document.getElementById("update")
updateBtn.addEventListener('click', updateVoucher)