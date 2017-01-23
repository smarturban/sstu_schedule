const httpRequest = require('http_request');
const parseXML = require('xml2js').parseString;
let config, cache;

class Parser{
    constructor(localConfig, localCache){
        config = localConfig;
        cache  = localCache;

        this.getFacultyList().then((response) => {

        }).catch((err) => {
            console.error(err)
        });
    }

    getFacultyList(){
        return new Promise((resolve, reject) => {

            httpRequest.request(config.basicURL, {
                method: "GET",
                headers: {
                    "Authorization": "Basic " + new Buffer(config.basicAuth.login + ":" + config.basicAuth.password).toString('base64')
                }
            }).then((response) => {
                const code = response.getCode();
                const body = response.getBody();

                if (code >= 200 && code <= 299) {
                    parseXML(body, (err, document) => {
                        try {
                            const result = [];
                            document.departments.department.map((item) => {
                                result.push({
                                    faculty_name: item.$.name,
                                    faculty_id: "ssu_" + item.$.id,
                                });
                            });
                            cache.faculties = result;
                            resolve(result);
                        } catch(e) {
                            reject(e);
                        }
                    });
                } else {
                    reject("Error! HTTP code: " + code);
                }
            });
        });
    }

    getShe
}
module.exports = Parser;