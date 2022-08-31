import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';
import {InternalError} from '../dtos/internal-error';
import {Notification} from '../dtos/notification';
import {HttpErrorResponse} from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class NotificationService {

    notifications: Subject<Notification>;

    constructor() {
        this.notifications = new Subject();
    }

    getLastMessage() {
    }
    /**
     * Adds a new Error Notification to the Queue
     * @param httpError the error that occurred
     */
    public async error(httpError: HttpErrorResponse) {
        let error: InternalError;
        console.log(httpError);
        if (httpError.error instanceof Blob) {
            const text: string = await httpError.error.text();
            console.log(text);
            error = JSON.parse(text);
        } else if (httpError.error?.messages === undefined) {
            try {
                error = JSON.parse(httpError.error);
            } catch (e) {
                error = <InternalError>{
                    messages: [(httpError.error instanceof ProgressEvent) ? 'Backend seems not to be reachable!' : httpError.error]
                };
            }
        } else {
            error = httpError.error;
        }


        console.log(error);
        const notif: Notification = {
            type: 'danger',
            messages: error.messages,
            timestamp: error.timestamp
        };
        this.notifications.next(notif);
        console.log(this.notifications);
    }

    public errorMessage(message: string) {
        const notif: Notification = {
            type: 'danger',
            messages: [message],
            timestamp: Date.now().toString()
        };
        this.notifications.complete();
        this.notifications.next(notif);
    }

    /**
     * Add a new Success message to the Notification Queue
     * @param message the message to display as a Success Notification
     */
    public success(message: string) {
        const notif: Notification = {
            type: 'success',
            messages: [message],
            timestamp: Date.now().toString()
        };
        this.notifications.next(notif);
    }

}
