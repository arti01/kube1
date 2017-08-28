<%@page import="java.awt.Color"%>
<%@
page import="java.io.*,java.util.*,java.text.*,com.lowagie.text.*,com.lowagie.text.pdf.*,pl.eod.managwn.*"
pageEncoding="UTF-8"
%><%//
                        response.setContentType("application/pdf");
			response.setHeader( "Content-disposition" , "attachment; filename = WnSzkol.pdf" );
			response.setCharacterEncoding("UTF-8");

			// step 1: creation of a document-object
			Document document = new Document(PageSize.A4,15,15,10,10);

			// step 2:
			// we create a writer that listens to the document
			// and directs a PDF-stream to a temporary buffer

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			PdfWriter writer = PdfWriter.getInstance(document, buffer);

			// step 3: we open the document
			document.open();
         
                        BaseFont f = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.CP1250, BaseFont.NOT_EMBEDDED);
			Font ff = new Font(f, 8, Font.NORMAL);
			Font sff = new Font(f, 7, Font.NORMAL);
			Font ssff = new Font(f, 6, Font.NORMAL);
			Font ffb = new Font(f, 8, Font.BOLD);
                        Font ffbi = new Font(f, 8, Font.ITALIC);
			Font ffb10 = new Font(f, 10, Font.BOLD);
                        Font ff10 = new Font(f, 10, Font.NORMAL);
			Font fboldi = new Font(f, 12, Font.BOLDITALIC);
                        Font fbold = new Font(f, 14, Font.BOLD);

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// hh:mm:ss
                    //  String f00 = df.format(new Date());
                        //DecimalNumberFormat nf = new DecimalNumberFormat("#####0.00");
                        DecimalFormat nf = new DecimalFormat("#####0.00");
    


                        String URL_ROOT = getServletContext().getRealPath(".");
			String URL_IMAGES = URL_ROOT + "/resources/wydruki/logo_druk.jpg";
			Image gif = Image.getInstance(URL_IMAGES);
                        
                        
			gif.scaleToFit(200, 100);

			gif.setAlignment(Image.ALIGN_CENTER);
                        
                        
                        
                        UrlopM urM = (UrlopM)request.getSession().getAttribute("UrlopM"); 
        
                        
                        Paragraph p0 = new Paragraph("\n",ffb10);
			
			Paragraph p = new Paragraph(
					"WNIOSEK SZKOLENIOWY\n\n", fbold);
			//Paragraph p1 = new Paragraph(urM.getUrlop().getUzytkownik().getFullname()+"\n",ffb10);
			
			p0.add(p);
			//p0.add(p1);
			p0.setAlignment("center");
			
			
			PdfPTable titletable = new PdfPTable(2);
			titletable.setWidthPercentage((float)98);
			int[] wids = {40,60};
			titletable.setWidths(wids);
			PdfPCell cp = new PdfPCell(p0);
			cp.setHorizontalAlignment(Element.ALIGN_CENTER);
			cp.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cp.setBorder(0);
                        cp.setBorderWidthBottom((float)0.7);
			
			PdfPCell cgif = new PdfPCell(gif);
			cgif.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cgif.setBorder(0);
			cgif.setBorderWidthBottom((float)0.7);
			
			titletable.addCell(cgif);
			titletable.addCell(cp);
			
                        document.add(titletable);
                        
                        PdfPTable nagtab1 = new PdfPTable(2);
			nagtab1.setWidthPercentage((float)98);
			int[] wids1 = {50,50};
			nagtab1.setWidths(wids);
                        nagtab1.getDefaultCell().setBorderWidth(0);
                        nagtab1.getDefaultCell().setBorder(0);
                        Paragraph n111 = new Paragraph("(imię i nazwisko)",ffbi);
                        Paragraph n112 = new Paragraph("(dział/ośrodek)",ffbi);
                        Paragraph n113 = new Paragraph("(stanowisko)\n",ffbi);
                        
                        Paragraph n11 = new Paragraph("\n\n"+urM.getUrlop().getUzytkownik().getFullname()+"\n",ffb10);
                        n11.add(n111);
                        n11.add("\n\n...............................................\n");
                        n11.add(n112);
                        n11.add("\n\n\n\n\n...............................................\n");
                        n11.add(n113);
                        
                        Paragraph n121 = new Paragraph("(data)",ffbi);
                        Paragraph n122 = new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\nDyrektor Działu / Ośrodka\n\n\n",ffb10);
                        Paragraph n12 = new Paragraph("\n\n"+df.format(urM.getUrlop().getDataWprowadzenia())+"\n",ffb10);
                        n12.add(n121);
                        n12.add(n122);
                        
                        PdfPCell nb11 = new PdfPCell(n11);
                        PdfPCell nb12 = new PdfPCell(n12);
                        nb11.setHorizontalAlignment(Element.ALIGN_LEFT);
                        nb12.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        nb11.setBorder(0);
                        nb12.setBorder(0);
                        
                        nagtab1.addCell(nb11);
                        nagtab1.addCell(nb12);
                        String t2 = "\n\nUprzejmie proszę o wyrażenie zgody na delegowanie mnie na szkolenie na temat: "+urM.getUrlop().getTemat_szkolenia().trim().toUpperCase()+".\n Szkolenie to "
                                + "odbędzie się w terminie od "+df.format(urM.getUrlop().getDataOd())+" do "+df.format(urM.getUrlop().getDataDo())+" w "+urM.getUrlop().getMiejsce().toUpperCase()+".\n\n\n"
                                + "\n\n\n\n\n\n";
                        String t20 = "\n\nUprzejmie proszę o wyrażenie zgody na delegowanie mnie na szkolenie na temat:";
                        String t21 = ".\n Szkolenie to odbędzie się w terminie od ";
                        String t22 = " do ";
                        String t23 = " w ";
                        String t24 = ".\n\n\n\n\n\n\n\n\n";
                        
                        Paragraph n2 = new Paragraph(t2,ffb10);
                        Paragraph n21 = new Paragraph(urM.getUrlop().getTemat_szkolenia().trim(),ffb10);
                        Paragraph n22 = new Paragraph(df.format(urM.getUrlop().getDataOd()),ffb10);
                        Paragraph n23 = new Paragraph(df.format(urM.getUrlop().getDataDo()),ffb10);
                        Paragraph n24 = new Paragraph(urM.getUrlop().getMiejsce().trim(),ffb10);
                        //n2.add(n21);
                        //n2.add(new Paragraph(t21,ff10));
                        //n2.add(n22);
                        //n2.add(new Paragraph(t22,ff10));
                        //n2.add(n23);
                        //n2.add(new Paragraph(t23,ff10));
                        //n2.add(n24);
                        //n2.add(new Paragraph(t24,ff10));
                        
                        PdfPCell nb2 = new PdfPCell(n2);
                        nb2.setColspan(2);
                        nb2.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                        nb2.setBorder(0);
                        nagtab1.addCell(nb2);
                        Paragraph n31 = new Paragraph("(podpis pracownika)\n\n",ffbi);
                        
                        String t3=df.format(urM.getUrlop().getDataWprowadzenia())+", "+urM.getUrlop().getUzytkownik().getFullname().trim()+"\n";
                        Paragraph n3 = new Paragraph(t3,ffb10);
                        n3.add(n31);
                        PdfPCell nb3 = new PdfPCell(n3);
                        nb3.setColspan(2);
                        nb3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        nb3.setBorder(0);
                        nb3.setBorderWidthBottom((float)0.7);
                        nagtab1.addCell(nb3);
                        
                        String t4 = "\n\nPrzewidywane koszty:\n  - całość: "+nf.format(urM.getUrlop().getKwotaWs().doubleValue())+" zł\n\nW tym:\n"
                                + " - wpisowe:"+nf.format(urM.getUrlop().getWpisowe().doubleValue())+" zł\n\n"
                                + " - koszty dojazdu: "+nf.format(urM.getUrlop().getKoszty_dojazdu().doubleValue())+" zł ; PKP/PKS  - Inne (jakie?):  ........................\n\n"
                                + " - hotel: "+nf.format(urM.getUrlop().getHotel().doubleValue())+" zł - limit (jaki?):  ......................\n\n"
                                + " - inne (jakie): "+nf.format(urM.getUrlop().getInne().doubleValue()) +" zł\n\n\n\n";
                        Paragraph n4 = new Paragraph(t4,ffb10);
                        PdfPCell nb4 = new PdfPCell(n4);
                        nb4.setColspan(2);
                        nb4.setHorizontalAlignment(Element.ALIGN_LEFT);
                        nb4.setBorder(0);
                        nagtab1.addCell(nb4);
                                
                        String t5 = "Budżet szkoleniowy (dotyczy TYLKO ośrodków medycznych):\n\n"
                                + "zgodne z założonym budżetem szkoleniowym (PL-R-18-03)\n\n"
                                + "Wyrażam zgodę na delegowanie pracownika na w/w szkolenie.\n\n\n\n\n";

                        Paragraph n5 = new Paragraph(t5,ffb10);
                        PdfPCell nb5 = new PdfPCell(n5);
                        nb5.setColspan(2);
                        nb5.setHorizontalAlignment(Element.ALIGN_LEFT);
                        nb5.setBorder(0);
                        nagtab1.addCell(nb5);
                        
                        String t6 = ""+df.format(urM.getUrlop().getDataOstZmiany())+", "+urM.getUrlop().getZaakceptowal().getFullname().trim()+"\n";
                        Paragraph n6 = new Paragraph(t6,ffb10);        
                        
                        String t7 = "(podpis Przełożonego lub Dyrektora Działu / Ośrodka)";
                        Paragraph n7 = new Paragraph(t7,ffbi);
                        n6.add(n7);
                        PdfPCell nb67 = new PdfPCell(n6);
                        nb67.setColspan(2);
                        nb67.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        nb67.setBorder(0);
                        nagtab1.addCell(nb67);
                        
                        String t8="\n\n\n*proszę wybrać w jakiej walucie ma zostać wypłacona zaliczka – UWAGA: zaliczka w walucie jest wypłacana tylko w przypadku posiadania przez pracownika konta walutowego w jednej z trzech następujących walut: EUR, USD lub GBP";
                        Paragraph n8 = new Paragraph(t8,ssff);
                        PdfPCell nb8 = new PdfPCell(n8);
                        nb8.setColspan(2);
                        nb8.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                        nb8.setBorder(0);
                        nagtab1.addCell(nb8);
                        
                        document.add(nagtab1);
        
        
        
        document.close();

			DataOutput output = new DataOutputStream(response.getOutputStream());
			byte[] bytes = buffer.toByteArray();
			response.setContentLength(bytes.length);
			for (int i = 0; i < bytes.length; i++) {
				output.writeByte(bytes[i]);
			}
		%>
