package Service;

import java.util.List;
import java.util.Optional;

import DAO.MessagesDAO;
import Model.Message;

public class MessageService {
    MessagesDAO messagesDAO;

    public MessageService(){
        messagesDAO = new MessagesDAO();
    }

    public MessageService(MessagesDAO messagesDAO){
        this.messagesDAO = messagesDAO;
    }

    public Message addMessage(Message message){
        String message_text = message.getMessage_text();
        if( message_text == null || message_text.length() == 0) {
            return null;
        }

        return messagesDAO.insertMessage(message);
    }

    public Message updateMessage(int message_id, Message message){
        if(messagesDAO.getMessageById(message_id) == null) {
            return null;
        }

        String message_text = message.getMessage_text();

        if(message_text == null || message_text.length() == 0 || message_text.length() > 255) {
            return null;
        }

        messagesDAO.updateMessage(message_id, message_text);
        return messagesDAO.getMessageById(message_id);
    }

    public Message getMessageById(int message_id){
        Message message = messagesDAO.getMessageById(message_id);
        if(message == null) {
            return null;
        }

        return messagesDAO.getMessageById(message_id);
    }

    public Message deleteMessageById(int message_id){
        Message message = messagesDAO.getMessageById(message_id);
        if(message == null) {
            return null;
        }

        if(messagesDAO.deleteMessageById(message_id)){
            return message;
        };
         
        return null;
    }

    public List<Message> getAllMessages() {
        return messagesDAO.getAllMessages();
    }

    public List<Message> getAllMessagesByUser(int account_id) {
        return messagesDAO.getAllMessagesByUser(account_id);
    }
}
