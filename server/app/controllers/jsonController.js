const model = require("../model.js")
const formidable = require('formidable');
const form = formidable({ multiples: true });
const fs = require('fs');
const tagsController = require("./tagsController");
const path = require('path');
const dbControllerPosts = require("./dbControllerPosts.js");
const { log } = require("console");
const filtersController = require("./filtersController.js");
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

    createNewPhoto: async (data)=>{

        return new Promise(async (resolve, reject)=>{
            let photo =  new model.Photo(generateId(), data.album, data.originalName, data.url, data.description, data.timestamp, data.location, data.filter)
            if(data.filter != ""){
                await filtersController.filters(photo.url.substring(1), data.filter)
                resolve(photo)
            }else{
                resolve(photo)  
            }
        })
        
    },    
    getAllPhotos: async ()=>{ 
        return  await dbControllerPosts.getAllPosts();
    },
    getPhotoById: async(id)=>{
        return await dbControllerPosts.getPostById(id);
    },
    updatePhoto: async (req)=>{

        return new Promise((resolve, reject) => {

            form.parse(req, async function(err, fields, files) {
                let x = await dbControllerPosts.getPostById(fields.id)
              
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

                dbControllerPosts.updatePost(fields.id, x)
                resolve(data);
    
            })


        })

       
    },

    getAndDelete: async (id)=>{
        let photo = await dbControllerPosts.getPostById(id)
        if(photo == null){
            return false;
        }
        await collection.deleteMany({id: photo.id})
        return photo;
    },
 
    addTags: async (req)=>{
         
        return new Promise(async (resolve, reject) => {
            form.parse(req, async function(err, fields, files) {
                console.log(fields);
                let photo = await dbControllerPosts.getPostById(fields.photoid)
                console.log("==========PHOTO==========");
                console.log(photo);
                console.log("=========================");
                if(photo != null && photo.tags.length == 0){
                    if(fields.ids != undefined){
                        if(!Array.isArray(fields.ids)){
                            fields.ids = [fields.ids]
                        }
                        let newTags = []
                        
                        fields.ids.map(async (i)=>{
                            let tagToAdd = await tagsController.getTagById(i);
                            console.log(tagToAdd);
                            newTags.push(tagToAdd);
                            if(newTags.length == fields.ids.length){
                                photo.tags = newTags
                                await dbControllerPosts.updatePost(photo.id, photo)
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
        let photo = await dbControllerPosts.getPostById(id)
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
                let photo= await dbControllerPosts.getPostById(fields.photoid)
                if(photo){
                    photo.setTags(photo.tags.filter(i=>{return i.id !=fields.tagid }))
    
                    resolve( {success: true, mesasge: "Photo tags updated"})
                }
                resolve({success: false, message:"Photo not found"})
            })

        })
       

    },
    getPhotosByAlbum: async (album)=>{
        return await dbControllerPosts.getPostsByAlbum(album);
    }
}