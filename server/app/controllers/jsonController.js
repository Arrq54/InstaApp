const model = require("../model.js")
const formidable = require('formidable');
const form = formidable({ multiples: true });
const fs = require('fs');
const tagsController = require("./tagsController");
const path = require('path');
const dbController = require("./dbController.js");
const  generateId = ()=>{
    return String(Date.now()) +Math.floor(Math.random() * 20001);
}
module.exports = {

    loadDefault: ()=>{
        fs.readdir("./uploads/default", (err, files) => {
            files.forEach(file => {
                new model.Photo(generateId(), "defualt", file, ("/uploads" + path.resolve(__dirname, `../uploads/default/${file}`).split("uploads")[1]).replaceAll("\\","/"), "original", "")
            });
          });
          return "";
    },

    createNewPhoto: (data)=>{
        //new photo
        // console.log("Uploaded:" + data.url);
        console.log(data);
        return new model.Photo(generateId(), data.album, data.originalName, data.url, data.description, data.timestamp, data.location)
    },
    getAllPhotos: async ()=>{
        return  await dbController.getAllPosts();
    },
    getPhotoById: async(id)=>{
        return await dbController.getPostById(id);
    },
    updatePhoto: async (req)=>{

        return new Promise((resolve, reject) => {

            form.parse(req, async function(err, fields, files) {
                let x = await dbController.getPostById(fields.id)
              
                if(x == null){reject({success: false, info: "Photo not found"})}
                x.lastChange = fields.lastChange;
                x.history.map((i)=>{
                    if(i.status == fields.lastChange){
                        resolve(x)
                    }
                })
                x.history.push({
                    status: fields.lastChange,
                    timestamp: Date.now()
                })

                dbController.updatePost(fields.id, x)
                resolve(data);
    
            })


        })

       
    },

    getAndDelete: async (id)=>{
        let photo = await dbController.getPostById(id)
        if(photo == null){
            return false;
        }
        await collection.deleteMany({id: photo.id})
        return photo;
    },
 
    addTags: async (req)=>{
         
        return new Promise(async (resolve, reject) => {
            form.parse(req, async function(err, fields, files) {
                let photo = await dbController.getPostById(fields.photoid)
                console.log(fields);
                if(photo != null && photo.tags.length == 0){
                    if(fields.ids != undefined){
                        if(!Array.isArray(fields.ids)){
                            fields.ids = [fields.ids]
                        }
                        let newTags = []
                        
                        fields.ids.map(async (i)=>{ 
                         
                                // console.log(await tagsController.getTagById(i));
                            let tagToAdd = await tagsController.getTagById(i);
                            console.log(tagToAdd);
                            newTags.push(tagToAdd);
                            if(newTags.length == fields.ids.length){
                                photo.tags = newTags
                                await dbController.updatePost(photo.id, photo)
                                resolve(photo)
                            }
                        })
                    }
                }else{
                    resolve({success: false, message:"Photo not found"})
                }
            })
        })
    },
    async getTagsById (id){
        let photo = await dbController.getPostById(id)
        if(photo){
            return {
                id: id,
                tags: photo.tags
            };
        }
        return {success: false, message:"Photo not found"};
    },
    deleteTagFromPhoto: async (req)=>{
        return new Promise((resolve, reject) => {
            form.parse(req, async function(err, fields, files) {
                // console.log(fields);
                let photo= await dbController.getPostById(fields.photoid)
                if(photo){
                    photo.setTags(photo.tags.filter(i=>{return i.id !=fields.tagid }))
    
                    resolve( {success: true, mesasge: "Photo tags updated"})
                }
                resolve({success: false, message:"Photo not found"})
            })

        })
       

    },
    getPhotosByAlbum: async (album)=>{
        return await dbController.getPostsByAlbum(album);
    }
}