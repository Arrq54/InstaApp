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
                    //ZAPIS PLIKU NA SERWER NA FOLDER dir (NAZWA ALBUMU)
        


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
                                console.log("g");
                                resolve({
                                    originalName: file.name,
                                    url: newPath,
                                    album: fields.album
                                })
                            }
                        })
                      } catch (error) {
                        console.log(error);
                        reject("form parser error")
                      }

            })
          });
    }

}