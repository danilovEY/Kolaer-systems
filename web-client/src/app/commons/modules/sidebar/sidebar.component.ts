import {Component, OnInit} from '@angular/core';
import {AuthenticationRestService} from '../auth/authentication-rest.service';

@Component({
	selector: 'app-sidebar',
	templateUrl: './sidebar.component.html',
	styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
	constructor(public authenticationService: AuthenticationRestService) {
	}

	ngOnInit() {

	}

}
