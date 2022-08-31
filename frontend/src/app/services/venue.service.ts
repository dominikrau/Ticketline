import {Injectable} from '@angular/core';
import {Globals} from '../global/globals';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Venue} from '../dtos/venue';

@Injectable({
    providedIn: 'root'
})
export class VenueService {

    private venueBaseUri: string = this.globals.backendUri + '/venues';

    constructor(private httpClient: HttpClient, private globals: Globals) {
    }

    createVenue(venue: Venue): Observable<Venue> {
        console.log('Create venue');
        return this.httpClient.post<Venue>(this.venueBaseUri, venue);
    }

    searchVenue(search: string): Observable<Venue[]> {
        return this.httpClient.get<Venue[]>(`${this.venueBaseUri}/search`, {
            params: {
                name: search
            }
        });
    }

}
