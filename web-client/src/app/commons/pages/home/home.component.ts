import {Component, ElementRef, HostListener, OnInit, Renderer2, ViewChild} from '@angular/core';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

    @ViewChild('container')
    container: ElementRef;

    fixFooter: boolean;

    constructor(private _render: Renderer2) {

	}

    ngOnInit() {
        this.onResize(undefined);
    }

    @HostListener('window:resize', ['$event'])
    onResize(event: any) {
    	if (window.innerHeight > this.container.nativeElement.offsetHeight) {
    	    this.fixFooter = true;
		} else {
            this.fixFooter = false;
        }
	}

}
