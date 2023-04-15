const jsonController = require("./jsonController.js")
const fileController = require("./fileController.js")

const model = require("./model.js")
const imageRouter = async (req, res) => {
    res.writeHead(200, { "content-type": "application/json;charset=utf-8" })
    if(req.url == "/api/photos" && req.method == "GET"){
       
        //ALL PHOTOS
       
        res.end(JSON.stringify(jsonController.getAllPhotos()))


    }else if(req.url == "/api/photos" && req.method == "POST"){

        //ADD PHOTO

        let upload = await fileController.uploadFle(req)

        if(upload != false){
           
            res.end(JSON.stringify(jsonController.createNewPhoto(upload)), null, 5)
        }

    }else if(req.url.match(/\/api\/photos\/([0-9]+)/) && req.method == "GET"){

        //ONE PHOTO BY ID
      

        res.end(JSON.stringify(jsonController.getPhotoById(req.url.split("/")[3])))
        

    }else if(req.url.match(/\/api\/photos\/([0-9]+)/) && req.method == "DELETE"){
    
        //DELETE PHOTO BY ID
        let x = jsonController.getAndDelete(req.url.split("/")[3]);
        
        if(x == false){
           
            res.end(JSON.stringify({success: false, info: "Photo not found" }))
        }else{
            if(await fileController.deleteFile(x.url) == false){
              

                res.end(JSON.stringify({success: false, info: "file system error"}))
            }else{
                res.writeHead(200, { "content-type": "application/json;charset=utf-8" })

                res.end(JSON.stringify({success: true, info: "file deleted"}))
            }
        }
    }else if(req.url == "/api/photos" && req.method == "PATCH"){
        //UPDATE PHOTO BY ID
        res.end(JSON.stringify(await jsonController.updatePhoto(req)))

    }else if(req.url == "/api/photos/tags/mass" && req.method =="PATCH"){

        res.end(JSON.stringify(await jsonController.addTags(req)))

    }else if(req.url.match(/\/api\/photos\/tags\/([0-9]+)/)){

        res.end(JSON.stringify(await jsonController.getTagsById(req.url.split("/")[4])))

    }
    else if(req.url == "/api/photos/tags" && req.method =="DELETE"){

            res.end(JSON.stringify(await jsonController.deleteTagFromPhoto(req)))
        
    }
}

module.exports = imageRouter