const fs = require('fs');
const model = require("./model.js")
const formidable = require('formidable');
const form = formidable({ multiples: true });

module.exports = { 
    async init(){
        return new Promise((resolve, reject) => {
            fs.readFile('app/data/tags.json', 'utf8', function readFileCallback(err, data){
                if (err){
                    console.log(err);
                    reject(false);
                } else {
                let dataFromFile = JSON.parse(data)
                dataFromFile.map(i=>{
                   new model.Tag(i.id, i.name, i.popularity)
                })
                // console.log(model.tagsArray);
                resolve(true)
            }});
        })
    },
    async checkIfInit(){
        if(model.tagsArray.length == 0){
            await this.init()
            return;
        }
        return
    },
    async getAllTags(){
        //LOAD DEFAULT TAGS FROM JSON FILE
        await this.checkIfInit()
        //================================
        return model.tagsArray
    },
    async getRawTags(){
        //LOAD DEFAULT TAGS FROM JSON FILE
        await this.checkIfInit()
        //================================
        return model.tagsArray.map(i=>{
            return i.name
        })
    },
    async getTagById(id){
         //LOAD DEFAULT TAGS FROM JSON FILE
         await this.checkIfInit()
         //================================

        let found = model.tagsArray.find(i=>{
            return i.id == id 
        })
        if(found){
            return found;
        }
        return {success: false, mesasge: "Tag not found"}
    },
    async addTag(req){
         //LOAD DEFAULT TAGS FROM JSON FILE
         await this.checkIfInit()
         //================================
         return new Promise((resolve, reject) => {

            form.parse(req, function(err, fields, files) {
                console.log(fields);


                if(!model.tagsArray.find(i=>{return i.name == fields.name}))
                {
                    new model.Tag(model.tagsArray.length, fields.name, fields.popularity)
                    resolve({message: "Tag added", success: true})
                }
                resolve({message: "Tag already exists", success: false})
            })
        })

    }
    
}