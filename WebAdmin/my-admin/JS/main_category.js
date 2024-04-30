document.addEventListener("DOMContentLoaded", () => {
  let tableBody = document.querySelector("tbody");
  let addCategory = document.querySelector(".add-category");
  let popup = document.querySelector(".popup");
  let form = document.querySelector("form");

  // Get a reference to the database service
  var database = firebase.database();
  var categoriesRef = firebase.database().ref("categories/");

  // Write data
  function writeCategoriesData(id, imageLink, name) {
    categoriesRef
      .child(id)
      .set({
        id: id,
        imageLink: imageLink,
        name: name,
      })
      .then(
        (onFullFilled) => {
          console.log("Data written successfully.");
        },
        (onRejected) => {
          console.log("Error writing data:", onRejected);
        }
      );
  }

  // Example usage
  writeCategoriesData("category10", "111", "234");

  // Read data
  categoriesRef.on("value", (snapshot) => {
    const categories = snapshot.val();
    console.log(categories);

    // Clear the table body
    tableBody.innerHTML = "";

    // Iterate over the categories object
    for (category in categories) {
      let tr = `
      <tr>
        <td>${categories[category].id}</td>
        <td>${categories[category].imageLink}</td>
        <td>${categories[category].name}</td>
        <td>
          <button class="edit">Edit</button>
          <button class="delete">Delete</button>
        </td>
      </tr>
    `;
      tableBody.innerHTML += tr;
    }
  });

  // Write Dynamic Data
  addCategory.addEventListener("click", () => {
    popup.classList.add("active");
    form.addEventListener("submit", (e) => {
      e.preventDefault();
      writeCategoriesData(form.id.value, form.imageLink.value, form.name.value);
    });
  });

  // Close popup
  window.addEventListener("click", (e) => {
    if (e.target == popup) {
      popup.classList.remove("active");
    }
  });
});