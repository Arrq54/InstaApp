const jsonController = require("./jsonController.js")
const fileController = require("./fileController.js")

const model = require("./model.js")
// const getRequestData = require("./utils.js")
const fs = require("fs")
const router = async (req, res) => {

    if(req.url == "/api/photos" && req.method == "GET"){

        //ALL PHOTOS
        res.end(JSON.stringify("ae"))


    }else if(req.url == "/api/photos" && req.method == "POST"){

        //ADD PHOTO

        let upload = await fileController.uploadFle(req)

        if(upload !=false){

            //TO DO - ZAPIS PLIKU DO JSONA I ZWROCENIE GO
            console.log(upload);
        }



        res.end(JSON.stringify("post"))


    }else if(req.url.match(/\/api\/photos\/([0-9]+)/) && req.method == "GET"){

        //ONE PHOTO BY ID


    }else if(req.url.match(/\/api\/photos\/([0-9]+)/) && req.method == "DELETE"){
    
        //DELETE PHOTO BY ID
    
    
    }else if(req.url == "/api/photos" && req.method == "PATCH"){

        //UPDATE PHOTO BY ID


    }

}

module.exports = router