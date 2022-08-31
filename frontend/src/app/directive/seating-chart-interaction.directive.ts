import {Directive, ElementRef, EventEmitter, HostListener, Input, Output} from '@angular/core';

@Directive({
    selector: '[appSeatingChartInteraction]'
})
export class SeatingChartInteractionDirective {

    @Output() private cursorInformation: EventEmitter<any> = new EventEmitter();
    @Input() hall: any;

    private x: number;
    private y: number;
    private clicked: boolean = false;


    constructor(private elementRef: ElementRef) {
    }

    @HostListener('mouseenter') onMouseEnter() {
        /*console.log('mouseenter');
        console.log(this.elementRef.nativeElement.width);*/
        // this.cursorInformation.emit('mouseenter');
        /*this.seatingPlan = this.elementRef.nativeElement;
        console.log(this.elementRef.nativeElement);*/
    }

    @HostListener('mouseleave') onMouseLeave() {
        // console.log('mouseleave');
        // this.cursorInformation.emit('mouseleave');
        /*this.active = false;
        this.elementRef.nativeElement.querySelector('#active').innerHTML = '';*/

    }

    @HostListener('mousemove', ['$event']) onMouseMove(event) {
        const x = Math.floor(event.offsetX / this.elementRef.nativeElement.clientWidth * this.hall.width);
        const y = Math.floor(event.offsetY / this.elementRef.nativeElement.clientHeight * this.hall.height);

        if (x !== this.x || y !== this.y) {
            this.x = x;
            this.y = y;
            this.sendEvent('mousemove');
        }

        // console.log('mousemove');

        /*console.log(this.seatingPlan.clientWidth);
        this.x = (event.offsetX / this.seatingPlan.clientWidth * this.seatingPlan.getAttribute('width'));
        this.y = (event.offsetY / this.seatingPlan.clientHeight * this.seatingPlan.getAttribute('height'));
        this.x = (Math.floor(this.x / 10));
        this.y = (Math.floor(this.y / 10));

        this.seat = `<use href="#test" x="${this.x * 10}" y="${this.y * 10}"/>`;
        this.elementRef.nativeElement.querySelector('#active').innerHTML = this.seat;*/
    }

    @HostListener('click') onClickEvent() {
        /*this.elementRef.nativeElement.querySelector('#selected').innerHTML += this.seat;*/
    }

    @HostListener('mousedown') onMouseDownEvent() {
        // console.log('mousedown');
        this.clicked = true;
        this.sendEvent('mousedown');
    }

    @HostListener('mouseup') onMouseUpEvent() {
        // console.log('mouseup');
        this.clicked = false;
        this.sendEvent('mouseup');
    }

    private sendEvent(action) {
        this.cursorInformation.emit({
            x: this.x,
            y: this.y,
            clicked: this.clicked,
            action: action
        });
    }
}
