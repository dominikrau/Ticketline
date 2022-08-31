import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Artist} from '../dtos/artist';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';

@Injectable({
    providedIn: 'root'
})
export class ArtistService {

    private artistBaseUri: string = this.globals.backendUri + '/artists';

    constructor(private httpClient: HttpClient, private globals: Globals) {
    }

    searchArtist(searchTerm: string): Observable<Artist[]> {
        return this.httpClient.get<Artist[]>(this.artistBaseUri, {
            params: {
                name: searchTerm
            }
        });
    }

    saveArtist(artist: Artist): Observable<Artist> {
        return this.httpClient.post<Artist>(this.artistBaseUri, artist);
    }

}
