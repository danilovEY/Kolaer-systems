import {Component, ElementRef, HostListener, OnInit, Renderer2, ViewChild} from '@angular/core';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

    @ViewChild('container')
    container: ElementRef;

    constructor(private _render: Renderer2) {

	}

    ngOnInit() {
    }

    @HostListener('window:resize', ['$event'])
    onResize(event: any) {
    	if (window.screen.height > this.container.nativeElement.offsetHeight) {
		}
	}

}
