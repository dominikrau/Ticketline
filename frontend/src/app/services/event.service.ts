import {Injectable} from '@angular/core';
import {Globals} from '../global/globals';
import {HttpClient} from '@angular/common/http';
import {Page} from '../dtos/page';
import {Observable} from 'rxjs';
import {Event} from '../dtos/event';

@Injectable({
    providedIn: 'root'
})
export class EventService {

    private readonly eventBaseUri: string = `${this.globals.backendUri}/events`;

    private readonly searchBaseUri: string = this.eventBaseUri + '/search';

    private readonly chartsBaseUri: string = this.eventBaseUri + '/charts';

    constructor(private globals: Globals, private httpClient: HttpClient) {
    }

    search(searchQuery: any, pageSize: number, pageNumber: number): Observable<Page<Event>> {
        for (const param in searchQuery) {
            if (searchQuery.hasOwnProperty(param)) {
                searchQuery[param] = searchQuery[param] ? searchQuery[param] : '';
            }
        }
        return this.httpClient.get<Page<Event>>(this.searchBaseUri, {
            params: {
                size: pageSize.toString(),
                page: pageNumber.toString(),
                ...searchQuery
            }
        });
    }

    getTopTen(searchQuery: any): Observable<Event[]> {
        return this.httpClient.get<Event[]>(this.chartsBaseUri, {
            params: {
                ...searchQuery
            }
        });
    }

    /**
     * Loads all events from the backend
     */
    getEvents(): Observable<Event[]> {
        return this.httpClient.get<Event[]>(this.eventBaseUri);
    }

    /**
     * Loads specific event from the backend
     * @param id of event to load
     */
    getEventById(id: number): Observable<Event> {
        return this.httpClient.get<Event>(this.eventBaseUri + '/' + id);
    }

    /**
     * Persists event to the backend
     * @param event to persist
     */
    createEvent(event: Event): Observable<Event> {
        return this.httpClient.post<Event>(this.eventBaseUri, event);
    }

}
