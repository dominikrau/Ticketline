import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {Ticket} from '../dtos/ticket';

@Injectable({
    providedIn: 'root'
})
export class TicketService {
    private ticketBaseUri: string = this.globals.backendUri + '/tickets';

    constructor(private httpClient: HttpClient, private globals: Globals) {
    }


    /**
     * Loads specific ticket from the backend
     * @param id of ticket to load
     */
    getTicketById(id: number): Observable<Ticket> {
        console.log('Load ticket details for ' + id);
        return this.httpClient.get<Ticket>(this.ticketBaseUri + '/' + id);
    }

    /**
     * Persists ticket to the backend
     * @param ticket to persist
     */
    createTicket(ticket: Ticket): Observable<Ticket> {
        console.log('Create ticket for show ' + ticket.show.showId);
        console.log(this.ticketBaseUri);
        console.log(ticket);
        return this.httpClient.post<Ticket>(this.ticketBaseUri, ticket);
    }

    /**
     * Cancels ticket with given id
     * @param id of the ticket to be cancelled
     */
    cancelTicket(id: number): Observable<Ticket> {
        console.log('Cancel ticket with id {}', id);
        return this.httpClient.delete<Ticket>(this.ticketBaseUri + '/' + id);
    }

    printCancellationInvoice(id: number): Observable<any> {
        return this.globals.openPdf(this.httpClient.get(`${this.ticketBaseUri}/${id}/cancellation`, {responseType: 'blob'}));
    }

}
