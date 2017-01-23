class Storage{
    constructor(config) {
        this.faculties = [];
        this.groups    = new Map();
        this.schedule  = new Map();
    }
}
module.exports = Storage;