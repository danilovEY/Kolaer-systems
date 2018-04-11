import {AfterContentInit, Component, ElementRef, HostListener, OnInit, Renderer2, ViewChild} from '@angular/core';
import {FooterComponent} from '../../modules/footer/footer.component';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, AfterContentInit {

    @ViewChild('container')
    container: ElementRef;

    @ViewChild(FooterComponent)
    footer: FooterComponent;

    fixFooter: boolean;

    constructor(private _render: Renderer2) {

	}

    ngOnInit() {

    }

    ngAfterContentInit(): void {
        this.onResize(undefined);
    }

    @HostListener('window:scroll', ['$event'])
    onResize(event: any) {
        const footerHeight = this.footer.footerHeight === 0 ? 60 : this.footer.footerHeight;
        if (window.innerHeight > this.container.nativeElement.offsetHeight + footerHeight * 6) {
            this.fixFooter = true;
        } else {
            this.fixFooter = false;
        }
    }
}
