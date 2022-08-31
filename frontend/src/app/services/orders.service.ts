import {Injectable} from '@angular/core';
import {Globals} from '../global/globals';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Order} from '../dtos/order';
import {Ticket} from '../dtos/ticket';
import {Page} from '../dtos/page';


@Injectable({
    providedIn: 'root'
})
export class OrdersService {

    private ordersBaseUri: string = this.globals.backendUri + '/orders';


    constructor(private httpClient: HttpClient, private globals: Globals) {
    }

    /**
     * loads all orders from the backend for currently logged in user
     */
    getOrders(pageSize: number, pageNumber: number): Observable<Page<Order>> {
        console.log('Load orders for current user');
        return this.httpClient.get<Page<Order>>(this.ordersBaseUri + '/user', {
            params: {
                size: pageSize.toString(),
                page: pageNumber.toString()
            }
        });
    }


    getTickets(id: number): Observable<Ticket[]> {
        console.log('Load tickets for this order' + id);
        return this.httpClient.get<Ticket[]>(this.ordersBaseUri + '/' + id + '/tickets');
    }

    printTickets(orderId: number) {
        return this.globals.openPdf(this.httpClient.get(`${this.ordersBaseUri}/${orderId}/tickets/pdf`, {responseType: 'blob'}));
    }

    printReceipt(orderId: number) {
        return this.globals.openPdf(this.httpClient.get(`${this.ordersBaseUri}/${orderId}/receipt`, {responseType: 'blob'}));
    }
}




