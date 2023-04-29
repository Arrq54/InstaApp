class Photo {
    constructor(id, album, originalName, url, lastChange, timestamp) {
        this.id = id;
        this.album = album;
        this.originalName = originalName;
        this.url = url;
        this.lastChange = lastChange;
        this.history = [
            {
                status: "original",
                timestamp: timestamp
            }
        ]
        this.tags = []

        photosArray.push(this);
    }
    async addTag(tag){
        return new Promise((resolve, reject) => {
            this.tags.push(tag)
            resolve()
        })
    }
    setTags(tags){
        console.log(tags);
        this.tags = tags
    }
    

}

let photosArray = []


class Tag{
    constructor(id, name, popularity){
        this.id = id;
        this.name = name;
        this.popularity = popularity;
        tagsArray.push(this)
    }
}
let tagsArray = []


class User{
    constructor(id, name, lastName, email, confirmed, password){
        this.id = id
        this.name = name;
        this.lastName = lastName
        this.email = email
        this.confimred = confirmed;
        this.password = password

        usersArray.push(this)
   
    }
    verify(){
        this.confimred = true;
    }
}
let usersArray = []


module.exports = { Photo, photosArray, Tag, tagsArray, User, usersArray };