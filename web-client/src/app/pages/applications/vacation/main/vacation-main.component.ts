import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'vacation-main',
    templateUrl: './vacation-main.component.html',
    styleUrls: ['./vacation-main.component.scss']
})
export class VacationMainComponent implements OnInit {
    cols: any[];

    ngOnInit(): void {
        this.cols = [
            { field: 'vin', header: 'Vin' },
            {field: 'year', header: 'Year' },
            { field: 'brand', header: 'Brand' },
            { field: 'color', header: 'Color' }
        ];
    }

}
