const tagsController = require("../controllers/tagsController")
//TO DO - POPRAWIC KODY (404 ITP)
const tagsRouter = async (req, res) => {
    res.writeHead(200, { "content-type": "application/json;charset=utf-8" })
    if(req.url == "/api/tags" && req.method == "GET"){
        res.end(JSON.stringify(await tagsController.getAllTags()))
    }else if(req.url == "/api/tags/raw"){

        res.end(JSON.stringify(await tagsController.getRawTags()))

    }else if(req.url.match(/\/api\/tags\/([0-9]+)/) && req.method == "GET"){

        res.end(JSON.stringify(await tagsController.getTagById(req.url.split("/")[3])))

    }else if(req.url == "/api/tags/add" && req.method == "POST"){
        res.end(JSON.stringify(await tagsController.addTagsList(req)))
    }
}

module.exports = tagsRouter