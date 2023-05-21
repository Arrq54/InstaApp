const userController = require("./userController")
const path = require('path')
const fs = require('fs');
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

    }else if(req.url == "/api/user/getInfo" && req.method == "POST"){
        res.end(JSON.stringify(await userController.getInfo(req)))
    }else if(req.url == "/api/user/update" && req.method == "POST"){
        res.end(JSON.stringify(await userController.update(req)))
    }
    else if(req.url.match(/\/api\/user\/pfp\/([0-9]+)/) && req.method == "GET"){
        console.log(req.url.split("/")[4]);
        let path2 = path.resolve(__dirname, `../uploads/profile_pictures/${req.url.split("/")[4]}.jpg`)

        fs.access(path2, fs.F_OK, (err) => {
            if (err) {
                fs.readFile(path.resolve(__dirname, `../uploads/profile_pictures/default.jpg`), function (error, data) {
                    res.writeHead(200, { 'Content-Type': 'image/jpeg' });
                    res.write(data);
                    res.end();
                })
              return
            }
            fs.readFile(path.resolve(path2), function (error, data) {
                res.writeHead(200, { 'Content-Type': 'image/jpeg' });
                res.write(data);
                res.end();
            })
          })
    }
}

module.exports = userRouter