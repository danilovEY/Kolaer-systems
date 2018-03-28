import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AuthModule} from './modules/auth/auth.module';

@NgModule({
	imports: [
		CommonModule,
		AuthModule
	],
	exports: [
		CommonModule,
        AuthModule
	]
})
export class AppCoreModule {

}
