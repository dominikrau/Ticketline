import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Ticket} from '../../dtos/ticket';
import {NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {FormBuilder} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {TicketService} from '../../services/ticket.service';
import {ActivatedRoute} from '@angular/router';
import {NotificationService} from '../../services/notification.service';

@Component({
  selector: 'app-ticket',
  templateUrl: './ticket.component.html',
  styleUrls: ['./ticket.component.css']
})
export class TicketComponent implements OnInit {

    error: boolean = false;
    private ticket: Ticket;
    private ticketId: number;
    constructor(private route: ActivatedRoute, private ticketService: TicketService,
                private ngbPaginationConfig: NgbPaginationConfig, private formBuilder: FormBuilder,
                private cd: ChangeDetectorRef, private authService: AuthService,
                private notificationService: NotificationService) { }

    ngOnInit(): void {
        this.loadTicket();
    }


    /**
     * Loads the specified ticket from the backend
     */
    private loadTicket() {
        this.ticketId = +this.route.snapshot.paramMap.get('id');
        this.ticketService.getTicketById(this.ticketId).subscribe(
            (ticket: Ticket) => {
                this.ticket = ticket;
            },
            error => {
                this.notificationService.error(error);
            }
        );
    }

}
