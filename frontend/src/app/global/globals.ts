import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {map, tap} from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class Globals {
    readonly backendUri: string = 'http://localhost:8080/api/v1';

    openPdf(blob: Observable<any>) {
        return blob.pipe(
            map(response => new Blob([response], {type: 'application/pdf'})),
            tap(bl => window.open(URL.createObjectURL(bl), '_blank')),
            // catchError(err => {console.log(err); return of(); })
        );
    }
}
