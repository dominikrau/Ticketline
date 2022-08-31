import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Event} from '../dtos/event';

@Injectable({
  providedIn: 'root'
})
export class EventCreatorService {

    private showBaseUri: string = this.globals.backendUri + '/events/create';

    constructor(private httpClient: HttpClient, private globals: Globals) { }

    /**
     * Persists event to the backend
     * @param event to persist
     */
    createEvent(event: Event): Observable<Event> {
        console.log('Create event');
        return this.httpClient.post<Event>(this.showBaseUri, event);
    }
}
