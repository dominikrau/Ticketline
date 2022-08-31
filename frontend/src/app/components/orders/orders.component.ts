import {Component, OnInit} from '@angular/core';
import {Order} from '../../dtos/order';
import {OrdersService} from '../../services/orders.service';
import {NotificationService} from '../../services/notification.service';
import {Page} from '../../dtos/page';
import {CheckoutService} from '../../services/checkout.service';
import {Show} from '../../dtos/show';
import {Router} from '@angular/router';

@Component({
    selector: 'app-orders',
    templateUrl: './orders.component.html',
    styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {

    public isCollapsed = true;
    orders: Page<Order>;
    pageSize: number = 5;
    pageNumber: number = 1;
    activeOrderId: number;

    constructor(private ordersService: OrdersService,
                private notificationService: NotificationService,
                private checkoutService: CheckoutService,
                private router: Router) {
    }

    ngOnInit(): void {
        this.getOrders();
    }
    getOrders() {
        this.ordersService.getOrders(this.pageSize, this.pageNumber - 1).subscribe(
            orders => this.orders = orders,
            error => this.notificationService.error(error)
        );
    }
    toggleActiveOrder(orderId: number) {
        this.activeOrderId = (this.activeOrderId === orderId) ? null : orderId;
    }

    changePage(page: number) {
        this.pageNumber = page;
        this.getOrders();
    }

    printTicket(orderId: number) {
        this.ordersService.printTickets(orderId).subscribe(
            () => this.notificationService.success('success!'),
            error => this.notificationService.error(error)
        );
    }

    printReceipt(orderId: number) {
        this.ordersService.printReceipt(orderId).subscribe(
            () => this.notificationService.success('success!'),
            error => this.notificationService.error(error)
        );
    }

    buyReserved(show: Show, order: Order) {
        this.checkoutService.setShow(show);
        this.checkoutService.setBuyingReservedTickets(true);
        this.ordersService.getTickets(order.orderId).subscribe(
            tickets => {
                if (tickets.length) {
                    this.checkoutService.setShow(tickets[0].show);
                }
                console.log(tickets);
                const seatIds = tickets.filter(
                    ticket => ticket.sector.type === 'sitting'
                    ).map(ticket => ticket.seat.id);

                const areas = tickets.filter(
                    ticket => ticket.sector.type === 'standing'
                ).map(
                    ticket => ({
                        id: ticket.sector.id,
                        amount: 1
                    })
                );
                const areasUnique = [];
                areas.forEach(area => {
                    if (areasUnique.find(x => x.id === area.id)) {
                        areasUnique.find(x => x.id === area.id).amount++;
                    } else {
                        areasUnique.push(area);
                    }
                });
                console.log(areasUnique);
                const purchaseIntent = {
                    showId: show.showId,
                    seats: seatIds,
                    standing: areas,
                    orderId: order.orderId
                };
                this.checkoutService.updateOrder(purchaseIntent);
                this.checkoutService.clearEnabledUrls();
                this.router.navigate(['/checkout/' + show.showId + '/payment']);
            }
        );

    }

    getNeatPaymentMethod(paymentMethod: string) {
        return this.checkoutService.getPaymentProviders().find(x => x.enum === paymentMethod)?.name;
    }
}
