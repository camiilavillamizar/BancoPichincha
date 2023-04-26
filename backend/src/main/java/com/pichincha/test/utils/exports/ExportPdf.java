package com.pichincha.test.utils.exports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pichincha.test.models.Dtos.classes.ReportDto;

public class ExportPdf {
 
	public static ByteArrayInputStream reportPdf(List<ReportDto> reports) throws IOException {
		
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		
		try {
			String[] titles = {"Fecha", "Nombre", "Cuenta", "Tipo cuenta", "Tipo Mov.", 
					"Saldo inicial", "Estado de cuenta", "Valor", "Saldo disponible"};
			List<String> headers = Arrays.asList(titles);
		    
			PdfPTable table = new PdfPTable(headers.size()); 
			table.setWidthPercentage(100);
			
			Font headerFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 9);
			Font font = FontFactory.getFont(FontFactory.COURIER, 9);
			
			
			for(String header: headers) {
				PdfPCell hcell =  new PdfPCell(new Phrase(header, headerFont));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
			}
			
			for(ReportDto line: reports) {
				
				List<PdfPCell> cells = new ArrayList<>(); 
				
				PdfPCell dateCell = new PdfPCell(new Phrase(line.getDate(), font));
				cells.add(dateCell); 
				PdfPCell nameCell = new PdfPCell(new Phrase(line.getClientName(), font));
				cells.add(nameCell);
				PdfPCell accountCell = new PdfPCell(new Phrase( String.valueOf(line.getAccountNumber()), font));
				cells.add(accountCell);
				PdfPCell accountTypeCell = new PdfPCell(new Phrase(line.getAccountType().toString(), font));
				cells.add(accountTypeCell);
				PdfPCell transactionTypeCell = new PdfPCell(new Phrase(line.getTransactionType().toString(), font));
				cells.add(transactionTypeCell);
				PdfPCell initialBalanceCell = new PdfPCell(new Phrase(line.getInitialBalance().toString(), font));
				cells.add(initialBalanceCell);
				PdfPCell accountStateCell = new PdfPCell(new Phrase(line.isAccountState() == true ? "TRUE" : "FALSE", font));
				cells.add(accountStateCell);
				PdfPCell amountCell = new PdfPCell(new Phrase(line.getTransactionAmount().toString(), font));
				cells.add(amountCell);
				PdfPCell balanceCell = new PdfPCell(new Phrase(line.getTransactionBalance().toString(), font));
				cells.add(balanceCell);
				
				
				for(PdfPCell cell: cells) {
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);					
				}
			}
			PdfWriter.getInstance(document, out);
			document.open();
			document.add(table);
			document.close();
			
			
		} catch(DocumentException ex) {
			 
		}
		return new ByteArrayInputStream(out.toByteArray());
	}
}
