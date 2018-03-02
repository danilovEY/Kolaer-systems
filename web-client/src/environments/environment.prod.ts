import {Level} from 'angular2-logger/core';

export const environment = {
	production: true,
	logger: {
		level: Level.WARN
	}
};

export const publicServerUrl: string = 'http://aerdc02:8080/kolaer-web/rest';
