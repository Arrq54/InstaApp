const bcrypt = require('bcryptjs');

const pass = "moje tajne hasło"

const encryptPass = async (password) => {

    let encryptedPassword = await bcrypt.hash(password, 10);
    console.log(encryptedPassword);
}

const decryptPass = async (userpass, encrypted) => {

    let decrypted = await bcrypt.compare(userpass, encrypted)
    console.log(decrypted);

}





//  encryptPass(pass)

 decryptPass(pass, "$2a$10$ZlJULtE/6D1ldG4xHJVpp.YQfuplxXmaKIdQZ3iGVLQ60vi0ykpKS")