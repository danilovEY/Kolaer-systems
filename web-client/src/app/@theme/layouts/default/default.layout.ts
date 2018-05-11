import {Component, OnDestroy} from '@angular/core';
import {
    NbMediaBreakpoint,
    NbMediaBreakpointsService,
    NbMenuService,
    NbSidebarService,
    NbThemeService,
} from '@nebular/theme';

import {Subscription} from 'rxjs/Subscription';
import 'rxjs/add/operator/withLatestFrom';
import 'rxjs/add/operator/delay';

@Component({
    selector: 'default-layout',
    styleUrls: ['./default.layout.scss'],
    templateUrl: './default.layout.html',
})
export class DefaultLayoutComponent implements OnDestroy {
    protected menuClick$: Subscription;

    constructor(protected menuService: NbMenuService,
                protected themeService: NbThemeService,
                protected bpService: NbMediaBreakpointsService,
                protected sidebarService: NbSidebarService) {

        const isBp = this.bpService.getByName('is');
        this.menuClick$ = this.menuService.onItemSelect()
            .withLatestFrom(this.themeService.onMediaQueryChange())
            .delay(20)
            .subscribe(([item, [bpFrom, bpTo]]: [any, [NbMediaBreakpoint, NbMediaBreakpoint]]) => {

                if (bpTo.width <= isBp.width) {
                    this.sidebarService.collapse('menu-sidebar');
                }
            });
    }

    ngOnDestroy() {
        this.menuClick$.unsubscribe();
    }
}
