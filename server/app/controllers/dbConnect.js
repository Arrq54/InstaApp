const { MongoClient, ObjectId } = require('mongodb');
module.exports = {

    connect: async ()=>{
        try {
            const mongoClient = new MongoClient(`mongodb+srv://instaapp:${process.env.MONGODB_PASSWORD}@instaapp.yk45hra.mongodb.net/?retryWrites=true&w=majority`);
            await mongoClient.connect();
            const db = mongoClient.db("InstaApp");
            console.log('\x1b[32m%s\x1b[0m',"\nSuccesfully conected to MongoDB database\n");
            return db
        } catch (error) {
            return error.message
        }    
    }

}