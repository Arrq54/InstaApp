const model = require("./model.js")
const formidable = require('formidable');
const fs = require('fs');
const path = require('path')

const mainPath = path.join(__dirname, "../");
module.exports = {
    uploadFle(req){
        const form = formidable({ multiples: true });
            return new Promise((resolve, reject) => {
                form.parse(req, (err, fields, files) => {
                    //CREATE ALBUM IF NOT EXISTS
                    let dir = `./uploads/${fields.album}`
                    if (!fs.existsSync(dir)){
                        fs.mkdirSync(dir);
                    }
        
                    let file = files.file;
                    try {
                        let newFileName = file.path.split("\\");
                        newFileName = newFileName[newFileName.length - 1];
                        var newPath =  mainPath +"/"+dir+ '/'+newFileName+".jpg"
        
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
                                        resolve({
                                            originalName: file.name,
                                            url: newPath,
                                            album: fields.album,
                                            timestamp: stats.birthtime
                                        })
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