const http = require('http');
const PORT  =3000
const imageRouter = require("./app/imageRouter")
const tagsRouter = require("./app/tagsRouter.js")
const filtersRouter = require("./app/filtersRouter")
http.createServer(async (req, res) => {
    
    //images

    if (req.url.search("/api/photos") != -1) {
       await imageRouter(req, res)
    }

    //tags

    else if (req.url.search("/api/tags") != -1) {
       await tagsRouter(req, res)
    }

    //filters router
   else if (req.url.search("/api/filters") != -1) {
      await filtersRouter(req, res)
   }

})
.listen(PORT, () => console.log("listen on 3000"))