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
  const aboutContainer = document.getElementById('aboutSpace');
  const aboutArr = aboutContainer.getElementsByTagName('p');

  if (aboutArr.length > 0) {
    const aboutText = aboutContainer.getElementsByTagName('p')[0];
    // Change the text in the aboutSpace element 
    if (aboutText.className === "about-hide") {
      aboutText.className = "about-show";
      aboutContainer.className = "background";
    } else {
      aboutText.className = "about-hide";
      aboutContainer.className = "";
    }
  }
}

/* Shows pictures when the "Pictures" button is pressed. */
function showPics() {
  const imgs = ["ucla1.JPG", "ucla2.JPG", "book.JPG", "harrypotter.JPG"]

  // Calculate a random index for the img array 
  const randInt = (Math.floor(Math.random() * imgs.length));
  const randImg = imgs[randInt];
    
  const div = document.getElementById('picSpace');
  const imgArr = div.getElementsByTagName('img');

  if (imgArr.length > 0) {
    const picContainer = imgArr[0];
    // If pictures pressed, either remove / show picture depending on what is already there
    if (picContainer.className === "image-hide") {
      picContainer.src = "/images/" + randImg;
      picContainer.className = "image-show";
      div.className = "background";
    } else {
      picContainer.className = "image-hide";
      div.className = "";
    }
  }
}
