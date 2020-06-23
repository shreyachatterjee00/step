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

/*
Shows about text when the "About" button is pressed.
*/
function showAbout () {
    aboutText = "I'm a rising junior at UCLA, majoring in Linguistics and Computer Science (that's one major)! In my free time, I enjoy reading and playing the piano. \n\n Recently, I've started learning to cook (I'm going to need this for my college apartment next year) and playing Stardew Valley (turns out video games are a great way to stay connected to friends during this time)!";
    var aboutContainer = document.getElementById('aboutSpace');
    //if about pressed, either remove / show about text depending on what is already there
    if (aboutContainer.innerText === "") {
        //change inner text to about text, and div class to CSS about class
        aboutContainer.innerText = aboutText;
        aboutContainer.className="background";
    }
    else {
        aboutContainer.innerText = "";
        aboutContainer.className="";
    }
}

/*
Shows pictures when the "Pictures" button is pressed.
*/
function showPics() {
    //array of images
    let imgs = ["ucla1.JPG", "ucla2.jpg.JPG", "book.JPG", "harrypotter.JPG"]
    //random int from 0 - 4
    var randInt = (Math.floor(Math.random() * 5));
    var randImg = imgs[randInt];
    


    var img1 = "images/ucla1.JPG";
    var div = document.getElementById('picSpace');
    var picContainer = document.getElementById('picSpace').getElementsByTagName('img')[0];

    //if pictures pressed, either remove / show picture depending on what is already there
    if (picContainer.src==="https://8080-0ba309ed-2f3c-4b55-83b6-7741ff94bec3.us-west1.cloudshell.dev/base") {
        picContainer.src = "https://8080-0ba309ed-2f3c-4b55-83b6-7741ff94bec3.us-west1.cloudshell.dev/images/" + randImg;
        picContainer.style.display = 'block';
        div.className = 'background';
        
        
    }
    else {
        picContainer.src="https://8080-0ba309ed-2f3c-4b55-83b6-7741ff94bec3.us-west1.cloudshell.dev/base";
        picContainer.style.display = 'none';
        div.className ="";
    }
    
}