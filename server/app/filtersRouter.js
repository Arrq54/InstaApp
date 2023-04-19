const filtersController = require("./filtersController.js")
//TO DO - POPRAWIC KODY (404 ITP)

const filtersRouter = async (req, res) => {
    res.writeHead(200, { "content-type": "application/json;charset=utf-8" })

    
    if(req.url.match(/\/api\/filters\/metadata\/([0-9]+)/) && req.method == "GET"){



        res.end( JSON.stringify(await filtersController.getPhotoInfo(req.url.split("/")[4])))


    }else if(req.url == "/api/filters" && req.method == "PATCH"){

        res.end(JSON.stringify(await filtersController.handleFilter(req)))

    }
}

module.exports = filtersRouter