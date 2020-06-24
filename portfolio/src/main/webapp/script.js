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
    aboutTest = aboutContainer.getElementsByTagName('p');
    if (aboutTest.length > 0) {
        aboutText = aboutContainer.getElementsByTagName('p')[0];
    }

    // Change the text in the aboutSpace element 
    if (aboutText.className === "about-hide") {
        aboutText.className = "about-show";
        aboutContainer.className = "background";
    }
    
    else {
        //aboutContainer.innerText = "";
        aboutText.className = "about-hide";
        aboutContainer.className = "";
    }
}

/* Shows pictures when the "Pictures" button is pressed. */
function showPics() {
    const imgs = ["ucla1.JPG", "ucla2.jpg.JPG", "book.JPG", "harrypotter.JPG"]
    const srcBase = "https://8080-0ba309ed-2f3c-4b55-83b6-7741ff94bec3.us-west1.cloudshell.dev/base"
    const picPath = "https://8080-0ba309ed-2f3c-4b55-83b6-7741ff94bec3.us-west1.cloudshell.dev/images/"
    
    // Calculate a random index for the img array 
    const randInt = (Math.floor(Math.random() * imgs.length));
    const randImg = imgs[randInt];
    
    const div = document.getElementById('picSpace');
    const imgArr = div.getElementsByTagName('img');

    if (imgArr.length > 0) {
        picContainer = imgArr[0];
    }

    // If pictures pressed, either remove / show picture depending on what is already there
    if (picContainer.src === srcBase) {
        picContainer.src = picPath + randImg;
        picContainer.className = "displayImage";
        div.className = "background";
    }
    else {
        picContainer.src = srcBase;
        picContainer.className = "image";
        div.className = "";
    }
    
}
