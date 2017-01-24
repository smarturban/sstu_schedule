const httpRequest = require('http_request');
const parseXML = require('xml2js').parseString;
let config, storage, authKey;

class Parser{
    constructor(localConfig, localStorage){
        authKey = "Basic " + new Buffer(localConfig.basicAuth.login + ":" + localConfig.basicAuth.password).toString('base64');
        config  = localConfig;
        storage = localStorage;

        this.getFacultyList().then((response) => {
            response.map((item) => {
                this.getDataForFaculty(item.faculty_id).then(() => {

                }).catch((e) => {
                    console.error(e);
                });
            });
        }).catch((err) => {
            console.error(err);
        });
    }

    getFacultyList(){
        return new Promise((resolve, reject) => {

            httpRequest.request(config.basicURL, {
                method: "GET",
                headers: {
                    Authorization: authKey
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
                                    faculty_id: item.$.id,
                                });
                            });
                            storage.faculties = result;
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

    getDataForFaculty(faculty){
        return new Promise((resolve, reject) => {

            httpRequest.request(config.basicURL + config.prefix + faculty, {
                method: "GET",
                headers: {
                    Authorization: authKey
                }
            }).then((response) => {
                const code = response.getCode();
                const body = response.getBody();

                if (code >= 200 && code <= 299) {
                    parseXML(body, (err, document) => {
                        try {
                            const groups = [];
                            document.schedule.group.map((item) => {
                                groups.push({
                                    group_name: item.$.number,
                                    group_id: faculty + "_" + item.$.number
                                });
                                item.day.map((day) => {
                                    // console.dir(day);
                                });
                            });
                            storage.groups.set(faculty, groups);
                            resolve(groups);
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
}
module.exports = Parser;