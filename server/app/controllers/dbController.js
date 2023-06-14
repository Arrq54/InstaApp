const { MongoClient, ObjectId } = require('mongodb');
const dbConnect = require("./dbConnect")

module.exports = {
    getAllPosts: async ()=>{
        return new Promise(async (resolve, reject)=>{
            let db = await dbConnect.connect();
            const collection = await db.collection("posts");
            const items = await collection.find({}).toArray()
            resolve(items)
        }) 
       
    }, getPostById: async (id)=>{
        return new Promise(async (resolve, reject)=>{
            let db = await dbConnect.connect();
            const collection = await db.collection("posts");
            const items = await collection.findOne({id: id})
            resolve(items)
        })
       
    },
    updatePost: async(id, obj)=>{
        let db = await dbConnect.connect();
        const collection = await db.collection("posts");
        const data = await collection.updateOne(
            { id: id },
            { $set: obj }
         )
    },
    getPostsByAlbum: async(album)=>{
        return new Promise(async (resolve, reject)=>{
            let db = await dbConnect.connect();
            const collection = await db.collection("posts");
            const items = await collection.find({album: album}).toArray()
            resolve(items)
        }) 
    },
    
    addPost: async (post)=>{
        let db =await dbConnect.connect();
        const collection = await db.collection("posts");
        const result = await collection.insertOne(post);
    },
    

}