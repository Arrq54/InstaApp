class Photo {
    constructor(id, album, originalName, url, lastChange, lastModifiedDate) {
        this.id = id;
        this.album = album;
        this.originalName = originalName;
        this.url = url;
        this.lastChange = lastChange;
        this.history = [
            {
                status: "original",
                lastModifiedDate: lastModifiedDate
            }
        ]
    }
    

}

let photosArray = []

module.exports = { Photo, photosArray };