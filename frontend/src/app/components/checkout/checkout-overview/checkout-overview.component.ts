import {Component, OnInit} from '@angular/core';
import {CheckoutService} from '../../../services/checkout.service';
import {Show} from '../../../dtos/show';

@Component({
    selector: 'app-checkout-overview',
    templateUrl: './checkout-overview.component.html',
    styleUrls: ['./checkout-overview.component.css']
})
export class CheckoutOverviewComponent implements OnInit {

    constructor(private checkoutService: CheckoutService) {
    }

    tickets: any[];
    order;
    show: Show;

    ngOnInit(): void {
        this.order = this.checkoutService.getOrder();
        this.show = this.checkoutService.getShow();
        this.tickets = this.orderToTickets(this.order);
        console.log(this.order);
    }

    private orderToTickets(order: any) {
        const standingTickets = [];

        order.standing.map(
            standing => {
                standingTickets.push(
                    ...Array(standing.amount).fill({
                        sector: this.checkoutService.getSector(standing.id),
                        show: this.show,
                    })
                );
            }
        );

        const tickets = order.seats.map(
            seat => ({
                // show: this.show.event.name,
                sector: this.checkoutService.getSectorFromSeat(seat),
                seat: seat,
                show: this.show
            })).concat(standingTickets);
        console.log(tickets);
        return tickets;
    }

    calculatePrice() {
        return this.checkoutService.calculatePrice();
    }

    getFullSeatById(id, sectorId) {
        return this.checkoutService.getSector(sectorId).seats.find(x => x.id === id);
    }

    getNeatSeatById(id, sectorId) {
        const seat = this.getFullSeatById(id, sectorId);
        return `row: ${seat.y} seat: ${seat.x}`;
    }
}
