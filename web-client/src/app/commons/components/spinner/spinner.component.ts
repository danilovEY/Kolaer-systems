import {Component, Input} from '@angular/core';

@Component({
    selector: 'spinner',
    styleUrls: ['./spinner.component.scss'],
    template: `
        <div *ngIf="show" [ngClass]="{'spinner-large': large, 'spinner-medium': medium,'spinner-small': small}"></div>
    `
})
export class SpinnerComponent {
    @Input() show: boolean = false;
    @Input() large: boolean = false;
    @Input() medium: boolean = false;
    @Input() small: boolean = false;
}
