import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Show} from '../dtos/show';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';

@Injectable({
    providedIn: 'root'
})
export class ShowService {

    private showBaseUri: string = this.globals.backendUri + '/shows';
    private eventBaseUri: string = this.globals.backendUri + '/events';

    constructor(private httpClient: HttpClient, private globals: Globals) {
    }

    /**
     * Loads all shows from the backend
     */
    getShow(): Observable<Show[]> {
        return this.httpClient.get<Show[]>(this.showBaseUri);
    }

    /**
     * Loads specific show from the backend
     * @param id of show to load
     */
    getShowById(id: number): Observable<Show> {
        console.log('Load show details for ' + id);
        return this.httpClient.get<Show>(this.showBaseUri + '/' + id);
    }

    /**
     * Persists show to the backend
     * @param show to persist
     */
    createShow(show: Show): Observable<Show> {
        console.log('Create show');
        return this.httpClient.post<Show>(this.showBaseUri, show);
    }

    createShowForEvent(show: Show, eventId: number): Observable<Show> {
        console.log('Create show for event');
        console.log(show);
        console.log(eventId);
        console.log(this.httpClient.get<Show>(this.showBaseUri + '/' + eventId));
        return this.httpClient.post<Show>(this.eventBaseUri + '/' + eventId + '/shows', show);
    }

    getShowsByEventId(eventId: number): Observable<Show[]> {
        console.log(this.eventBaseUri + '/' + eventId + '/shows');
        return this.httpClient.get<Show[]>(this.eventBaseUri + '/' + eventId + '/shows');
    }
}
