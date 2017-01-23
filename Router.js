class Router{
    constructor(config, app, cache){

        app.get('/get_faculties', (req, res) => {
            res.json({
                faculties: cache.faculties
            });
        });

    }
}
module.exports = Router;