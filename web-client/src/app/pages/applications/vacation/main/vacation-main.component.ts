import {Component, OnInit} from '@angular/core';
import {Title} from '@angular/platform-browser';

@Component({
    selector: 'vacation-main',
    templateUrl: './vacation-main.component.html',
    styleUrls: ['./vacation-main.component.scss']
})
export class VacationMainComponent implements OnInit {

    constructor(private titleService: Title) {
        this.titleService.setTitle('График отпусков');
    }

    ngOnInit(): void {

    }

}
