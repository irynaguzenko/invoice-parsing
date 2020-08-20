package ai.invoiceparsing;

import java.util.List;

public class Label {
    private String invoiceNumber;
    private List<String> totalAmount;
    private String orderNumber;
    private String lineItemNumber;
    private String lineItemReceivedQty;
    private List<String> lineItemUOM;
    private String lineItemPartDescription;
    private List<String> lineItemUnitPrice;
    private String lineItemPartNumber;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public List<String> getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(List<String> totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getLineItemNumber() {
        return lineItemNumber;
    }

    public void setLineItemNumber(String lineItemNumber) {
        this.lineItemNumber = lineItemNumber;
    }

    public String getLineItemReceivedQty() {
        return lineItemReceivedQty;
    }

    public void setLineItemReceivedQty(String lineItemReceivedQty) {
        this.lineItemReceivedQty = lineItemReceivedQty;
    }

    public List<String> getLineItemUOM() {
        return lineItemUOM;
    }

    public void setLineItemUOM(List<String> lineItemUOM) {
        this.lineItemUOM = lineItemUOM;
    }

    public String getLineItemPartDescription() {
        return lineItemPartDescription;
    }

    public void setLineItemPartDescription(String lineItemPartDescription) {
        this.lineItemPartDescription = lineItemPartDescription;
    }

    public List<String> getLineItemUnitPrice() {
        return lineItemUnitPrice;
    }

    public void setLineItemUnitPrice(List<String> lineItemUnitPrice) {
        this.lineItemUnitPrice = lineItemUnitPrice;
    }

    public String getLineItemPartNumber() {
        return lineItemPartNumber;
    }

    public void setLineItemPartNumber(String lineItemPartNumber) {
        this.lineItemPartNumber = lineItemPartNumber;
    }
}