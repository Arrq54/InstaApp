const jwt = require('jsonwebtoken');

const createToken = async () => {

    let token = await jwt.sign(
        {
            email: "aaa@yahoo.com",
            anyData: "123"
        },
        "verysecretkey", // powinno być w .env
        {
            expiresIn: "30s" // "1m", "1d", "24h"
        }
    );
    console.log({ token: token });
}


const verifyToken = async (token) => {

    try {
        let decoded = await jwt.verify(token, "verysecretkey")
        console.log({ decoded: decoded });
    }
    catch (ex) {
        console.log({ message: ex.message });
    }
}

// createToken()
 verifyToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImFhYUB5YWhvby5jb20iLCJhbnlEYXRhIjoiMTIzIiwiaWF0IjoxNjgyMzE2Nzg2LCJleHAiOjE2ODIzMTY4MTZ9.FOL2c4ufTsHOA8N7EKhWhleW7foElgJRX6wz3RaK_XI")






createToken()