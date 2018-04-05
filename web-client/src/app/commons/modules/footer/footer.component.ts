import {Component, ElementRef, HostBinding, Input, OnInit, ViewChild} from '@angular/core';

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

    @HostBinding('class.isFixFooter')
    @Input()
    fixFooter: boolean = false;

    @HostBinding('class.notFixFooter')
    notFixFooter: boolean = !this.fixFooter;

	constructor() {
	}

	ngOnInit() {
        this.footerHeight = this.footerElement.nativeElement.offsetHeight;
	}

}
