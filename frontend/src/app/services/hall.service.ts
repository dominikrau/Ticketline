import {Injectable} from '@angular/core';
import {Hall} from '../dtos/hall';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';

@Injectable({
    providedIn: 'root'
})
export class HallService {

    private hallBaseUrl: string = this.globals.backendUri + '/halls';

    constructor(private httpClient: HttpClient, private globals: Globals) {
    }

    createHall(hall: Hall): Observable<Hall> {
        return this.httpClient.post<Hall>(this.hallBaseUrl, hall);
    }

    searchHall(venueId: number, search: string): Observable<Hall[]> {
        return this.httpClient.get<Hall[]>(`${this.hallBaseUrl}/search`, {
            params: {
                name: search,
                venueId: venueId.toString()
            }
        });
    }

}
