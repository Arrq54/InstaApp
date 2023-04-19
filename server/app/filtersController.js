const fs = require('fs');
const model = require("./model.js")
const formidable = require('formidable');
const sharp = require("sharp");
const form = formidable({ multiples: true });
const jsonController = require("./jsonController")
module.exports = { 
    getPhotoInfo: async (id)=>{
        return new Promise(async (resolve, reject) => {
            try {
                if (jsonController.getPhotoById(id).url) {
                    let metadata =   await sharp(jsonController.getPhotoById(id).url).metadata();
                    resolve(metadata)
                }
                else {
                    resolve("url_not_found")
                }
        
            } catch (err) {
                reject(err.mesage)
            }
        })
    },
    handleFilter: async (req)=>{
        return new Promise(async (resolve, reject) => {

            
            form.parse(req, async function(err, fields, files) {
                if(err){
                    reject("err")
                }


                let photoUrl = jsonController.getPhotoById(fields.id).url

                switch(fields.filterType){
                    case "resize":
                        if(fields.dimensions != null){
                            await sharp(photoUrl)
                                .resize({
                                    width: fields.dimensions.width,
                                    height: fields.dimensions.height
                                })
                                .toFile(photoUrl.slice(0,-4) + "_"+fields.filterType + ".jpg");
                        }
                        break;
                    case "tint":
                        if(fields.tint != null){
                            await sharp(photoUrl)
                            .tint({
                                r:fields.tint.r,
                                g:fields.tint.g,
                                b:fields.tint.b
                            })
                                .toFile(photoUrl.slice(0,-4) + "_"+fields.filterType + ".jpg");
                        }
                        break;
                    case "negate":
                        await sharp(photoUrl)
                            .negate()
                            .toFile(photoUrl.slice(0,-4) + "_"+fields.filterType + ".jpg");
                        break;
                    case "grayscale":
                        await sharp(photoUrl)
                            .grayscale()
                            .toFile(photoUrl.slice(0,-4) + "_"+fields.filterType + ".jpg");
                        break;
                    default: break;
                }
                resolve({sucess: true})

            })

            resolve({})
        })


    }
}