var phantom = require('phantom');
 
phantom.create().then(function(ph) {
 ph.createPage().then(function(page) {
  page.open("http://127.0.0.1:8000/chapter-10.html").then(function(status) {
  setTimeout(() => {
    page.property('viewportSize',{width: 500, height: 500});
    page.render('./chapter-102.pdf').then(function(){
     console.log('Page rendered');
     ph.exit();
    });
   });
  }, 3000);
 
 });
});