import {Injectable, OnInit} from '@angular/core';
import {NavigationEnd, Router, UrlTree} from '@angular/router';
import {filter} from 'rxjs/operators';

@Injectable()
export class RouterNavigatorService implements OnInit {
    private activeUrlIndex: number = 1;
    private historyUrl: UrlTree[] = [];
    private prevOrNextRoute: boolean = false;

    constructor(private router: Router) {
        this.ngOnInit();
    }

    ngOnInit() {
        this.router.events
            .pipe(filter(event => event instanceof NavigationEnd))
            .subscribe((urlAfterRedirects: NavigationEnd) => {
                if (!this.prevOrNextRoute) {
                    if (this.activeUrlIndex > 1) {
                        this.historyUrl.splice(this.historyUrl.length - this.activeUrlIndex + 1, this.activeUrlIndex);
                    }

                    this.activeUrlIndex = 1;
                }
                this.historyUrl.push(this.router.parseUrl(urlAfterRedirects.urlAfterRedirects));
            });
    }

    canNextUrl(): boolean {
        return this.activeUrlIndex > 1;
    }

    canPrevUrl(): boolean {
        return this.historyUrl.length > this.activeUrlIndex;
    }

    navigateToPreviousUrl() {
        if (this.canPrevUrl()) {
            this.prevOrNextRoute = true;

            this.router.navigateByUrl(this.historyUrl[this.historyUrl.length - ++this.activeUrlIndex])
                .then(value => {
                    this.prevOrNextRoute = false;
                    this.historyUrl.pop();
                });
        }
    }

    navigateToNextUrl() {
        if (this.canNextUrl()) {
            this.prevOrNextRoute = true;

            this.router.navigateByUrl(this.historyUrl[this.historyUrl.length - --this.activeUrlIndex])
                .then(value => {
                    this.prevOrNextRoute = false;
                    this.historyUrl.pop();
                });
        }
    }
}
