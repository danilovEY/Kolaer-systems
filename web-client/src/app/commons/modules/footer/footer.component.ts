import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';

@Component({
	selector: 'app-footer',
	templateUrl: './footer.component.html',
	styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

    @Input()
    footerHeight: number = 0;

    @ViewChild('footer')
	footerElement: ElementRef;

    @Input()
    fixFooter: boolean = true;

	constructor() {
	}

	ngOnInit() {
        this.footerHeight = this.footerElement.nativeElement.offsetHeight;
	}

}
