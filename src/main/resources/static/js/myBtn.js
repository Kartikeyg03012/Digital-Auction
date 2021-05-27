mybutton = document.getElementById("myBtn");

window.onscroll = function() {scrollFunction()};

function scrollFunction() {
  if (document.body.scrollTop > 750 || document.documentElement.scrollTop > 750) {
    mybutton.style.display = "block";
  } else {
    mybutton.style.display = "none";
  }
}
//
// // When the user clicks on the button, scroll to the top of the document
// function topFunction() {
//
//   // document.body.scrollTop = 0; // For Safari
//   // document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
// }
function toggleMore() {
   // get the clock
   var myMore = document.getElementById('viewMore');
   // get the current value of the clock's display property
   var displaySetting = myMore.style.display;

   // also get the clock button, so we can change what it says
   var moreButton = document.getElementById('viewMoreButton');
var displaySetting = moreButton.style.display;
   // now toggle the clock and the button text, depending on current state

   if (displaySetting == 'block') {
     // clock is visible. hide it
     myMore.style.display = 'none';
     // change button text
     moreButton.innerHTML = 'Show clock';
   }
   else {
     // clock is hidden. show it
     myMore.style.display = 'block';
     // change button text
     moreButton.style.display = 'none';
   }
 }
