import {Component, OnInit} from '@angular/core';
import {KolpassService} from './kolpass.service';
import 'rxjs/add/operator/do';

@Component({
	selector: 'app-kolpass',
	templateUrl: './kolpass.component.html',
	styleUrls: ['./kolpass.component.scss']
})
export class KolpassComponent implements OnInit {
	constructor(private _kolpassService: KolpassService) {

	}

	ngOnInit() {

	}

}
