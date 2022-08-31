import {Injectable} from '@angular/core';
import {Globals} from '../global/globals';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AuthService} from './auth.service';
import {tap} from 'rxjs/operators';
import {ApplicationUser} from '../dtos/application-user';

@Injectable({
    providedIn: 'root'
})
export class RegistrationService {

    userRoles: string[];
    baseUri = `${this.globals.backendUri}/registration`;

    constructor(private globals: Globals, private http: HttpClient, private authService: AuthService) {
    }

    registerUser(user: ApplicationUser): Observable<string> {
        return this.http.post(this.baseUri, user, {responseType: 'text'}).pipe(
            tap((token: string) => this.authService.setToken(token))
        );
    }

    registerUserAsAdmin(user: ApplicationUser): Observable<string> {
        user.roles = [user.roles.toString()];
        return this.http.post(this.baseUri, user, {responseType: 'text'});
    }

}
