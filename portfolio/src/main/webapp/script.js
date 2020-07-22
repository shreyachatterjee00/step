// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/* Shows about text when the "About" button is pressed. */
function showAbout () {
  const aboutText = document.getElementById('aboutSpace');

  if (aboutText.classList.contains("about-hide")) {
    aboutText.classList.remove("about-hide");
    aboutText.classList.add("about-show");
    aboutText.classList.add("background");
  } else {
    aboutText.classList.remove("about-show");
    aboutText.classList.remove("background");
    aboutText.classList.add("about-hide");
    }
}

/* Shows pictures when the "Pictures" button is pressed. */
function showPics() {
  const imgs = ["ucla1.JPG", "ucla2.JPG", "book.JPG", "harrypotter.JPG"]

  // Calculate a random index for the img array 
  const randInt = (Math.floor(Math.random() * imgs.length));
  const randImg = imgs[randInt];
    
  const img = document.getElementById('picSpace');

  if (img.classList.contains("image-hide")) {
    img.classList.remove("image-hide");
    img.classList.add("image-show");
    img.classList.add("background");
    img.src = "/images/" + randImg;
  } else {
    img.classList.remove("image-show");
    img.classList.remove("background");
    img.classList.add("image-hide");
  }
}

/* Gets array list of messages from servlet, converts to JSON, and displays it on home page. */
function getBucketList() {
  fetch('/data').then(response => response.json()).then((list) => {
    const listElement = document.getElementById('list-container');
    listElement.innerHTML = '';

    listElement.appendChild(createOrderedList(list));
  });
}


/* Delete list elements in the bucket list */
function deleteData() {
  fetch('/delete-data', {method: 'POST'});
}

/* Fetch sentiment score of user's message and display it. */
function doSentAnalysis() {
  const sentComm = document.getElementById("sent-id").value;

  fetch('/sentiment?sentiment-comment=' + sentComm).then(response => response.json()).then((sentimentScore) => {
    const sentElement = document.getElementById('sentiment-container');
    sentElement.innerHTML = '';
    
    if (sentimentScore) {
      sentElement.innerText = "Sentiment Analysis score is: " + sentimentScore;
    }
  });
}

/* Fetch entities in user's message and display it. */
function doEntExtraction() {
  const entEx = document.getElementById("ent-id").value;

  fetch('/entity?entity-comment=' + entEx).then(response => response.json()).then((entities) => {
    const entElement = document.getElementById('entity-container');
    entElement.innerHTML = '';
    let entString = "";
    for (let i = 0; i < entities.length; i++) {
        entString += "{" + entities[i] + "} ";
    }
    entElement.innerText = entString;
    });

/* Creates a <ol> ordered list, and creates inner <li> tags for items. */
function createOrderedList(array) {
  let list = document.createElement('ol');

  for (let i = 0; i < array.length; i++) {
    if (array[i]) {
      const liElement = document.createElement('li');
      liElement.innerText = array[i];
      list.appendChild(liElement);
    }
  }
    return list;
}

/* Delete list elements in the bucket list */
function deleteData() {
    fetch('/delete-data', {method: 'POST'});
}