import {Component, HostBinding, Input, OnInit} from '@angular/core';

@Component({
	selector: 'app-footer',
	templateUrl: './footer.component.html',
	styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

    @HostBinding('class.isFixFooter')
	@Input()
	fixFooter: boolean = false;

	constructor() {
	}

	ngOnInit() {

	}

}
