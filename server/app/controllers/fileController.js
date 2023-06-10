const model = require("../model.js")
const formidable = require('formidable');
const fs = require('fs');
const path = require('path');
const mainPath = path.join(__dirname, "../../");
module.exports = {
    uploadFle(req){ 
        const form = formidable({ multiples: true });
            return new Promise((resolve, reject) => {
                form.parse(req, (err, fields, files) => {

                    console.log(fields);
                    //CREATE ALBUM IF NOT EXISTS
                    let dir = `./uploads/${fields.album}`
                    if (!fs.existsSync(dir)){
                        fs.mkdirSync(dir);
                    }
        
                    let file = files.file;
                    try {
                        let newFileName = file.path.split("\\");
                        newFileName = newFileName[newFileName.length - 1];

                        let ext = file.name.split(".")[ file.name.split(".").length - 1]

                        var newPath =  mainPath +"/"+dir+ '/'+newFileName + "."+ext
        
                        var rawData = fs.readFileSync(file.path)
        
                        fs.writeFile(newPath, rawData, function(err){
                            if(err){
                                console.log(err);
                                reject("file system error")
                            }else{
                                fs.stat(newPath, (err, stats) => {
                                    if(err){
                                        console.log(err);
                                        reject("file system error")
                                    }else{
                                        newPath = "/uploads" + newPath.split("uploads")[1]
                                        let obj = {
                                            originalName: file.name,
                                            url: newPath,
                                            album: fields.album,
                                            description: fields.description,
                                            timestamp: stats.birthtime,
                                            location: {
                                                id: "",
                                                name: ""
                                            },
                                            filter: ""
                                        }
                                        if(fields.location != null){
                                            obj.location = JSON.parse(fields.location)
                                        }
                                        if(fields.filter != null){
                                            obj.filter = fields.filter
                                        }
                                        resolve(obj)
                                    }
                                   
                                })
                                
                            }
                        })
                      } catch (error) {
                        console.log(error);
                        reject("form parser error")
                      }

            })
          });
    },
    deleteFile: (fileURL)=>{
        return new Promise((resolve, reject) => {
            fs.unlink(fileURL, (err)=>{
                if(err){
                    reject(false)
                }
                resolve(true)
            })
        })
    }

}