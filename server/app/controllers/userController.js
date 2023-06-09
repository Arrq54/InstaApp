const bcrypt = require('bcryptjs');
const model = require("../model.js")
const formidable = require('formidable');
const jwt = require('jsonwebtoken');
const fs = require('fs');
const path = require('path');
const { log } = require('console');
const dbControllerUsers = require('./dbControllerUsers.js');
const mainPath = path.join(__dirname, "../../");
const form = formidable({ multiples: true });
const  generateId = ()=>{
    return String(Date.now()) +Math.floor(Math.random() * 25001);
}
module.exports = { 
    async register(req){
        return new Promise((resolve, reject) => {
            const ip = Object.values(require('os').networkInterfaces()).reduce((r, list) => r.concat(list.reduce((rr, i) => rr.concat(i.family === 'IPv4' && !i.internal && i.address || []), [])), [])[0]
            form.parse(req, async function(err, fields, files) {
                if(fields.name && fields.lastName && fields.email && fields.password){
                   fields.password = await bcrypt.hash(fields.password, 10);
                }
                let id = generateId();
                let token = await jwt.sign(
                    {
                        id: id
                    },
                    process.env.JWT_KEY, // powinno być w .env
                    {
                        expiresIn: "60m" // "1m", "1d", "24h"
                    }
                );
    
                new model.User(id, fields.name, fields.lastName, fields.email, false, fields.password, "")


                resolve({token: `http://${ip}:${process.env.APP_PORT}/api/user/confirm/${token}`})
            })

        })
       
    },
    async confirm(token){
        return new Promise(async (resolve, reject) => {
            try {
                let decoded = await jwt.verify(token, process.env.JWT_KEY)


                let user = await dbControllerUsers.getUserById(decoded.id);
                user.confimred = true;
                console.log(user);

                await dbControllerUsers.updateUser(decoded.id, user)
                
                resolve({success: true})
            }
            catch (ex) {
                resolve({success: false})
            }
        })
        
    },
    async login(req){
        return new Promise((resolve, reject) => {
           
            form.parse(req, async function(err, fields, files) {
                let user = await dbControllerUsers.getUserByName(fields.username);
                if(user){
                    if(await bcrypt.compare(fields.password, user.password)){
                        let token = await jwt.sign(
                            {
                                id: user.id
                            },
                            process.env.JWT_KEY, // powinno być w .env
                            {
                                expiresIn: "60d" // "1m", "1d", "24h"
                            }
                        );
                        resolve({success: true, token: token, username: user.name, id: user.id})
                    }else{
                        resolve({success: false, token: ""})
                    }
                }else{
                    resolve({success: false, token: ""})
                }
            })
        })
    },
    auth: (req)=>{
        return new Promise(async (resolve, reject) => {
            if(req.headers.authorization && req.headers.authorization.startsWith("Bearer")){
                // czytam dane z nagłowka
                let token = req.headers.authorization.split(" ")[1]
                let decoded = await jwt.verify(token, process.env.JWT_KEY)
                resolve({success: true})
             }
             resolve({success: false})
        })
    },
    getInfo: (req)=>{
        return new Promise(async (resolve, reject) => {
            formidable({multiples: true}).parse(req, async function(err, fields, files){
                console.log(fields);
                if(req.headers.authorization && req.headers.authorization.startsWith("Bearer")){
                    // czytam dane z nagłowka
                    let token = req.headers.authorization.split(" ")[1]
                    let decoded = await jwt.verify(token, process.env.JWT_KEY)
                    
                    let id = decoded.id;

                    let user = await dbControllerUsers.getUserById(id);
                    

                    console.log(user);

                    if(user != null){
                        resolve({
                            id: user.id,
                            name: user.name, 
                            lastName: user.lastName,
                            email: user.email,
                            bio: user.bio
                        })
                    }
                 }
                 resolve({success: false})
            })
        })
    },
    update: (req)=>{
        console.log("POST - USER PROFILE UPDATE");
        const form = formidable({ multiples: true });
            return new Promise((resolve, reject) => {
                form.parse(req, async (err, fields, files) => {
                    let userToEdit = await dbControllerUsers.getUserById(fields.id);
                    if(fields.bio != undefined){
                        userToEdit.bio = fields.bio;
                    }
                    if(fields.email != undefined){
                        userToEdit.email = fields.email;
                    }
                    if(fields.lastName != undefined){
                        userToEdit.lastName = fields.email
                    }
                    let dir = `./uploads/profile_pictures`
                    if (!fs.existsSync(dir)){
                        fs.mkdirSync(dir);
                    } 



                    let file = files.file;
 
                    try {
                        let newFileName = file.path.split("\\");
                        newFileName = newFileName[newFileName.length - 1];

                        let ext = file.name.split(".")[1]

                        var newPath =  mainPath +"/"+dir+ '/'+fields.id + "."+ext
        
                        var rawData = fs.readFileSync(file.path)

        
                        fs.writeFile(newPath, rawData, function(err){
                            if(err){
                                console.log(err);
                                reject("file system error")
                            }else{
                                fs.stat(newPath, async (err, stats) => {
                                    if(err){
                                        console.log(err);
                                        reject("file system error")
                                    }else{
                                        newPath = "/uploads" + newPath.split("uploads")[1]

                                        await dbControllerUsers.updateUser(userToEdit.id, userToEdit);


                                        resolve({
                                            originalName: file.name,
                                            url: newPath,
                                            album: fields.album,
                                            description: fields.description,
                                            timestamp: stats.birthtime
                                        })
                                    }
                                   
                                })
                                
                            }
                        })
                      } catch (error) {
                        console.log(error);
                        reject("form parser error")
                      }

                    resolve({})
                })
            })
    },updateNoPfp: (req)=>{
        console.log("POST - USER PROFILE UPDATE, NO PFP");

        const form = formidable({ multiples: true });
            return new Promise((resolve, reject) => {
                form.parse(req, async (err, fields, files) => {
                    let userToEdit = await dbControllerUsers.getUserById(fields.id);
                    if(fields.bio != undefined){
                        userToEdit.bio = fields.bio;
                    }
                    if(fields.email != undefined){
                        userToEdit.email = fields.email;
                    }
                    if(fields.lastName != undefined){
                        userToEdit.lastName = fields.email
                    }
                    await dbControllerUsers.updateUser(userToEdit.id, userToEdit);
                    resolve({})
                }) 
            })
    }, 
    async getAllUsers(){
        return new Promise((resolve, reject) => {
            let list = fs.readdirSync(mainPath + "/uploads")
            resolve(list)
        })
    }, 
    async getUsers(query){
        return new Promise((resolve, reject) => {
            let list = fs.readdirSync(mainPath + "/uploads")
            list = list.filter(i=>{return i != "profile_pictures"})
            list = list.filter(i=>{return i.startsWith(query)})
            resolve(list)
        })
    },
    async getBioById(id){
        let user = await dbControllerUsers.getUserById(id);
        return {
            bio: user!=undefined?user.bio:""
        }
    },
    async getBioByName(name){
        let user = await dbControllerUsers.getUserByName(name);

        return {
            bio:user!=undefined?user.bio:""
        }
    }
}