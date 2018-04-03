import {Component, ElementRef, OnInit, Renderer2, ViewChild} from '@angular/core';
import {FooterComponent} from '../../modules/footer/footer.component';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

    @ViewChild('container')
    container: ElementRef;

    @ViewChild(FooterComponent)
    footer: FooterComponent;

    constructor(private _render: Renderer2) {

	}

    ngOnInit() {

    }

}
