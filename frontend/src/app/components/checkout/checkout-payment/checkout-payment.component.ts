import {Component, OnInit} from '@angular/core';
import {CheckoutService} from '../../../services/checkout.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Show} from '../../../dtos/show';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
    selector: 'app-checkout-payment',
    templateUrl: './checkout-payment.component.html',
    styleUrls: ['./checkout-payment.component.css']
})
export class CheckoutPaymentComponent implements OnInit {
    show: Show;
    paymentForm: FormGroup;

    paymentProviders;

    constructor(private checkoutService: CheckoutService,
                private router: Router,
                private route: ActivatedRoute,
                private formBuilder: FormBuilder) {
    }

    ngOnInit(): void {
        this.checkoutService.addEnabledUrl('/payment');
        this.show = this.checkoutService.getShow();
        this.paymentProviders = this.checkoutService.getPaymentProviders();
        // console.log(this.checkoutService.getOrder());
        /*this.paymentForm = this.formBuilder.group({
            payment: this.formBuilder.group({
                method: ['', [Validators.required]],
                name: [''],
                number: ['']
            })
        });*/
        this.paymentForm = this.formBuilder.group({
            payment: this.formBuilder.group({
                method: ['', [Validators.required]],
                name: [''],
                number: ['']
            })
        });
        if (this.checkoutService.getOrder() && this.checkoutService.getOrder().payment) {
            this.paymentForm.patchValue(this.checkoutService.getOrder());
        }
    }

    continueToConfirmation() {
        this.paymentForm.controls.payment.markAllAsTouched();
        if (this.paymentForm.valid) {
            console.log(this.paymentForm.value);
            this.checkoutService.updateOrder(this.paymentForm.value);
            this.router.navigate(['../confirm'], {relativeTo: this.route});
        }
        return null;
    }

    isInvalid() {
        return this.paymentForm.get('payment')['controls'].method.touched && this.paymentForm.get('payment')['controls'].method.errors;
    }
}
