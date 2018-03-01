import {MdbLinksComponent} from './links.component';
import {MdbLogoComponent} from './logo.component';
import {NavbarService} from './navbar.service';
import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {MdbNavbarComponent} from './navbar.component';
import {MdbNavlinksComponent} from './navlinks.component';

@NgModule({
	imports: [CommonModule],
	declarations: [MdbNavbarComponent, MdbLinksComponent, MdbLogoComponent, MdbNavlinksComponent],
	exports: [MdbNavbarComponent, MdbLinksComponent, MdbLogoComponent, MdbNavlinksComponent],
	providers: [NavbarService]
})
export class MdbNavbarModule {
}
