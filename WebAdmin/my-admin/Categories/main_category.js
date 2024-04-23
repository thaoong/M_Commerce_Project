const admin = require('firebase-admin');
const serviceAccount = require('/Volumes/Study/Android_Project/WebAdmin/my-admin/config/serviceAccountKey.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: 'https://console.firebase.google.com/u/4/project/thestore-55f0f/database/thestore-55f0f-default-rtdb/data/~2F'
});
// Get a reference to the database service
const database = admin.database();
const categoriesRef = database.ref('categories/');

// Write data
function writeCategoriesData(id, imageLink, name) {
  const newCategoryRef = categoriesRef.push();
  newCategoryRef.set({
    id: id,
    imageLink: imageLink,
    name: name
  })
  .then(() => {
    console.log("Data written successfully.");
  })
  .catch((error) => {
    console.log("Error writing data:", error);
  });
}

// Example usage
writeCategoriesData("category10", "111", "234");

// Read data
categoriesRef.on('value', (snapshot) => {
  const categories = snapshot.val();
  console.log(categories);

  // Iterate over the categories object
  for (const categoryKey in categories) {
    const category = categories[categoryKey];
    console.log(category);
    console.log(typeof category);
  }
});