import {Injectable} from '@angular/core';
import {Show} from '../dtos/show';
import {SeatingChartSector} from '../dtos/seating-chart';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {HttpClient} from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class CheckoutService {

    private readonly checkoutBaseUri: string = `${this.globals.backendUri}/orders`;


    show: Show;
    enabledUrls: string[] = [];
    order: any;
    buyingReservedTickets: boolean = false;

    paymentProviders = [
        {
            name: 'PayPal',
            value: 'paypal',
            description: 'Simple and Quick',
            enum: 'PAY_PAL'
        },
        {
            name: 'Credit Card',
            value: 'creditcard',
            description: 'Trusted',
            enum: 'CREDIT_CARD'
        },
        {
            name: 'Bank Transfer',
            value: 'banktransfer',
            description: 'Reliable',
            enum: 'BANK_TRANSFER'
        },
        {
            name: 'Reservation',
            value: 'reservation',
            description: 'Pick up your ticket at least 30 minutes before the show!',
            enum: 'RESERVATION'
        }
    ];

    constructor(private globals: Globals,
                private httpClient: HttpClient) {
    }

    setShow(show: Show) {
        this.show = show;
        if (this.order === undefined) {
            this.order = {};
        }
        this.order.showId = show.showId;
    }

    getShow() {
        return this.show;
    }

    addEnabledUrl(url: string) {
        if (this.enabledUrls.indexOf(url) < 0) {
            this.enabledUrls.push(url);
        }
    }

    clearEnabledUrls() {
        this.enabledUrls = [];
    }

    getPaymentProviders() {
        if (this.buyingReservedTickets) {
            return this.paymentProviders.filter(x => x.value !== 'reservation');
        }
        return this.paymentProviders;
    }

    isDisabled(s: string) {
        return this.enabledUrls.indexOf(s) < 0;
    }

    /**
     * gets a sector by id
     * @param id - the sector id
     */
    getSector(id: number) {
        return this.show.seatingChart.sectors.find(x => x.id === id);
    }

    calculatePrice() {
        return this.show.pricings.reduce((a, pricing) => a + this.getSubTotal(pricing.sector.id), 0);
    }

    /**
     * calculates the subtotal for a given sector id
     * @param id - the sector id
     */
    getSubTotal(id: number) {
        const sector = this.getSector(id);
        if (sector.type === 'sitting') {
            return this.getSeatsForSector(id).length * this.getPriceForSector(id);
        } else if (sector.type === 'standing') {
            return this.getCurrentStandingAmountOfSector(id) * this.getPriceForSector(id);
        }
    }

    updateOrder(value: any) {

        Object.assign(this.order, value);
        console.log(this.order);
    }

    getOrder() {
        return this.order;
    }

    /**
     * gets all selected seats for a given sector
     * @param id - the sector id
     */
    getSeatsForSector(id: number) {
        return this.order.seats.filter(seat => this.getSector(id).seats.filter(x => x.id === seat).length);
    }

    /**
     * returns the amount of places selected for a given standing sector id
     * @param id - the standing sector id
     */
    getCurrentStandingAmountOfSector(id: number) {
        const amount = this.order.standing.find(x => x.id === id)?.amount;
        return (amount !== undefined) ? amount : 0;
    }

    getPriceForSector(id: number) {
        return this.show.pricings.find(x => x.sector.id === id).price;
    }

    /**
     * Gets the sector for a given Seat
     * @param id - the seat id
     */
    getSectorFromSeat(id: number): SeatingChartSector {
        console.log('looking for seat with id: ' + id);
        console.log({sectors: this.show.seatingChart.sectors});
        return this.show.seatingChart.sectors.find(
            sector => (sector.type === 'sitting' && sector.seats.filter(seat => seat.id === id).length)
        );
    }

    /**
     * Send a checkout post request
     */
    checkout(): Observable<any> {
        if (this.buyingReservedTickets) {
            return this.httpClient.put<any>(this.checkoutBaseUri + '/' + this.order.orderId + '/purchase', this.order.payment);
        }
        return this.httpClient.post<any>(this.checkoutBaseUri, this.order);
    }

    setBuyingReservedTickets(b: boolean) {
        this.buyingReservedTickets = b;
    }

    isBuyingReservedTickets() {
        return this.buyingReservedTickets;
    }
}
