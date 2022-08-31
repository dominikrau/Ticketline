import {Component, OnInit} from '@angular/core';
import {CheckoutService} from '../../../services/checkout.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Show} from '../../../dtos/show';

@Component({
    selector: 'app-checkout-navigation',
    templateUrl: './checkout-navigation.component.html',
    styleUrls: ['./checkout-navigation.component.css']
})
export class CheckoutNavigationComponent implements OnInit {
    show: Show;

    constructor(private checkoutService: CheckoutService,
                private router: Router,
                public route: ActivatedRoute) {
    }

    ngOnInit(): void {
        this.show = this.checkoutService.getShow();
        if (this.show === undefined) {
            this.router.navigate(['/checkout/' + this.route.snapshot.paramMap.get('id')]);
        }
    }

    getCheckoutUrl() {
        return '/checkout/' + this.checkoutService.getShow().showId + '/';
    }


    isDisabled(s: string) {
        return this.checkoutService.isDisabled(s);
    }

    isBuyingReservedTickets() {
        return this.checkoutService.isBuyingReservedTickets();
    }
}
