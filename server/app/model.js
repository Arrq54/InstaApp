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

        photosArray.push(this);
    }
    

}

let photosArray = []

module.exports = { Photo, photosArray };