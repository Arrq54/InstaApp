const http = require('http');

require('dotenv').config();
const PORT  = process.env.APP_PORT
const imageRouter = require("./app/imageRouter")
const tagsRouter = require("./app/tagsRouter.js")
const filtersRouter = require("./app/filtersRouter")
const userRouter = require("./app/userRouter.js")
http.createServer(async (req, res) => {
    
    //images

    if (req.url.search("/api/photos") != -1) {
       await imageRouter(req, res)
    }

    //tags

    else if (req.url.search("/api/tags") != -1) {
      console.log("Use tags router");
       await tagsRouter(req, res)
    }

    //filters router
   else if (req.url.search("/api/filters") != -1) {
      await filtersRouter(req, res)
   }else if (req.url.search("/api/user") != -1) {
      await userRouter(req, res)
   }

})
.listen(PORT, () => console.log("listen on 3000"))