import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Message} from '../dtos/message';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {Page} from '../dtos/page';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private messageBaseUri: string = this.globals.backendUri + '/messages';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all unread messages from the backend
   */
  getUnreadMessages(pageSize: number, pageNumber: number): Observable<Page<Message>> {
    return this.httpClient.get<Page<Message>>(this.messageBaseUri, {
        params: {
            size: pageSize.toString(),
            page: pageNumber.toString()
        }
    });
  }

    /**
     * Loads all read messages from the backend
     */
    getReadMessages(pageSize: number, pageNumber: number): Observable<Page<Message>> {
        return this.httpClient.get<Page<Message>>(this.messageBaseUri + '/read', {
            params: {
                size: pageSize.toString(),
                page: pageNumber.toString()
            }
        });
    }

  /**
   * Loads specific message from the backend
   * @param id of message to load
   */
  getMessageById(id: number): Observable<Message> {
    console.log('Load message details for ' + id);
    return this.httpClient.get<Message>(this.messageBaseUri + '/' + id);
  }

    /**
     * marks specific message as read
     * @param id of message to be marked as read
     */
    markAsRead(id: number): Observable<Message> {
        return this.httpClient.get<Message>(this.messageBaseUri + '/read/' + id);
    }

  /**
   * Persists message to the backend
   * @param message to persist
   */
  createMessage(message: Message): Observable<Message> {
    console.log('Create message with title ' + message.title);
    return this.httpClient.post<Message>(this.messageBaseUri, message);
  }
}
