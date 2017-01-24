class Logger {

    constructor() {
        this.logs = [];

        this.messageType = {
            INFO:    'info',
            WARNING: 'warning',
            ERROR:   'error'
        };
    }

    log(message, messageType){
        if (messageType === undefined) {
            messageType = this.messageType.INFO;
        }

        var logEntity = {
            time: (new Date()).getTime(),
            level: messageType,
            message: message
        };

        this.logs.push(logEntity);

        console.log(message);
    }

    getLog(){
        return this.logs.slice(0);
    }
}

module.exports = Logger;