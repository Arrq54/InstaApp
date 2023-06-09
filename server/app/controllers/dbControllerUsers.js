const { MongoClient, ObjectId } = require('mongodb');
const dbConnect = require("./dbConnect")

module.exports = {
    addUser: async (post)=>{
        let db = await dbConnect.connect();
        const collection = await db.collection("users");
        const result = await collection.insertOne(post);
    },
    getUserById: async (id)=>{
        console.log("Get user by id: "+ id );
        return new Promise(async (resolve, reject)=>{
            let db = await dbConnect.connect();
            const collection = await db.collection("users");
            const items = await collection.findOne({id: id})
            resolve(items)
        })
    } ,
    getUserByName: async (name)=>{
        return new Promise(async (resolve, reject)=>{
            let db = await dbConnect.connect();
            const collection = await db.collection("users");
            const items = await collection.findOne({name: name})
            resolve(items)
        })
    } ,
    updateUser: async(id, obj)=>{
        let db = await dbConnect.connect();
        const collection = await db.collection("users");
        const data = await collection.updateOne(
            { id: id },
            { $set: obj }
         )
    },
    

}