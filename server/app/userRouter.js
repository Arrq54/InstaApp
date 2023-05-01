const userController = require("./userController")
const userRouter = async (req, res) => {
    res.writeHead(200, { "content-type": "application/json;charset=utf-8" })

    if(req.url == "/api/user/register" && req.method == "POST"){

        res.end(JSON.stringify(await userController.register(req)))

    }else if(req.url.match(/\/api\/user\/confirm\/(.*?)/) && req.method == "GET"){

        res.end(JSON.stringify(await userController.confirm(req.url.split("/")[4])))

    }else if (req.url == "/api/user/auth"){
        res.end(JSON.stringify(await userController.auth(req)))
    }
    else if(req.url == "/api/user/login" && req.method == "POST"){
        let obj = await userController.login(req)
        res.end(JSON.stringify(obj))

    }
}

module.exports = userRouter