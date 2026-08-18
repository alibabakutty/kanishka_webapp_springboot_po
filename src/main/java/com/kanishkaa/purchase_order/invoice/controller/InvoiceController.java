package com.kanishkaa.purchase_order.invoice.controller;

import com.kanishkaa.purchase_order.invoice.dto.InvoiceRequest;
import com.kanishkaa.purchase_order.invoice.dto.Item;
import com.kanishkaa.purchase_order.invoice.service.InvoiceWorkflowService;
import com.kanishkaa.purchase_order.invoice.service.PdfService;
import com.kanishkaa.purchase_order.invoice.util.InvoiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.Base64;
import java.math.BigDecimal;
import org.springframework.core.io.ClassPathResource;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final TemplateEngine templateEngine;
    private final PdfService pdfService;
    private final InvoiceWorkflowService workflowService;

    @PostMapping("/generate-from-po/{poId}")
    public ResponseEntity<byte[]> generateFromPurchaseOrder(
            @PathVariable Long poId,
            @RequestParam String invoiceNo,
            @RequestParam(required = false) String address
    ) throws Exception {

        // 1. Build the invoice payload directly out of your PO details
        InvoiceRequest request = workflowService.generateInvoiceRequestFromPO(poId, invoiceNo, address);

        InvoiceUtil invoiceUtil = new InvoiceUtil();
        BigDecimal total = BigDecimal.ZERO;

        // 2. Use the exact list size populated by your inventoryItemCount logic
        int itemCount = request.getItems().size();
        int rowHeight = 40;
        int maxRowsPerPage = 13;
        int emptyRows = maxRowsPerPage - itemCount;
        int spacerHeight = Math.max(0, emptyRows * rowHeight);

        // 3. Format and process item computations
        for (Item item : request.getItems()) {
            BigDecimal amount = item.getRate().multiply(BigDecimal.valueOf(item.getQty()));
            item.setAmount(amount);

            item.setFormattedRate(invoiceUtil.formatINR(item.getRate()));
            item.setFormattedAmount(invoiceUtil.formatINR(amount));
            total = total.add(amount);
        }

        // 4. Bind variables to Thymeleaf Engine
        Context context = new Context();
        context.setVariable("invoiceNo", request.getInvoiceNo());
        context.setVariable("date", invoiceUtil.formatDate(request.getDate()));
        context.setVariable("customerName", request.getCustomerName());
        context.setVariable("address", request.getAddress());
        context.setVariable("items", request.getItems());
        context.setVariable("spacerHeight", spacerHeight); // 👈 Controls your structural padding
        context.setVariable("formattedTotal", invoiceUtil.formatINR(total));
        context.setVariable("totalInWords", invoiceUtil.convertToWords(total));

        // 5. Embed image asset safely
        ClassPathResource resource = new ClassPathResource("static/logo.png");
        byte[] imageBytes = resource.getInputStream().readAllBytes();
        String logoBase64 = Base64.getEncoder().encodeToString(imageBytes);
        context.setVariable("logo", "data:image/png;base64," + logoBase64);

        // 6. Compile and return printable document stream
        String html = templateEngine.process("invoice", context);
        byte[] pdf = pdfService.generatePdf(html);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=invoice_" + invoiceNo + ".pdf")
                .header("Content-Type", "application/pdf")
                .body(pdf);
    }
}
