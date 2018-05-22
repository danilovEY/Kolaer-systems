import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {KolpassService} from '../kolpass.service';

@Component({
    selector: 'repository-detailed',
    styleUrls: ['./repository-detailed.component.scss'],
    templateUrl: './repository-detailed.component.html'
})
export class RepositoryDetailedComponent implements OnInit {
    constructor(private activatedRoute: ActivatedRoute,
                private kolpassService: KolpassService) {

    }
    
    ngOnInit(): void {
    }
    
}
