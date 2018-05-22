import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {KolpassService} from '../kolpass.service';
import {ToasterConfig} from "angular2-toaster";

@Component({
    selector: 'repository-detailed',
    styleUrls: ['./repository-detailed.component.scss'],
    templateUrl: './repository-detailed.component.html'
})
export class RepositoryDetailedComponent implements OnInit {


    loading: boolean = false;
    config: ToasterConfig = new ToasterConfig({
        positionClass: 'toast-top-right',
        timeout: 5000,
        newestOnTop: true,
        tapToDismiss: true,
        preventDuplicates: false,
        animation: 'fade',
        limit: 5,
    });

    constructor(private activatedRoute: ActivatedRoute,
                private kolpassService: KolpassService) {

    }
    
    ngOnInit(): void {
    }
    
}
