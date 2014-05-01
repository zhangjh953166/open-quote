package com.ail.payment;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceInterface;
import com.ail.core.DumpService;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;

@ServiceInterface
public class PaymentCancelledService {
    
    @ServiceArgument
    public interface PaymentCancelledArgument extends Argument {
        String getPaymentTokenArg();
        
        void setPaymentTokenArg(String paymentTokenArg);
    }
    
    @ServiceCommand(defaultServiceClass=DumpService.class)
    public interface PaymentCancelledCommand extends PaymentCancelledArgument, Command {
    }
}