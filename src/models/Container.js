const EventEmitter = require('events');

/**
 * Dependency injection container
 * Usage:
 *  adding service with no dependencies
 *      .addService("serviceName", "path_to_module_file")
 *      .addService("serviceName2", ServiceClass)
 * 
 *  adding another service with dependency
 *      .addService("serviceName3", "path_to_module_file", ["serviceName"])
 *      in result of this container pass to service 3 constructor an entity of service 1
 * 
 *      .addService("serviceName4", ServiceClass, ["serviceName2", "serviceName"])
 *      in result of this container pass to service 4 constructor an entities of service 2 and service 1
 * 
 *  adding previously created service
 *      .addService("=serviceName5", {...})
 *      in result of this service will be available by name serviceName5 and will be used as it was given to container
 * 
 * getting service from container
 *      .getService("serviceName")
 *      after this container creates service entity if needed, pass to its constructor required dependencies and returns
 *      service entity. Future calls will use previously created entity.
 *  
 * Important note:
 *      first service wich app will adds to container need to be a "system.logger"
 *      This service MUST implement method
 *      .log(String message, Integer messageType)
 *      and MUST NOT have any dependencies
 */
class Container extends EventEmitter {
    
    constructor() {
        super();
        
        this.services = {
            '@container': this
        };
        this.servicesConfig = {};
    }
    
    /**
     * Create and return service entity
     * @param {string} serviceName Name used to identify service
     * @returns {Object}
     */
    loadService(serviceName) {

        if (!this.servicesConfig[serviceName]) {
            this.getService('system.logger').log(`Container: Service ${serviceName} not exists`);
            return null;
        }
        let serviceObject = this.servicesConfig[serviceName].service;
        
        if (typeof serviceObject === 'string') {
            serviceObject = require('./../../' + serviceObject);
        }
        
        if (typeof serviceObject === 'function' && this.servicesConfig[serviceName].createInstance) {
            const diServices = [null];
            for (const i in this.servicesConfig[serviceName].parameters) {
                if (this.servicesConfig[serviceName].parameters.hasOwnProperty(i)) {
                    diServices.push(this.getService(this.servicesConfig[serviceName].parameters[i]));
                }
            }
            serviceObject = new (Function.prototype.bind.apply(serviceObject, diServices));
        }
        
        this.services[serviceName] = serviceObject;
        this.getService('system.logger').log(`Container: Service ${serviceName} loaded successfully`);
        return serviceObject;
    }
    
    /**
     * Adds service to service container
     * @param {string} serviceName Name used to identify service
     * @param {string|Object} service Path to service module file or service object
     * @param {string[]} parameters required service dependencies
     */
    addService(serviceName, service, parameters) {
        let createInstance = true;
        if (serviceName[0] === '=') {
            serviceName = serviceName.substr(1);
            createInstance = false;
        }
        this.servicesConfig[serviceName] = {
            service: service,
            parameters: parameters,
            createInstance: createInstance
        };
        this.getService('system.logger').log(`Container: Service ${serviceName} added to DI container`);
    }
    
    /**
     * Returns service entity by service name
     * @param {string} serviceName Name used to identify service
     * @returns {Object}
     */
    getService(serviceName) {
        const service = this.services[serviceName];
        return service ? service : this.loadService(serviceName);
    }

    /**
     * Cache warming. Creates all service instances.
     */
    warming() {
        for(const serviceName in this.servicesConfig) {
            if (this.servicesConfig.hasOwnProperty(serviceName)){
                this.getService(serviceName);
            }
        }
    }
    
}

module.exports = Container;