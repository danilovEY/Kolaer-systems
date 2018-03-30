import {Component, OnInit} from '@angular/core';
import {Logger} from 'angular2-logger/core';

@Component({
	selector: 'app-dashboard',
	templateUrl: './dashboard.component.html',
	styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

	constructor(private _logger: Logger) {

	}

	ngOnInit() {

	}

}
