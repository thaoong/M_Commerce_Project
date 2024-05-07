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

let codeInput = document.getElementById("codeAdd")
let amountInput = document.getElementById("amountAdd")
let conditionInput = document.getElementById("conditionAdd");
let expirationInput = document.getElementById("expirationAdd");

function postVoucher() {
    if (codeInput.value.trim() === '') {
        alert("Please enter a valid voucher code!");
        return;
    }
    const voucherRef = ref(db, `vouchers/${codeInput.value}`);
    // Check if the voucher code already exists before upload
    get(voucherRef)
        .then((snapshot) => {
            if (snapshot.exists()) {
                alert("This voucher code already exists. Please choose a different code!");
                return;
            }
            const expirationParts = expirationInput.value.split("-");
            const formattedExpiration = expirationParts[2] + "/" + expirationParts[1] + "/" + expirationParts[0];
            const amount = parseInt(amountInput.value);
            const condition = parseInt(conditionInput.value);
            set(voucherRef, {
                amount: amount,
                code: codeInput.value,
                condition: condition,
                expiration: formattedExpiration
            })
                .then(() => {
                    alert("Voucher added successfully!");
                    window.history.back();
                })
                .catch((error) => {
                    alert("Failed to add voucher. Please check details.");
                    console.error(error);
                });
        })
}

let addVoucher = document.getElementById("addVoucher")
addVoucher.addEventListener('click', postVoucher)