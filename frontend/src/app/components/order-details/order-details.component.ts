import {Component, Input, OnInit} from '@angular/core';
import {Ticket} from '../../dtos/ticket';
import {NotificationService} from '../../services/notification.service';
import {OrdersService} from '../../services/orders.service';
import {TicketService} from '../../services/ticket.service';

@Component({
    selector: 'app-order-details',
    templateUrl: './order-details.component.html',
    styleUrls: ['./order-details.component.css']
})
export class OrderDetailsComponent implements OnInit {

    @Input('orderId')
    orderId: number;

    @Input('activeOrder')
    activeOrder: number;

    tickets: Ticket[];

    constructor(private ordersService: OrdersService, private ticketService: TicketService,
                private notificationService: NotificationService) {

    }

    ngOnInit(): void {

        this.getTickets();
    }

    getTickets(): void {
        this.ordersService.getTickets(this.orderId).subscribe(
            tickets => {
                this.tickets = tickets;
                console.log(tickets);
            },
            error => this.notificationService.error(error)
        );
    }


    cancelTicket(ticketId: number) {
        this.ticketService.cancelTicket(ticketId).subscribe(
            success => {
                this.getTickets();
                console.log('deleted ticket with id' + ticketId);
                this.notificationService.success('Ticket was successfully cancelled');
            },
            error => this.notificationService.error(error)
        );
    }

    printCancellationInvoice(ticketId: number) {
        this.ticketService.printCancellationInvoice(ticketId).subscribe(
            () => this.notificationService.success('success!'),
            error => this.notificationService.error(error)
        );
    }

    isAvailable(startTime: string) {
        return new Date(startTime) > new Date();
    }

}
