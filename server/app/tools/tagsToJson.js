const fs = require('fs');
const tags = [
    "#instagood",
    "#love",
    "#fashion",
    "#photooftheday",
    "#art",
    "#photography",
    "#instagram",
    "#beautiful",
    "#picoftheday",
    "#nature",
    "#happy",
    "#cute",
    "#travel",
    "#style",
    "#followme",
    "#tbt",
    "#instadaily",
    "#repost",
    "#like4like",
    "#summer",
    "#beauty",
    "#fitness",
    "#food",
    "#selfie",
    "#me",
    "#instalike",
    "#girl",
    "#friends",
    "#fun",
    "#photo"
]

let newTags = []

tags.map((i,index)=>{
    newTags.push({
        id: index,
        name: i,
        popularity: Math.ceil(Math.random() * (1500 - 100) + 100)

    })
})
let json = JSON.stringify(newTags, null, 5)
fs.writeFile('app/data/tags.json', json, 'utf8', (err)=>{console.log(err);});