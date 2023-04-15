const model = require("./model.js")
const formidable = require('formidable');
const form = formidable({ multiples: true });
const tagsController = require("./tagsController")
const  generateId = ()=>{
    return String(Date.now()) +Math.floor(Math.random() * 20001);
}
module.exports = {

    createNewPhoto: (data)=>{
        //new photo
        return new model.Photo(generateId(), data.album, data.originalName, data.url, "original", data.timestamp)
    },
    getAllPhotos: ()=>{
        return model.photosArray;
    },
    getPhotoById: (id)=>{
        return model.photosArray.find((i)=>{return i.id == id}) != null ? model.photosArray.find((i)=>{return i.id == id}): {success: false, info: "Photo not found"};
    },
    updatePhoto: async (req)=>{

        return new Promise((resolve, reject) => {

            form.parse(req, function(err, fields, files) {
                let x =  model.photosArray.find((i)=>{return i.id == fields.id})
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
                resolve(x);
    
            })


        })

       
    },

    getAndDelete: (id)=>{
        let photo = model.photosArray.find((i)=>{return i.id == id});
        if(photo == null){
            return false;
        }
        model.photosArray = model.photosArray.filter(i=>{return i.id != photo.id})
        return photo;
    },

    addTags: async (req)=>{
        return new Promise((resolve, reject) => {
            form.parse(req, function(err, fields, files) {
                let photo = model.photosArray.find((i)=>{return i.id == fields.photoid});
                if(photo){
                    fields.ids.map(async (i)=>{
                        if(!photo.tags.find((z)=>{return z.id == i})){
                             await photo.addTag(await tagsController.getTagById(i))
                        }
                       
                    })
                    resolve(model.photosArray.find((i)=>{return i.id == fields.photoid}))
                }
                reject({success: false, message:"Photo not found"})
            })
        })
    },
    getTagsById(id){
        let photo = model.photosArray.find((i)=>{return i.id == id});
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
            form.parse(req, function(err, fields, files) {
                console.log(fields);
                let photo = model.photosArray.find((i)=>{return i.id == fields.photoid});
                if(photo){
                    photo.setTags(photo.tags.filter(i=>{return i.id !=fields.tagid }))
    
                    resolve( {success: true, mesasge: "Photo tags updated"})
                }
                resolve({success: false, message:"Photo not found"})
            })

        })
       

    },
}