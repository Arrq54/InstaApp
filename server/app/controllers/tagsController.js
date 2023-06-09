const fs = require('fs');
const model = require("../model.js")
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
    async addTagsList(req){
        if(model.tagsArray.length == 0){
            fs.readFile('app/data/tags.json', 'utf8', function readFileCallback(err, data){
                if (err){
                    console.log(err);
                } else {
                let dataFromFile = JSON.parse(data)
                dataFromFile.map(i=>{
                   new model.Tag(i.id, i.name, i.popularity)
                })
            }});
        }
        return new Promise((resolve, reject) => {
            let ids = []
            formidable({multiples: true}).parse(req, function(err, fields, files){
                if(fields.tags != undefined){
                    if(!Array.isArray(fields.tags)){
                        fields.tags = [fields.tags]
                    }
                    fields.tags.map(i=>{
                        if(model.tagsArray.find(z=>{return z.name == i}) == null){
                            let newTag = new model.Tag(model.tagsArray.length, i, 1)
                            ids.push(newTag.id)
                        }
                       
                    })
                }
                resolve({message: "SUCCESS", success: true, ids: ids})
            })
          

        })


           // formidable({multiples: true}).parse(req, function(err, fields, files){
           //     console.log(fields);
           //     let ids = []
           //     // if(err){
           //     //     resolve({message: "ERROR", success: false, ids: []})
           //     // }
             
           //     // let ids = []
           //     // if(fields.tags != undefined){
           //     //     if(!Array.isArray(fields.tags)){
           //     //         fields.tags = [fields.tags]
           //     //     }
           //     //     fields.tags.map(i=>{
   
           //     //         let newTag = new model.Tag(model.tagsArray.length, i, 1)
           //     //         ids.push(newTag.id)
           //     //     })
           //     // }
           //     return {message: "SUCCESS", success: true, ids: ids}
           // })

       
       
   },
    async getTagById(id){
        return new Promise(async (resolve, reject)=>{
            await this.checkIfInit()
            let found = model.tagsArray.find(i=>{
                return i.id == id 
            })
            if(found){
    
                resolve(found)
            }else{
                resolve(null)
            }
        })
    },
   
    
} 