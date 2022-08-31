import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {UserProfile} from '../dtos/user-profile';
import {tap} from 'rxjs/operators';
import {AuthService} from './auth.service';
import {UpdatePassword} from '../dtos/update-password';
import {Page} from '../dtos/page';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    private userBaseUri: string = this.globals.backendUri + '/users';

    private readonly searchBaseUri: string = this.userBaseUri + '/search';

    constructor(private httpClient: HttpClient, private globals: Globals, private authService: AuthService) {
    }

    /**
     * Loads current user from the backend
     */
    getCurrentUser(): Observable<UserProfile> {
        return this.httpClient.get<UserProfile>(this.userBaseUri + '/profile');
    }

    /**
     * Update User Profile and Authentication Token
     * @param userProfile the updated user profile
     */
    editUserProfile(userProfile: UserProfile): Observable<string> {
        return this.httpClient.put(this.userBaseUri + '/profile/edit', userProfile, {responseType: 'text'})
            .pipe(
                tap((authResponse: string) => this.authService.setToken(authResponse))
            );
    }

    /**
     * Update user password and authentication token
     * @param password the passwordobject to update
     */
    updatePassword(password: UpdatePassword): Observable<string> {
        return this.httpClient.put(this.userBaseUri + '/password/update', password, {responseType: 'text'})
            .pipe(
                tap((authResponse: string) => {
                    // console.log(authResponse);
                    this.authService.setToken(authResponse);
                })
            );
    }


    /**
     * Loads user by id from the backend
     * @param id of the user
     */
    getUserById(id: number): Observable<UserProfile> {
        console.log('Load user details');
        return this.httpClient.get<UserProfile>(this.userBaseUri + '/' + id);
    }

    /**
     * Search for user
     * @param searchQuery parameters to search for
     * @param pageSize amount of users to load
     * @param pageNumber number of page to load
     */
    search(searchQuery: any, pageSize: number, pageNumber: number): Observable<Page<UserProfile>> {
        return this.httpClient.get<Page<UserProfile>>(this.searchBaseUri, {
            params: {
                size: pageSize.toString(),
                page: pageNumber.toString(),
                ...searchQuery
            }
        });
    }

    /**
     * Unblock a user by id
     * @param id of the user to unblock
     */
    unblockUser(id: number) {
        console.log('Unblock user');
        return this.httpClient.put<UserProfile>(this.userBaseUri + '/' + id + '/unblock', null);
    }

    /**
     * Block a user by id
     * @param id of the user to block
     */
    blockUser(id: number) {
        console.log('Block user');
        return this.httpClient.put<UserProfile>(this.userBaseUri + '/' + id + '/block', null);
    }

    /**
     * Reset password for user by id
     * @param updatePassword including new password
     * @param id of the user to update password
     */
    resetPasswordForUser(updatePassword: UpdatePassword, id: number) {
        console.log('Reset password for user');
        return this.httpClient.put<UserProfile>(this.userBaseUri + '/' + id + '/reset', updatePassword);
    }

    makeUserAdmin(id: number) {
        console.log('Reset password for user with id ' + id);
        return this.httpClient.put<UserProfile>(this.userBaseUri + '/' + id + '/toadmin', null);
    }

    /**
     * Deletes current user from the backend
     */
  deleteAccount(): Observable<UserProfile> {
      console.log('DELETE ' + this.userBaseUri + '/profile/delete');
      return this.httpClient.delete<UserProfile>(this.userBaseUri + '/profile/delete');
  }
}
