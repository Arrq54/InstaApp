const bcrypt = require('bcryptjs');
const model = require("./model.js")
const formidable = require('formidable');
const jwt = require('jsonwebtoken');
const { log } = require('console');

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
    
                new model.User(id, fields.name, fields.lastName, fields.email, false, fields.password)
                resolve({token: `http://${ip}:${process.env.APP_PORT}/api/user/confirm/${token}`})
            })

        })
       
    },
    async confirm(token){
        return new Promise(async (resolve, reject) => {
            try {
                let decoded = await jwt.verify(token, process.env.JWT_KEY)
                model.usersArray.find(i=>{return i.id == decoded.id}).verify()
                
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
                let user = model.usersArray.find(i=>{return i.name == fields.username})
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
                        resolve({success: true, token: token, username: user.name})
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
    }
    
}