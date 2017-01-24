const container = new (require('./src/models/Container'));
const express   = require('express');
const app       = express();

container.addService('system.logger', './src/models/Logger', []);
container.addService('=config', './src/config', []);
container.addService('=app', app, []);

const server = require('http').Server(app);
container.addService('=server', server, []);

/* Создаем модули с зависимостями */
container.addService('Storage', './src/services/Storage.js', ['config']);
container.addService('Parser', './src/services/Parser.js', ['config', 'Storage']);
container.addService('Router', './Router.js', ['config', 'app', 'Storage']);

/* Запускаем модули */
container.warming();

const config = container.getService('config');

server.listen(config.port, () => {
    console.info('Project working on http://localhost:' + config.port);
});
