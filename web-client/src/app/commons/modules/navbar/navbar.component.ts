import {Component, OnInit} from '@angular/core';
import {ROUTES} from '../sidebar/sidebar-routes.config';
import {Location} from '@angular/common';

@Component({
	selector: 'app-navbar',
	templateUrl: './navbar.component.html',
	styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
	private listTitles: any[];
	location: Location;

	constructor(location: Location) {
		this.location = location;
	}

	ngOnInit() {
		this.listTitles = ROUTES.filter(listTitle => listTitle);
	}

}
