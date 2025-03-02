package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Message;
import Model.Account;
import Service.MessageService;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    MessageService messageService;
    AccountService accountService;

    public SocialMediaController() {
        messageService = new MessageService();
        accountService = new AccountService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        app.get("example-endpoint", this::exampleHandler);
        
        app.post("/register", this::registrationHandler);

        app.post("/login", this::loginHandler);

        app.post("/messages", this::postMessageHandler);

        app.get("/messages", this::getAllMessagesHandler);

        app.get("/messages/{message_id}", this::getMessageByIdHandler);

        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);

        app.patch("/messages/{message_id}", this::updateMessageHandler);

        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserHandler);

        return app;
    }

    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registrationHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.register(account);
        if(addedAccount==null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account userAccount = accountService.login(account);
        if(userAccount==null){
            ctx.status(401);
        }else{
            ctx.json(mapper.writeValueAsString(userAccount));
        }
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage==null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(addedMessage));
        }
    }

    private void getAllMessagesHandler(Context ctx){
        ctx.json(messageService.getAllMessages());
    }

    private void getMessageByIdHandler(Context ctx){
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        if(messageService.getMessageById(message_id) != null) {
            ctx.json(messageService.getMessageById(message_id));
        } else{
            ctx.json("");
        }
        
    }

    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        
        Message deleted_message = messageService.deleteMessageById(message_id);

        if(deleted_message != null) {
            ctx.json(mapper.writeValueAsString(deleted_message));
        } else {
            ctx.status(200);
        }
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message);
        System.out.println(updatedMessage);
        if(updatedMessage == null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }

    }

    private void getAllMessagesByUserHandler(Context ctx){
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getAllMessagesByUser(account_id));
    }

}