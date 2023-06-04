const { MongoClient, ObjectId } = require('mongodb');
const dbConnect = require("./dbConnect")

module.exports = {

    addPost: async (post)=>{
        let db =await dbConnect.connect();
        const collection = await db.collection("posts");
        const result = await collection.insertOne(post);
    }

}