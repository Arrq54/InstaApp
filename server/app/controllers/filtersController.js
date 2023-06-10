const fs = require('fs');
const model = require("../model.js")
const formidable = require('formidable');
const sharp = require("sharp");
const form = formidable({ multiples: true });
const jsonController = require("./jsonController")
module.exports = { 
    getPhotoInfo: async (id)=>{
        return new Promise(async (resolve, reject) => {
            try {
                if ((await jsonController.getPhotoById(id)).url) {
                    let metadata =   await sharp((await jsonController.getPhotoById(id)).url).metadata();
                    resolve(metadata)
                }
                else {
                    resolve("url_not_found")
                }
         
            } catch (err) {
                console.log(err);
                resolve(err.mesage)
            }
        }) 
    },
    filters: async(url, filter)=>{
        return new Promise(async (resolve, reject) => {
         
            let tint = {}
            if(filter == "retro"){
                tint.r = 200;
                tint.g = 160;
                tint.b = 100
            }else if(filter == "grayscale"){
                let buffer = await sharp(url).grayscale().toBuffer()
                await sharp(buffer).toFile(url.slice(0,-4) + ".jpg");
                return;
            }else if(filter == "pastelpink"){
                tint.r = 255;
                tint.g = 180;
                tint.b = 200
            }else if(filter == "goldenhour"){
                tint.r = 255;
                tint.g = 190;
                tint.b = 100
            }else if(filter == "cinematic"){
                tint.r = 60;
                tint.g = 30;
                tint.b = 90
            }
            let buffer = await sharp(url).tint(tint).toBuffer()

            await sharp(buffer).toFile(url.slice(0,-4) + ".jpg");

        })
    },
    handleFilter: async (req)=>{
        return new Promise(async (resolve, reject) => {

            /*
retro
"r": 100,
    "g": 75, 
    "b": 50
*/
/**
 * retro 2
 * "r": 100,
    "g": 75, 
    "b": 0
 */
/**
 * retro 3
 *  "tint": {
    "r": 200,
    "g": 160, 
    "b": 100
    }
 * 
 */
/**niebieskie
 *  "r": 140,
    "g": 170, 
    "b": 210
 */

/*
Vibrant Teal Tint:
 "r": 0,
    "g": 100, 
    "b": 150 */
            form.parse(req, async function(err, fields, files) {
                if(err){
                    reject("err")
                }
                console.log(fields);
                console.log(await jsonController.getPhotoById(fields.id));
                let photoUrl = (await jsonController.getPhotoById(fields.id)).url
                photoUrl = photoUrl.substring(1)
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