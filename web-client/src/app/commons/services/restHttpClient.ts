import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {map} from 'rxjs/operator/map';
import {ServerToken} from '../models/server.token';

@Injectable()
export class RestHttpClient {
	private _token: ServerToken;

	constructor(private _httpClient: HttpClient) {

	}

	get<T>(url: string, params?: HttpParams, headers: HttpHeaders = this.getHttpHeader()): Observable<T> {
		return this._httpClient.get<T>(url, {params, headers});
	}

	post<T>(url: string, body?: any, params?: HttpParams, headers: HttpHeaders = this.getHttpHeader()): Observable<T> {
		return this._httpClient.post<T>(url, body, { params, headers });
	}

	put<T>(url: string, body?: any, params?: HttpParams, headers: HttpHeaders = this.getHttpHeader()): Observable<T> {
		return this._httpClient.put<T>(url, body, { params, headers });
	}

	getHttpHeader(): HttpHeaders {
		let httpHeader: HttpHeaders = new HttpHeaders();
		httpHeader = httpHeader.append('Content-Type', 'application/json; charset=utf-8');

		if (this._token) {
			httpHeader = httpHeader.append('x-token', this._token.token);
		}

		return httpHeader;
	}

	getHttpClient(): HttpClient {
		return this._httpClient;
	}

	set token(value: ServerToken) {
		this._token = value;
	}

	get token(): ServerToken {
		return this._token;
	}

}
