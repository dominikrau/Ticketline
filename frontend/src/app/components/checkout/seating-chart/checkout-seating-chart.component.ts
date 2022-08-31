import {Component, OnInit} from '@angular/core';
import {CheckoutService} from '../../../services/checkout.service';
import {Show} from '../../../dtos/show';
import {ShowService} from '../../../services/show.service';
import {ActivatedRoute, Router} from '@angular/router';
import {SeatingChartSearchResult, SeatingChartSector, SeatingChartSelection} from '../../../dtos/seating-chart';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
    selector: 'app-checkout',
    templateUrl: './checkout-seating-chart.component.html',
    styleUrls: ['./checkout-seating-chart.component.css']
})
export class CheckoutSeatingChartComponent implements OnInit {
    show: Show;
    seats: any[];
    standing: SeatingChartSector[];
    checkoutForm: FormGroup;
    activeSector: number;

    constructor(
        private checkoutService: CheckoutService,
        private showService: ShowService,
        private route: ActivatedRoute,
        private router: Router,
        private formBuilder: FormBuilder,
    ) {
    }

    ngOnInit(): void {
        this.showService.getShowById(+this.route.snapshot.paramMap.get('id')).subscribe(
            show =>
                this.initShow(show)
        );
        this.checkoutService.addEnabledUrl('/');
        this.checkoutService.setBuyingReservedTickets(false);
    }

    initShow(show: Show) {
        this.show = show;
        this.checkoutService.setShow(this.show);
        this.checkoutForm = this.formBuilder.group({
            seats: this.formBuilder.array([]),
            standing: this.formBuilder.array(
                [] // this.createFormStandingFields() // if we want to initialize the entire array at the start
            )
        });
        // todo: not working correctly yet. Should not clear form when navigating back to seatingchart
        if (this.checkoutService.getOrder()) {
            this.checkoutForm.patchValue(this.checkoutService.getOrder());
            console.log(this.checkoutForm.value);
        }

        this.checkoutForm.valueChanges.subscribe(
            valueChange => {
                console.log(valueChange);
                if (this.checkoutForm.valid) {
                    this.checkoutService.updateOrder(this.checkoutForm.value);
                }
            }
        );
        this.checkoutService.updateOrder(this.checkoutForm.value);
    }

    selected(): SeatingChartSelection {
        return {
            seats: this.checkoutForm.value.seats,
            standing: this.checkoutForm.value.standing.filter(x => x.amount > 0)
        };
    }

    clicked(event: SeatingChartSearchResult[]) {
        if (event.length > 0) {
            const seatOrArea = event[0];
            if (seatOrArea.type !== undefined && seatOrArea.type === 'standing' && this.availableStandingPlaces(seatOrArea.id) > 0) {
                const standing = this.checkoutForm.get('standing') as FormArray;
                const index = this.getFormIndexOfStandingSector(seatOrArea.id);
                if (index < 0) { // standing has not been added to form array yet
                    standing.push(this.formBuilder.group({
                        id: seatOrArea.id,
                        amount: [null, [Validators.max(Math.min(seatOrArea.available, 20)), Validators.min(0)]]
                    }));
                } else {
                    standing.at(index).patchValue({amount: standing.at(index).value.amount + 1});
                }
                this.activeSector = seatOrArea.id;
            }
            if (seatOrArea.available === true && this.checkoutForm.value.seats.filter(x => x === seatOrArea.id).length === 0) {
                const seats = this.checkoutForm.get('seats') as FormArray;
                seats.push(this.formBuilder.control(seatOrArea.id));
                this.activeSector = this.getSectorFromSeat(seatOrArea.id).id;
            } else if (this.selected().seats.indexOf(seatOrArea.id) > -1) {
                this.removeSeat(seatOrArea.id);
            }
        }
    }


    getSectorFromSeat(id: number): SeatingChartSector {
        return this.checkoutService.getSectorFromSeat(id);
    }

    continueToOrder() {
        if (this.checkoutForm.valid) {
            console.log(this.checkoutForm.value);
            this.router.navigate([this.router.url + '/payment']);
        }
    }

    /**
     * gets the price for a given sector id
     * @param id - the sector id
     */
    getPriceForSector(id: number) {
        return this.checkoutService.getPriceForSector(id);
    }

    calculatePrice() {
        return this.checkoutService.calculatePrice();
    }

    getSeatsForSector(id: number) {
        return this.checkoutService.getSeatsForSector(id);
    }

    getFullSeatById(id, sectorId) {
        return this.getSector(sectorId).seats.find(x => x.id === id);
    }

    getNeatSeatById(id, sectorId) {
        const seat = this.getFullSeatById(id, sectorId);
        return `row: ${seat.y} seat: ${seat.x}`;
    }

    getSector(id) {
        return this.checkoutService.getSector(id);
    }

    getSubTotal(id: number) {
        return this.checkoutService.getSubTotal(id);
    }

    /**
     * removes a selected seat by its id
     * @param id - the seat id
     */
    removeSeat(id: number) {
        const seats = this.checkoutForm.get('seats') as FormArray;
        seats.removeAt(seats.value.indexOf(id));
    }

    /**
     * returns the index of the form array element for the standing sector
     * @param id - the standing sector id
     */
    getFormIndexOfStandingSector(id: number) {
        const standing = this.checkoutForm.get('standing') as FormArray;
        return standing.value.findIndex(sector => sector.id === id);
    }

    getCurrentStandingAmountOfSector(id: number) {
        return this.checkoutService.getCurrentStandingAmountOfSector(id);
    }

    /**
     * calculates if there is still a standing place available for a given sector by checking
     * the selected standing amount + available places
     * @param id - the sector id
     */
    private availableStandingPlaces(id: number) {
        return this.getSector(id).available - this.getCurrentStandingAmountOfSector(id);
    }

    isStandingSectorAmountValid(id: number) {
        const standing = this.checkoutForm.get('standing') as FormArray;
        const currentStanding = standing.controls[this.getFormIndexOfStandingSector(id)] as FormGroup;
        return currentStanding.controls.amount.errors;
    }
}
