class Router{
    constructor(config, app, storage){

        app.get('/get_faculties', (req, res) => {
            res.json({
                faculties: storage.faculties
            });
        });

        app.get('/get_groups', (req, res) => {
            res.json({
                groups: storage.groups.get(req.query.faculty_id)
            });
        });

        app.get('/get_schedule', (req, res) => {
            res.json({
                groups: storage.schedule.get(req.query.group_id)
            });
        });
    }
}
module.exports = Router;