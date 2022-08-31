import {Component, OnInit} from '@angular/core';
import {CheckoutService} from '../../../services/checkout.service';
import {UserService} from '../../../services/user.service';
import {UserProfile} from '../../../dtos/user-profile';
import {NotificationService} from '../../../services/notification.service';
import {Router} from '@angular/router';

@Component({
    selector: 'app-checkout-confirmation',
    templateUrl: './checkout-confirmation.component.html',
    styleUrls: ['./checkout-confirmation.component.css']
})
export class CheckoutConfirmationComponent implements OnInit {

    user: UserProfile;
    order: any;
    paymentProvider;

    constructor(private checkoutService: CheckoutService,
                private userService: UserService,
                private notificationService: NotificationService,
                private router: Router) {
    }

    ngOnInit(): void {
        this.checkoutService.addEnabledUrl('/confirm');
        this.userService.getCurrentUser().subscribe(
            user => this.user = user,
            error => this.notificationService.error(error)
        );
        this.order = this.checkoutService.getOrder();
        this.paymentProvider = this.checkoutService.getPaymentProviders().find(x => x.value === this.order.payment.method);
    }

    checkout() {
        this.checkoutService.checkout().subscribe(
            () => {
                this.notificationService.success('Order placed successfully!');
                this.router.navigate(['orders']);
            },
            error => this.notificationService.error(error)
        );
    }
}
