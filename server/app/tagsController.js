const fs = require('fs');
const model = require("./model.js")
const formidable = require('formidable');
const { log } = require('console');
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
    async addTags(req){
         //LOAD DEFAULT TAGS FROM JSON FILE
         await this.checkIfInit()
         //================================
         return new Promise((resolve, reject) => {

            try{
                form.parse(req, function(err, fields, files) {
                    let ids = []
                    console.log(fields);
                    if(!Array.isArray(fields.tags)){
                        fields.tags = [fields.tags]
                    }
                    fields.tags.map(z=>{
                        z = JSON.parse(z);
                        console.log(model.tagsArray);
                        let found = model.tagsArray.find(i=>{return i.name == z.name})
                        if(found == undefined || found == null)
                        {
                            let newTag = new model.Tag(model.tagsArray.length, z.name, z.popularity)
                            ids.push(newTag.id);
                            
                        }
                    })
                    resolve({message: "tags added", success: false, ids: ids})
                   
    
                    
                })
            }catch{
                resolve({message: "ERROR", success: false, ids: []})
            }
           
        })

    }
    
}