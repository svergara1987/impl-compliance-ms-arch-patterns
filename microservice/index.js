let express = require("express");
let process = require('process');
let os = require('os');
let app = express();

let appName = 'rest-api-1'
let processId = (process.pid) ? process.pid : null;

health = (req, res) => {
    const now = new Date().toISOString();
    return res.send(`${now} ${appName}[pid=${processId}][hostname=${os.hostname()}] is healty`);
}

app.get('/', health);
app.get('/health', health);

const processPort = (process.env.PORT || 3000);
app.listen(processPort, () =>
    console.log(`Example app listening on port ${processPort}!`),
);